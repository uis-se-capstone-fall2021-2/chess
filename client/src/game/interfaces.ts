import * as Strongbus from 'strongbus';
import * as typedi from 'typedi';

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
  getGame(gameId: GameId): GameData|null;
  fetchGameState(gameId: GameId): Promise<GameState>;
  on: Strongbus.Bus<GameStore.Events>['on'];
}

export namespace GameService {
  export const Token = new typedi.Token<GameService>('GameService');
}

export interface GameStore {
  getGame(gameId: GameId): GameData|null;
  upsertGameInfo(info: GameInfo): GameData;
  upsertGameState(state: GameState): GameData;
  on: Strongbus.Bus<GameStore.Events>['on'];
}

export namespace GameStore {
  export const Token = new typedi.Token('GameStore');

  export type Events = {
    GAME_ADDED: GameId;
    [key: `GAME_UPDATED_${GameId}`]: (keyof GameData)[]
  }
}