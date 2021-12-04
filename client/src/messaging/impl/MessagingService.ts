import * as Stomp from '@stomp/stompjs';
import {autobind} from 'core-decorators';
import {Deferred} from 'jaasync/lib';
import {Service, Inject} from 'typedi';

import {Tokens} from '../../di/tokens';
import {User} from '../../user/interfaces';
import {MessagingService} from '../interfaces/MessagingService';

@Service(MessagingService.Token)
@autobind
export class MessagingServiceImpl implements MessagingService {

  @Inject(User.Token)
  private readonly user: User;
  @Inject(Tokens.STOMP_BROKER_BASE_URL)
  private readonly stompBrokerBaseUrl: string;
  private connectionInitiated: boolean = false;
  private readonly connected = new Deferred<Stomp.Client>();

  private get client(): Promise<Stomp.Client> {
    if(this.connectionInitiated) {
      return this.connected;
    }

    this.connectionInitiated = true;

    const client = new Stomp.Client({
      brokerURL: `${this.stompBrokerBaseUrl}/stomp`,
      connectHeaders: {
        authToken: this.user.token
      }
    });
    client.onConnect = (frame: Stomp.Frame): void => {
      this.connected.resolve(client);
    };
    client.onStompError = (frame: Stomp.Frame): void => {
      const err: string = frame.headers?.message;
      this.connected.reject(err);
    };
    client.activate();

    return this.connected;
  }

  public async subscribe<T>(topic: string, handler: (message: T) => void): Promise<{unsubscribe: () => void}> {
    const client = await this.client;
    const wrappedHandler = (msg: Stomp.Message) => {
      const json = msg.body;
      try {
        const payload = JSON.parse(json);
        handler(payload);
      } catch(e) {
        console.error(e);
      }
    };
    const headers = {};
    const sub = client.subscribe(topic, wrappedHandler, headers);
    return sub;
  }
}
