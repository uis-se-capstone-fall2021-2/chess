import * as Strongbus from 'strongbus';
import * as typedi from 'typedi';

import {GameData, GameInfo, GameState} from './Game';
import {GameId} from './GameId';

export interface GameStore {
  on: Strongbus.Bus<GameStore.Events>['on'];
  getGame(gameId: GameId): GameData|null;
  upsertGameInfo(info: GameInfo): GameData;
  upsertGameState(state: GameState): GameData;
  removeGame(gameId: GameId): void
}

export namespace GameStore {
  export const Token = new typedi.Token('GameStore');

  export type Events = {
    GAME_ADDED: GameId;
    [key: `GAME_UPDATED_${GameId}`]: (keyof GameData)[];
    GAME_REMOVED: GameId;
    [key: `GAME_REMOVED_${GameId}`]: void;
  }
}