import {autobind} from 'core-decorators';
import * as React from 'react';

import {GameState} from '../../interfaces';

@autobind
export class GameBoard extends React.Component<GameBoard.Props, GameBoard.State> {
  public override render(): React.ReactNode {
    return 'Game board here';
  }
}

export namespace GameBoard {
  export interface Props {
    gameState: GameState;
  }

  export interface State {}
}

