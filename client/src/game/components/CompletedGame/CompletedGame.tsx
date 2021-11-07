import {autobind} from 'core-decorators';
import * as React from 'react';

import {GameInfo} from '../../interfaces';

@autobind
export class CompletedGame extends React.Component<CompletedGame.Props, CompletedGame.State> {
  public override render(): React.ReactNode {
    return 'Completed Game here';
  }
}

export namespace CompletedGame {
  export interface Props {
    gameInfo: GameInfo;
  }

  export interface State {}
}