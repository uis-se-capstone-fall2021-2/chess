import * as Strongbus from 'strongbus';
import * as typedi from 'typedi';

import {GameData} from '../../game/interfaces';
import {Player} from './Player';
import {PlayerId} from './PlayerId';
import {PlayerStore} from './PlayerStore';
import {PlayerType} from './PlayerType';

export interface PlayerService {
  fetchPlayer(playerId: PlayerId): Promise<Player>;
  getPlayer(playerId: PlayerId): Player;
  searchPlayers(query: string, playerType: PlayerType): Promise<Player[]>;
  getActiveGames(playerId: PlayerId): Promise<GameData[]>;
  getOwnActiveGames(): Promise<GameData[]>;
  getPendingGames(playerId: PlayerId): Promise<GameData[]>;
  getOwnPendingGames(): Promise<GameData[]>;
  getGameHistory(playerId: PlayerId): Promise<GameData[]>;
  getOwnGameHistory(): Promise<GameData[]>;
  on: Strongbus.Bus<PlayerStore.Events>['on'];
}

export namespace PlayerService {
  export const Token = new typedi.Token<PlayerService>('PlayerService');
}