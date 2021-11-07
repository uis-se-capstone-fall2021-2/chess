import {Tabs, Tab} from '@mui/material';
import {autobind} from 'core-decorators';
import * as React from 'react';
import {__RouterContext as RouterContext} from 'react-router';
import {
  Switch,
  Route,
  Link
} from 'react-router-dom';
import {ActiveGamesTable} from '../../game/components/ActiveGames';
import {GameHistoryTable} from '../../game/components/GameHistory';
import {PendingGamesTable} from '../../game/components/PendingGames';


@autobind
export class MyGames extends React.Component {

  public override render(): React.ReactNode {
    return (
      <RouterContext.Consumer>
        {(ctx) => (
          <>
            <Tabs value={ctx.location.pathname}>
              <Tab label="Active Games" value="/games/active" component={Link} to="/games/active"/>
              <Tab label="Pending Games" value="/games/pending" component={Link} to="/games/pending"/>
              <Tab label="Game History" value="/games/history" component={Link} to='/games/history'/>
            </Tabs>
            <Switch>
              <Route path='/games/active'>
                <ActiveGamesTable/>
              </Route>
              <Route path='/games/pending'>
                <PendingGamesTable/>
              </Route>
              <Route path='/games/history'>
                <GameHistoryTable/>
              </Route>
            </Switch>
          </>
        )}
      </RouterContext.Consumer>
    );
  }
}
  

