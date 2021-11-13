import * as Strongbus from 'strongbus';
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
  fetchPlayer(playerId: PlayerId): Promise<Player>;
  getPlayer(playerId: PlayerId): Player;
  on: Strongbus.Bus<PlayerStore.Events>['on'];
}

export namespace PlayerService {
  export const Token = new typedi.Token<PlayerService>('PlayerService');
}


export interface PlayerStore {
  getPlayer(playerId: PlayerId): Player;
  upsertPlayer(player: Player): void;
  on: Strongbus.Bus<PlayerStore.Events>['on'];
}

export namespace PlayerStore {
  export const Token = new typedi.Token('PlayerStore');

  export type Events = {
    PLAYER_ADDED: PlayerId;
    [event: `PLAYER_UPDATED_${PlayerId}`]: (keyof Player)[];
  };
}