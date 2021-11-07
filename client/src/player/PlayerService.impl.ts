import {Service, Inject} from 'typedi';
import {Deferred} from 'jaasync';

import {GameCompletionState, GameInfo} from '../game/interfaces';
import {User} from "../user/User";
import {Player, PlayerService} from './interfaces';
import {Resource, ResourceFactory} from '../utils/resource/interfaces';


@Service(PlayerService.Token)
export class PlayerServiceImpl implements PlayerService {
  @Inject(User.Token)
  private readonly user: User;
  @Resource('/api/v1/players')
  private readonly resource: Resource;

  private readonly players = new Map<number, Player>();
  
  private fetchQueue = new Map<number, Deferred<Player>>();
  public async getPlayer(playerId: number): Promise<Player> {
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
    this.fetchQueue = new Map<number, Deferred<Player>>();

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

  public async getActiveGames(playerId: number): Promise<GameInfo[]> {
    const result = await this.resource.get<GameInfo[]>(
      `/${playerId}/games?status=ACTIVE&orderBy=updatedAt`);
    return PlayerServiceImpl.normalizeGameInfoListResult(result);
  }

  public async getOwnActiveGames(): Promise<GameInfo[]> {
    return this.getActiveGames(this.user.playerId);
  }

  public async getGameHistory(playerId: number): Promise<GameInfo[]> {
    const result = await this.resource.get<GameInfo[]>(
      `/${playerId}/games?status=COMPLETE,TERMINATED&orderBy=completedAt`)
    return PlayerServiceImpl.normalizeGameInfoListResult(result);
  }

  public async getOwnGameHistory(): Promise<GameInfo[]> {
    return this.getGameHistory(this.user.playerId);
  }

  public async getPendingGames(playerId: number): Promise<GameInfo[]> {
    const result = await this.resource.get<GameInfo[]>(
      `/${playerId}/games?status=PENDING,DECLINED&orderBy=createdAt`);
    return PlayerServiceImpl.normalizeGameInfoListResult(result);
  }

  public async getOwnPendingGames(): Promise<GameInfo[]> {
    return this.getPendingGames(this.user.playerId);
  }

  private static normalizeGameInfoListResult(result: GameInfo[]): GameInfo[] {
    return result.map(info => ({
      ...info,
      state: GameCompletionState[info.state]
    }));
  }
}