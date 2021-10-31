import {User} from "../user/User";



export class GameService {
  private readonly user: User;
  constructor(params: GameService.Params) {
    this.user = params.user;
  }
}

export namespace GameService {
  export interface Params {
    user: User;
  }
}