import {autobind} from 'core-decorators';
import * as React from 'react';

import {Inject} from '../../../di';
import {GameService, GameState} from '../../interfaces';

@autobind
export class GameBoard extends React.Component<GameBoard.Props, GameBoard.State> {

  @Inject(GameService.Token)
  private readonly gameService: GameService;

  public override render(): React.ReactNode {
    return (
      <pre>
        {JSON.stringify(this.props.gameState, null, 2)}
      </pre>
    );
  }
}

export namespace GameBoard {
  export interface Props {
    gameState: GameState;
  }

  export interface State {}
}

