import {autobind} from 'core-decorators';
import * as React from 'react';
import {Route} from 'react-router-dom';

import {CreateGame} from '../../game/components/CreateGame';
import {GameState} from '../../game/interfaces';

@autobind
export class Home extends React.Component<Home.Props, Home.State> {

  public override render(): React.ReactNode {
    return (
      <Route>
        {({history}) => (
          <CreateGame onNewGame={(game: GameState) => history.push(`/games/${game.gameId}`)} />
        )}
      </Route>
    );
  }
}

export namespace Home {
  export interface Props {}
  export interface State {}
}