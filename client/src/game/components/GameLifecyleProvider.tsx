import {autobind} from 'core-decorators';
import * as React from 'react';

import {Inject} from '../../di';
import {User} from '../../user/interfaces';
import {GameState} from '../interfaces';

@autobind
export class GameLifecycleProvider extends React.Component<GameLifecycleProvider.Props> {
  @Inject(User.Token)
  private readonly user: User;

  public override render(): React.ReactNode {
    const {players, playerInCheck, moveCount, winner} = this.props.gameState;
    const isUsersTurn = moveCount % 2 === players.indexOf(this.user.playerId);
    const isUserInCheck = playerInCheck === this.user.playerId;
    const userIsWinner = winner === this.user.playerId;

    return this.props.children({isUserInCheck, isUsersTurn, userIsWinner});
  }
}

export namespace GameLifecycleProvider {
  export interface Props {
    gameState: GameState;
    children: (props: ChildProps) => React.ReactNode;
  }

  export interface ChildProps {
    isUsersTurn: boolean;
    isUserInCheck: boolean;
    userIsWinner: boolean;
  }
}