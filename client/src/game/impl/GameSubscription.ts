import {autobind} from 'core-decorators';
import * as StrongBus from 'strongbus';

import {MessagingService} from '../../messaging/interfaces';
import {GameId} from '../interfaces';

@autobind
export class GameSubscription {
  private readonly bus = new StrongBus.Bus<GameSubscription.Events>();
  private readonly messagingService: MessagingService;
  private readonly gameId: GameId;
  private readonly onGameNotification: () => void;
  private subscription: () => void;
  
  constructor(params: GameSubscription.Params) {
    this.messagingService = params.messagingService;
    this.gameId = params.gameId;
    this.onGameNotification = params.onGameNotification;
    this.bus.hook('active', this.activateSubscription);
    this.bus.hook('idle', this.deactivateSubscription);
  }

  public subscribe(): StrongBus.Subscription {
    return this.bus.on('update', () => void(0));
  }

  private async activateSubscription() {
    const subscribed = await this.messagingService.subscribe(
      `/games/${this.gameId}`,
      this.onGameNotification
    );
    this.subscription = () => subscribed.unsubscribe();
  }

  private deactivateSubscription() {
    this.subscription?.();
  }
}

export namespace GameSubscription {
  export interface Params {
    messagingService: MessagingService;
    gameId: GameId;
    onGameNotification: () => void;
  }

  export interface Events {
    update: void;
  }
}