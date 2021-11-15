import {autobind} from 'core-decorators';
import {keys, isEqual, uniq, update} from 'lodash';
import {Service} from 'typedi';
import * as Strongbus from 'strongbus';

import {GameData, GameId, GameInfo, GameState, GameStatus, GameStore} from '../interfaces';

@Service(GameStore.Token)
@autobind
export class GameStoreImpl implements GameStore {
  private readonly games = new Map<GameId, GameData>();
  private readonly bus = new Strongbus.Bus<GameStore.Events>();
  public readonly on = this.bus.on;

  public getGame(gameId: GameId): GameData|null {
    return this.games.get(gameId) ?? null;
  }

  public upsertGameInfo(info: GameInfo): GameData {
    return this.upsertGameData(info);
  }

  public upsertGameState(state: GameState): GameData {
    return this.upsertGameData(state as any);
  }

  public removeGame(gameId: GameId): void {
    this.games.delete(gameId);
    this.bus.emit('GAME_REMOVED', gameId);
    this.bus.emit(`GAME_REMOVED_${gameId}`, void(0));
  }

  private upsertGameData(data: GameData): GameData {
    let added: boolean = false;
    const {gameId} = data;
    let game = this.games.get(gameId);
    if(!game) {
      game = data;
      added = true;
    }

    const updated: GameData = {
      ...game,
      ...data
    };

    updated.status = GameStatus[updated.status];
    updated.createdAt = new Date(updated.createdAt);
    updated.updatedAt = new Date(updated.updatedAt);
    updated.completedAt = new Date(updated.completedAt);
    
    this.games.set(gameId, updated);
    if(added) {
      this.bus.emit('GAME_ADDED', gameId);
      this.bus.emit(`GAME_UPDATED_${gameId}`, keys(updated) as (keyof GameData)[]);
    } else {
      const updatedKeys: (keyof GameData)[] = [];
      for(const k of uniq([...keys(game), ...keys(updated)])) {
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