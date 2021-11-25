import * as Strongbus from 'strongbus';
import * as typedi from 'typedi';

import {Player} from '../../player/interfaces/Player';
import {PlayerColor} from '../../player/interfaces/PlayerColor';
import {GameData, GameState} from './Game';
import {GameId} from './GameId';

export interface GameService {
  subscribe(gameId: GameId, handler: () => void): Strongbus.Subscription;
  getGame(gameId: GameId): GameData|null;
  fetchGameState(gameId: GameId): Promise<GameState>;
  createGame(params: {opponent: Player, playerColor: PlayerColor}): Promise<GameState>;
  quitGame(gameId: GameId): Promise<void>;
  acceptGameInvite(gameId: GameId): Promise<void>;
  declineGameInvite(gameId: GameId): Promise<void>;
  cancelGameInvite(gameId: GameId): Promise<void>;
}

export namespace GameService {
  export const Token = new typedi.Token<GameService>('GameService');
}