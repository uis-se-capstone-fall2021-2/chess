import {autobind} from 'core-decorators';
import * as Strongbus from 'strongbus';
import {Service, Inject} from 'typedi';

import {GameId} from '../types';
import {User} from "../user/interfaces";
import {Resource} from '../utils/resource/interfaces';
import {GameData, GameService, GameState, GameStore} from './interfaces';


@Service(GameService.Token)
@autobind
export class GameServiceImpl implements GameService {
  
  @Inject(User.Token)
  private readonly user: User;
  @Inject(GameStore.Token)
  private readonly games: GameStore;
  @Resource('/api/v1/games')
  private readonly resource: Resource;

  public getGame(gameId: GameId): GameData|null {
    return this.games.getGame(gameId);
  }

  public async fetchGameState(gameId: GameId) {
    const gameState = await this.resource.get<GameState>(`/${gameId}`);
    this.games.upsertGameState(gameState);
    return gameState;
  }

  public on<T extends Strongbus.Listenable<Strongbus.EventKeys<GameStore.Events>>>(
    event: T,
    handler: Strongbus.EventHandler<GameStore.Events, T>
  ): Strongbus.Subscription {
      return this.games.on(event, handler);
  }

  public async quitGame(gameId: number): Promise<void> {
    await this.resource.post(`/${gameId}/quit`, {});
    this.fetchGameState(gameId);
  }

  public async acceptGameInvite(gameId: number): Promise<void> {
    await this.resource.post(`/${gameId}/invitation/accept`, {});
    this.fetchGameState(gameId);
  }

  public async declineGameInvite(gameId: number): Promise<void> {
    await this.resource.post(`/${gameId}/invitation/decline`, {});
    this.fetchGameState(gameId);
  }

}
