import {Service, Inject} from 'typedi';
import {Deferred} from 'jaasync';

import {PlayerId} from '../types';
import {GameData, GameInfo, GameStore} from '../game/interfaces';
import {User} from "../user/User";
import {Player, PlayerService} from './interfaces';
import {Resource} from '../utils/resource/interfaces';


@Service(PlayerService.Token)
export class PlayerServiceImpl implements PlayerService {
  @Inject(User.Token)
  private readonly user: User;
  @Inject(GameStore.Token)
  private readonly games: GameStore;
  @Resource('/api/v1/players')
  private readonly resource: Resource;

  private readonly players = new Map<PlayerId, Player>();
  
  private fetchQueue = new Map<PlayerId, Deferred<Player>>();
  public async getPlayer(playerId: PlayerId): Promise<Player> {
    const player = this.players.get(playerId);
    if(player) {
      return player;
    }

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
      this.players.set(playerId, player);
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
    return this.normalizeGameInfoListResult(result);
  }

  public async getOwnActiveGames(): Promise<GameData[]> {
    return this.getActiveGames(this.user.playerId);
  }

  public async getGameHistory(playerId: PlayerId): Promise<GameData[]> {
    const result = await this.resource.get<GameInfo[]>(
      `/${playerId}/games?status=COMPLETE,TERMINATED&orderBy=completedAt`)
    return this.normalizeGameInfoListResult(result);
  }

  public async getOwnGameHistory(): Promise<GameData[]> {
    return this.getGameHistory(this.user.playerId);
  }

  public async getPendingGames(playerId: PlayerId): Promise<GameData[]> {
    const result = await this.resource.get<GameInfo[]>(
      `/${playerId}/games?status=PENDING,DECLINED&orderBy=createdAt`);
    return this.normalizeGameInfoListResult(result);
  }

  public async getOwnPendingGames(): Promise<GameData[]> {
    return this.getPendingGames(this.user.playerId);
  }

  private normalizeGameInfoListResult(result: GameInfo[]): GameInfo[] {
    return result.map(this.games.updateGameInfo);
  }
}