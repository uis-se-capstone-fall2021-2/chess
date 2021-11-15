import * as typedi from 'typedi';

export interface User {
  displayName: string;
  token: string;
  playerId: number;
}

export namespace User {
  export const Token = new typedi.Token<User>('User');
}