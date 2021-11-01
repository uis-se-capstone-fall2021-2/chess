import axios from 'axios';
import {Service, Inject} from 'typedi';

import {Tokens} from '../di';
import {GameCompletionState, GameInfo} from '../game/interfaces';
import {User} from "../user/User";
import {PlayerService} from './interfaces';


@Service(PlayerService.Token)
export class PlayerServiceImpl implements PlayerService {
  @Inject(User.Token)
  private readonly user: User;
  @Inject(Tokens.API_HOST)
  private readonly apiHost: string;

  public async getActiveGames(): Promise<GameInfo[]> {
    const {data: result} = await axios.get<GameInfo[]>(`${this.apiHost}/api/v1/players/${this.user.playerId}/games/active`, {
      headers: {
        Authorization: `Bearer ${this.user.token}`
      }
    });

    return PlayerServiceImpl.normalizeGameInfoListResult(result);
  }

  public async getOwnGameHistory(): Promise<GameInfo[]> {
    return this.getGameHistory(this.user.playerId);
  }

  public async getGameHistory(playerId: number): Promise<GameInfo[]> {
    const {data: result} = await axios.get<GameInfo[]>(`${this.apiHost}/api/v1/players/${playerId}/games/history`, {
      headers: {
        Authorization: `Bearer ${this.user.token}`
      }
    });

    return PlayerServiceImpl.normalizeGameInfoListResult(result);
  }

  private static normalizeGameInfoListResult(result: GameInfo[]): GameInfo[] {
    return result.map(info => ({
      ...info,
      state: GameCompletionState[info.state]
    }));
  }
}