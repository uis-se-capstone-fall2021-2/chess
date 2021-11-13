import {autobind} from 'core-decorators';
import * as Strongbus from 'strongbus';
import {Service, Inject} from 'typedi';
import {Deferred} from 'jaasync';

import {PlayerId} from '../types';
import {GameData, GameInfo, GameStore} from '../game/interfaces';
import {User} from "../user/User";
import {Player, PlayerService, PlayerStore} from './interfaces';
import {Resource} from '../utils/resource/interfaces';


@Service(PlayerService.Token)
@autobind
export class PlayerServiceImpl implements PlayerService {
  @Inject(User.Token)
  private readonly user: User;
  @Inject(GameStore.Token)
  private readonly games: GameStore;
  @Inject(PlayerStore.Token)
  private readonly players: PlayerStore;
  @Resource('/api/v1/players')
  private readonly resource: Resource;

  public on<T extends Strongbus.Listenable<Strongbus.EventKeys<PlayerStore.Events>>>(
    event: T,
    handler: Strongbus.EventHandler<PlayerStore.Events, T>
  ): Strongbus.Subscription {
      return this.players.on(event, handler);
  }

  public getPlayer(playerId: PlayerId): Player {
    return this.players.getPlayer(playerId);
  }
  
  private fetchQueue = new Map<PlayerId, Deferred<Player>>();
  public async fetchPlayer(playerId: PlayerId): Promise<Player> {
    const scheduleFetch = this.fetchQueue.size === 0;
    let promise: Deferred<Player>|undefined = this.fetchQueue.get(playerId);
    if(!promise) {
      promise = new Deferred<Player>();
      this.fetchQueue.set(playerId, promise);
    }

    if(scheduleFetch) {
      setTimeout(this.fetchPlayers, 0);
    }

    return await promise;
  }

  private async fetchPlayers(): Promise<void> {
    const queue = this.fetchQueue;
    this.fetchQueue = new Map<PlayerId, Deferred<Player>>();

    const ids = [...queue.keys()];
    const players = await this.resource.get<Player[]>(`/?id=${ids.join(',')}`);

    for(const player of players) {
      const {playerId} = player;
      this.players.upsertPlayer(player);
      const promise = queue.get(playerId);
      if(promise) {
        promise.resolve(player);
      }
      queue.delete(playerId);
    }

    for(const promise of queue.values()) {
      promise.reject();
    }
  }

  public async getActiveGames(playerId: PlayerId): Promise<GameData[]> {
    const result = await this.resource.get<GameInfo[]>(
      `/${playerId}/games?status=ACTIVE&orderBy=updatedAt`);
    return this.localizeGameInfoResult(result);
  }

  public async getOwnActiveGames(): Promise<GameData[]> {
    return this.getActiveGames(this.user.playerId);
  }

  public async getGameHistory(playerId: PlayerId): Promise<GameData[]> {
    const result = await this.resource.get<GameInfo[]>(
      `/${playerId}/games?status=COMPLETE,TERMINATED&orderBy=completedAt`)
    return this.localizeGameInfoResult(result);
  }

  public async getOwnGameHistory(): Promise<GameData[]> {
    return this.getGameHistory(this.user.playerId);
  }

  public async getPendingGames(playerId: PlayerId): Promise<GameData[]> {
    const result = await this.resource.get<GameInfo[]>(
      `/${playerId}/games?status=PENDING,DECLINED&orderBy=createdAt`);
    return this.localizeGameInfoResult(result);
  }

  public async getOwnPendingGames(): Promise<GameData[]> {
    return this.getPendingGames(this.user.playerId);
  }

  private localizeGameInfoResult(result: GameInfo[]): GameInfo[] {
    return result.map(this.games.upsertGameInfo);
  }
}