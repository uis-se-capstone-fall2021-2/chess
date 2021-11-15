import {PlayerId} from '../../player/interfaces/PlayerId';
import {GameId} from './GameId';
import {GameStatus} from './GameStatus';

interface GameDataBase {
  gameId: GameId;
  owner: PlayerId;
  winner: PlayerId;
  players: [PlayerId, PlayerId],
  moveCount: number;
  status: GameStatus;
}

interface GameInfoExtensions {
  createdAt: Date;
  updatedAt: Date;
  completedAt: Date;
}

interface GameStateExtensions {
  playerInCheck: PlayerId;
  board: number[];
}


export interface GameInfo extends GameDataBase, GameInfoExtensions {}

export interface GameState extends GameDataBase, GameStateExtensions {}

export interface GameData extends GameInfo, Partial<GameStateExtensions> {}