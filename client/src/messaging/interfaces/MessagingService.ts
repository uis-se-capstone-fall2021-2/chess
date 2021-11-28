import * as typedi from 'typedi';

export interface MessagingService {
  subscribe<TMessageBody>(topic: string, handler: (message: TMessageBody) => void): Promise<{unsubscribe: () => void}>;
}

export namespace MessagingService {
  export const Token = new typedi.Token<MessagingService>('MessagingService');
}