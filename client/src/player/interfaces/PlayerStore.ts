import * as Strongbus from 'strongbus';
import * as typedi from 'typedi';

import {Player} from './Player';
import {PlayerId} from './PlayerId';


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