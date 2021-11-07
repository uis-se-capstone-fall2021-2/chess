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

  public async getOwnActiveGames(): Promise<GameInfo[]> {
    const resource: string = `/api/v1/players/${this.user.playerId}/games?status=ACTIVE&orderBy=updatedAt`;
    const {data: result} = await axios.get<GameInfo[]>(
      `${this.apiHost}${resource}`, {
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
    const resource: string = `/api/v1/players/${playerId}/games?status=COMPLETE&status=TERMINATED&orderBy=completedAt`
    const {data: result} = await axios.get<GameInfo[]>(
      `${this.apiHost}${resource}`
    , {
      headers: {
        Authorization: `Bearer ${this.user.token}`
      }
    });

    return PlayerServiceImpl.normalizeGameInfoListResult(result);
  }

  public async getOwnPendingGames(): Promise<GameInfo[]> {
    const resource: string = `/api/v1/players/${this.user.playerId}/games?status=PENDING&status=DECLINED&orderBy=createdAt`
    const {data: result} = await axios.get<GameInfo[]>(
      `${this.apiHost}${resource}`
    , {
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