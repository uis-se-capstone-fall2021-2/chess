import {autobind} from 'core-decorators';
import {isEqual, uniq} from 'lodash';
import {Service} from 'typedi';
import * as Strongbus from 'strongbus';

import {GameId} from '../types';
import {GameData, GameInfo, GameState, GameStatus, GameStore} from './interfaces';

@Service(GameStore.Token)
@autobind
export class GameStoreImpl implements GameStore {
  private readonly games = new Map<GameId, GameData>();
  private readonly bus = new Strongbus.Bus<GameStore.Events>();
  public readonly on = this.bus.on;

  public getGameData(gameId: GameId): GameData|null {
    return this.games.get(gameId) ?? null;
  }

  public updateGameInfo(info: GameInfo): GameData {
    return this.updateGameData(info);
  }

  public updateGameState(state: GameState): GameData {
    return this.updateGameData(state as any);
  }

  private updateGameData(data: GameData): GameData {
    let added: boolean = false;
    const {gameId} = data;
    let game = this.games.get(gameId);
    if(!game) {
      game = data;
      added = true;
    }

    const updated: GameData = {
      ...game,
      ...data,
      status: GameStatus[game.status]
    };
    
    this.games.set(gameId, updated);
    if(added) {
      this.bus.emit('GAME_ADDED', gameId);
      this.bus.emit(`GAME_UPDATED_${gameId}`, Object.keys(updated) as (keyof GameData)[]);
    } else {
      const updatedKeys: (keyof GameData)[] = [];
      for(const k of uniq([...Object.keys(game), ...Object.keys(updated)])) {
        const key = k as keyof GameData;
        if(!isEqual(game[key], updated[key])) {
          updatedKeys.push(key);
        }
      }
      this.bus.emit(`GAME_UPDATED_${gameId}`, updatedKeys);
    }

    return updated;
  }

}