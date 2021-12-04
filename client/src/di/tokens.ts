import * as typedi from 'typedi';

export namespace Tokens {
  export const API_HOST = new typedi.Token<string>('ApiHost');
  export const STOMP_BROKER_BASE_URL = new typedi.Token<string>('StompUrl');
  export const AUTH_0_CLIENT_ID = new typedi.Token('Auth0ClientID');
}