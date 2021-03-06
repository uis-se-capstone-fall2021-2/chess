import {autobind} from 'core-decorators';
import {sleep} from 'jaasync/lib';
import {over} from 'lodash';
import * as Strongbus from 'strongbus';
import {Service, Inject} from 'typedi';
import {MoveIntent} from '../../board/interfaces';
import {MessagingService} from '../../messaging/interfaces';

import {Player} from '../../player/interfaces/Player';
import {PlayerColor} from '../../player/interfaces/PlayerColor';
import {User} from '../../user/interfaces';
import {Resource} from '../../utils/resource/interfaces';
import {GameId, GameData, GameService, GameState, GameStore} from '../interfaces';
import {GameSubscription} from './GameSubscription';


@Service(GameService.Token)
@autobind
export class GameServiceImpl implements GameService {

  @Inject(User.Token)
  private readonly user: User;
  @Inject(GameStore.Token)
  private readonly games: GameStore;
  @Resource('/api/v1/games')
  private readonly resource: Resource;
  @Inject(MessagingService.Token)
  private readonly messagingService: MessagingService;
  
  private readonly subscriptions = new Map<GameId, GameSubscription>();
  private flaggedGames = new Set<GameId>();

  constructor() {
    this.updateGamesAsNeeded();
  }

  private async updateGamesAsNeeded(): Promise<void> {
    while(true) {
      const flagged = this.flaggedGames;
      this.flaggedGames = new Set<GameId>();
      if(flagged.size) {
        try {
          await this.fetchGameStates([...flagged]);
        } catch(e) {
          flagged.forEach(gameId => this.flaggedGames.add(gameId));
        }
      }
      await sleep(250);
    }
  }

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

  private async fetchGameStates(gameIds: GameId[]): Promise<void> {
    const gameStates = await this.resource.get<GameState[]>(`/?id=${gameIds.join(',')}`);
    for(const gameState of gameStates) {
      this.games.upsertGameState(gameState);
    }
  }

  public async createGame(params: {opponent: Player, playerColor: PlayerColor}): Promise<GameState> {
    const gameState = await this.resource.post<GameState>('/', {
      opponentId: params.opponent.playerId,
      playerColor: params.playerColor
    });
    this.games.upsertGameState(gameState);
    return gameState;
  }

  public async quitGame(gameId: GameId): Promise<void> {
    await this.resource.post(`/${gameId}/quit`, {});
    this.fetchGameState(gameId);
  }

  public async acceptGameInvite(gameId: GameId): Promise<void> {
    await this.resource.post(`/${gameId}/invitation/accept`, {});
    this.fetchGameState(gameId);
  }

  public async declineGameInvite(gameId: GameId): Promise<void> {
    await this.resource.post(`/${gameId}/invitation/decline`, {});
    this.fetchGameState(gameId);
  }

  public async cancelGameInvite(gameId: GameId): Promise<void> {
    await this.resource.post(`/${gameId}/invitation/cancel`, {});
    this.games.removeGame(gameId);
  }

  public async move(gameId: GameId, intent: MoveIntent): Promise<GameState> {
    const gameState = await this.resource.post<GameState>(`/${gameId}/move`, intent);
    this.games.upsertGameState(gameState);
    return gameState;
  }

  public subscribe(gameId: GameId, handler: () => void): Strongbus.Subscription {
    let realtimeGameUpdates = this.subscriptions.get(gameId);
    if(!realtimeGameUpdates) {
      realtimeGameUpdates = new GameSubscription({
        user: this.user,
        gameId,
        messagingService: this.messagingService,
        onGameUpdated: () => this.flaggedGames.add(gameId),
        onGameDeleted: () => this.games.removeGame(gameId)
      });
      this.subscriptions.set(gameId, realtimeGameUpdates);
    }
    const subscriptions = [
      realtimeGameUpdates.subscribe(),
      this.games.on(`GAME_UPDATED_${gameId}`, handler),
      this.games.on(`GAME_REMOVED_${gameId}`, handler)
    ];

    if(!this.games.getGame(gameId)) {
      (async () => {
        try {
          await this.fetchGameState(gameId);
        } catch(e) {
          console.warn(`Unable to fetch game ${gameId}`, e);
        }
      })();
    }

    return Strongbus.generateSubscription(over(subscriptions));
  }

  public async download(gameId: GameId): Promise<void> {
    const pgn = await this.resource.get<string>(`/${gameId}/export`);
    const {createObjectURL, revokeObjectURL} = window.URL || window.webkitURL;
    const blob = new Blob([pgn], {type: 'text/plain'});
    const url = createObjectURL(blob);
    const a = document.createElement('a');
    a.setAttribute('href', url);
    a.setAttribute('download', `game_${gameId}.pgn`);
    a.click();
    revokeObjectURL(url);
  }

  public async getMoveHistory(gameId: GameId): Promise<string[]> {
    const history = await this.resource.get<string[]>(`/${gameId}/moves/history`);
    return history;
  }
}
