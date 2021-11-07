import * as typedi from 'typedi';

import {GameInfo} from '../game/interfaces';

export interface PlayerService {
  getOwnActiveGames(): Promise<GameInfo[]>;
  getOwnPendingGames(): Promise<GameInfo[]>;
}

export namespace PlayerService {
  export const Token = new typedi.Token<PlayerService>('PlayerService');
}