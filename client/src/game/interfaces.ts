import * as Strongbus from 'strongbus';
import * as typedi from 'typedi';
import {MoveIntent} from '../board/interfaces';

import {GameId, PlayerId} from '../types';

export enum GameStatus {
  PENDING = 'PENDING',
  DECLINED = 'DECLINED',
  ACTIVE = 'ACTIVE',
  COMPLETE = 'COMPLETE',
  TERMINATED = 'TERMINATED'
}

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




export interface GameService {
  move(gameId: GameId, intent: MoveIntent): Promise<GameState>;
  getGameData(gameId: GameId): GameData|null;
  fetchGameState(gameId: GameId): Promise<GameState>;
  on: Strongbus.Bus<GameStore.Events>['on'];
}

export namespace GameService {
  export const Token = new typedi.Token<GameService>('GameService');
}

export interface GameStore {
  getGameData(gameId: GameId): GameData|null;
  updateGameInfo(info: GameInfo): GameData;
  updateGameState(state: GameState): GameData;
  on: Strongbus.Bus<GameStore.Events>['on'];
}

export namespace GameStore {
  export const Token = new typedi.Token('GameStore');

  export type Events = {
    GAME_ADDED: GameId;
    [key: `GAME_UPDATED_${number}`]: (keyof GameData)[]
  }
}