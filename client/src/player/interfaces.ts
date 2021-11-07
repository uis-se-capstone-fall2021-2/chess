import * as typedi from 'typedi';

import {PlayerId} from '../types';
import {GameData} from '../game/interfaces';

export interface Player {
  playerId: PlayerId;
  displayName: string;
}

export interface PlayerService {
  getActiveGames(playerId: PlayerId): Promise<GameData[]>;
  getOwnActiveGames(): Promise<GameData[]>;
  getPendingGames(playerId: PlayerId): Promise<GameData[]>;
  getOwnPendingGames(): Promise<GameData[]>;
  getGameHistory(playerId: PlayerId): Promise<GameData[]>;
  getOwnGameHistory(): Promise<GameData[]>;
}

export namespace PlayerService {
  export const Token = new typedi.Token<PlayerService>('PlayerService');
}
