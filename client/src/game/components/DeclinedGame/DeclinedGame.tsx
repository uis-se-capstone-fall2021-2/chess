import {autobind} from 'core-decorators';
import * as React from 'react';

import {GameInfo} from '../../interfaces';

@autobind
export class DeclinedGame extends React.Component<DeclinedGame.Props, DeclinedGame.State> {
  public override render(): React.ReactNode {
    return 'Declined Game here';
  }
}

export namespace DeclinedGame {
  export interface Props {
    gameInfo: GameInfo;
  }

  export interface State {}
}