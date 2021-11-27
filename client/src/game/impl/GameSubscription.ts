import {autobind} from 'core-decorators';
import * as StrongBus from 'strongbus';

import {MessagingService} from '../../messaging/interfaces';
import {User} from '../../user/interfaces';
import {GameId} from '../interfaces';

@autobind
export class GameSubscription {
  private readonly bus = new StrongBus.Bus<GameSubscription.Events>();
  private readonly user: User;
  private readonly messagingService: MessagingService;
  private readonly gameId: GameId;
  private readonly onGameUpdated: () => void;
  private readonly onGameDeleted: () => void;
  private subscription: () => void;
  
  constructor(params: GameSubscription.Params) {
    this.messagingService = params.messagingService;
    this.gameId = params.gameId;
    this.user = params.user;
    this.onGameUpdated = params.onGameUpdated;
    this.bus.hook('active', this.activateSubscription);
    this.bus.hook('idle', this.deactivateSubscription);
  }

  public subscribe(): StrongBus.Subscription {
    return this.bus.on('update', () => void(0));
  }

  private async activateSubscription() {
    const subs = await Promise.all([
      this.messagingService.subscribe(
        `/users/${this.user.userId}/games/${this.gameId}/update`,
        this.onGameUpdated
      ),
      this.messagingService.subscribe(
        `/games/${this.gameId}/update`,
        this.onGameUpdated
      ),
      this.messagingService.subscribe(
        `/games/${this.gameId}/delete`,
        this.onGameDeleted
      )
    ]);
    this.subscription = () => subs.forEach(s => s.unsubscribe());
  }

  private deactivateSubscription() {
    this.subscription?.();
  }
}

export namespace GameSubscription {
  export interface Params {
    user: User;
    messagingService: MessagingService;
    gameId: GameId;
    onGameUpdated: () => void;
    onGameDeleted: () => void;
  }

  export interface Events {
    update: void;
  }
}