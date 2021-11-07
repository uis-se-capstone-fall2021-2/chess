import {autobind} from 'core-decorators';
import * as React from 'react';

import {GameState} from '../../interfaces';
import {GameBoard} from '../GameBoard';

@autobind
export class ActiveGame extends React.Component<ActiveGame.Props, ActiveGame.State> {
  public override render(): React.ReactNode {
    return (
      <GameBoard gameState={this.props.gameState}/>
    );
  }
}

export namespace ActiveGame {
  export interface Props {
    gameState: GameState;
  }

  export interface State {}
}