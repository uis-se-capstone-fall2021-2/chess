import {autobind} from 'core-decorators';
import * as Strongbus from 'strongbus';
import {Service, Inject} from 'typedi';
import {MoveIntent} from '../../board/interfaces';

import {Player} from '../../player/interfaces/Player';
import {PlayerColor} from '../../player/interfaces/PlayerColor';
import {Resource} from '../../utils/resource/interfaces';
import {GameId, GameData, GameService, GameState, GameStore} from '../interfaces';


@Service(GameService.Token)
@autobind
export class GameServiceImpl implements GameService {

  @Inject(GameStore.Token)
  private readonly games: GameStore;
  @Resource('/api/v1/games')
  private readonly resource: Resource;

  public on<T extends Strongbus.Listenable<Strongbus.EventKeys<GameStore.Events>>>(
    event: T,
    handler: Strongbus.EventHandler<GameStore.Events, T>
  ): Strongbus.Subscription {
      return this.games.on(event, handler);
  }

  public getGame(gameId: GameId): GameData|null {
    return this.games.getGame(gameId);
  }

  public async fetchGameState(gameId: GameId) {
    const gameState = await this.resource.get<GameState>(`/${gameId}`);
    this.games.upsertGameState(gameState);
    return gameState;
  }

  public async createGame(params: {opponent: Player, playerColor: PlayerColor}): Promise<GameState> {
    const gameState = await this.resource.post<GameState>('/', {
      opponentId: params.opponent.playerId,
      playerColor: params.playerColor
    });
    this.games.upsertGameState(gameState);
    return gameState;
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

  public async cancelGameInvite(gameId: number): Promise<void> {
    await this.resource.post(`/${gameId}/invitation/cancel`, {});
    this.games.removeGame(gameId);
  }

  public async move(gameId: GameId, intent: MoveIntent): Promise<GameState> {
    const gameState = await this.resource.post<GameState>(`/${gameId}/move`, intent);
    this.games.upsertGameState(gameState);
    return gameState;
  }
}
