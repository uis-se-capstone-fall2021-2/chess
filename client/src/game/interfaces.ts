import * as typedi from 'typedi';

export enum GameCompletionState {
  ACTIVE = 'ACTIVE',
  COMPLETE = 'COMPLETE',
  TERMINATED = 'TERMINATED'
}

export interface GameInfo {
  gameId: number;
  owner: number;
  winner: number;
  players: [number, number],
  moveCount: number;
  state: GameCompletionState;
}


export interface GameService {}

export namespace GameService {
  export const Token = new typedi.Token<GameService>('GameService');
}