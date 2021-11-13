import {autobind} from 'core-decorators';
import {uniq, keys, isEqual} from 'lodash';
import * as Strongbus from 'strongbus';
import {Service} from 'typedi';


import {PlayerId} from '../types';
import {PlayerStore, Player} from './interfaces';

@Service(PlayerStore.Token)
@autobind
export class PlayerStoreImpl implements PlayerStore {
  
  private readonly players = new Map<PlayerId, Player>();
  private readonly bus = new Strongbus.Bus<PlayerStore.Events>();
  public readonly on = this.bus.on;

  public getPlayer(playerId: PlayerId): Player {
    return this.players.get(playerId);
  }
  public upsertPlayer(player: Player): void {
    const {playerId} = player;
    const existing = this.players.get(playerId);
    if(!existing) {
      this.players.set(playerId, player);
      this.bus.emit('PLAYER_ADDED', playerId);
      this.bus.emit(`PLAYER_UPDATED_${playerId}`, keys(player) as (keyof Player)[]);
    } else {
      const updatedKeys: (keyof Player)[] = [];
      for(const k of uniq([...keys(existing), ...keys(player)])) {
        const key = k as keyof Player;
        if(!isEqual(player[key], existing[key])) {
          updatedKeys.push(key);
        }
      }
      this.bus.emit(`PLAYER_UPDATED_${playerId}`, updatedKeys);
    }
  }
}
