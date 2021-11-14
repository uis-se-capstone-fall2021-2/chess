import {autobind} from 'core-decorators';
import * as Strongbus from 'strongbus';
import {Service, Inject} from 'typedi';
import {MoveIntent} from '../board/interfaces';

import {GameId} from '../types';
import {User} from "../user/User";
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

  public getGameData(gameId: GameId): GameData|null {
    return this.games.getGameData(gameId);
  }

  public async fetchGameState(gameId: GameId) {
    const gameState = await this.resource.get<GameState>(`/${gameId}`);
    this.games.updateGameState(gameState);
    return gameState;
  }

  public on<T extends Strongbus.Listenable<Strongbus.EventKeys<GameStore.Events>>>(
    event: T,
    handler: Strongbus.EventHandler<GameStore.Events, T>
  ): Strongbus.Subscription {
      return this.games.on(event, handler);
  }

  public async move(gameId: GameId, intent: MoveIntent): Promise<GameState> {
    const gameState = await this.resource.patch<GameState>(`/${gameId}`, intent);
    this.games.updateGameState(gameState);
    return gameState;
  }

}
