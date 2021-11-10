import * as typedi from 'typedi';

export namespace Tokens {
  export const API_HOST = new typedi.Token<string>('ApiHost');
}