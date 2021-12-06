import {autobind} from 'core-decorators';
import * as React from 'react';

import {GameData} from '../../interfaces';
import {GameBoard} from '../GameBoard';

@autobind
export class ActiveGame extends React.Component<ActiveGame.Props, ActiveGame.State> {
  public override render(): React.ReactNode {
    return (
      <GameBoard game={this.props.game}/>
    );
  }
}

export namespace ActiveGame {
  export interface Props {
    game: GameData;
  }

  export interface State {}
}