import {PlayerId} from '../../player/interfaces/PlayerId';
import {GameId} from './GameId';
import {GameStatus} from './GameStatus';

export interface GameInfo {
  gameId: GameId;
  owner: PlayerId;
  winner: PlayerId;
  players: [PlayerId, PlayerId],
  moveCount: number;
  status: GameStatus;
  createdAt: Date;
  updatedAt: Date;
  completedAt: Date;
}

interface GameStateExtensions {
  playerInCheck: PlayerId;
  board: number[];
}


export interface GameState extends GameInfo, GameStateExtensions {}

export interface GameData extends GameInfo, Partial<GameStateExtensions> {}