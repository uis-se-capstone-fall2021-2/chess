import {autobind} from 'core-decorators';
import * as React from 'react';

import {GameInfo} from '../../interfaces';

@autobind
export class PendingGame extends React.Component<PendingGame.Props, PendingGame.State> {
  public override render(): React.ReactNode {
    return 'Pending Game here';
  }
}

export namespace PendingGame {
  export interface Props {
    gameInfo: GameInfo;
  }

  export interface State {}
}