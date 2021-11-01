import {Service, Inject} from 'typedi';

import {User} from "../user/User";
import {GameService} from './interfaces';


@Service(GameService.Token)
export class GameServiceImpl implements GameService {
  @Inject(User.Token)
  private readonly user: User;
}
