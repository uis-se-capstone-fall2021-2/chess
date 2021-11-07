import {Box, Tabs, Tab, Typography} from '@mui/material';
import {DataGrid, GridColDef} from '@mui/x-data-grid';
import {autobind} from 'core-decorators';
import * as React from 'react';
import {__RouterContext as RouterContext} from 'react-router';
import {
  HashRouter as Router,
  Switch,
  Route,
  Link
} from 'react-router-dom';

import {Inject} from '../../di';
import {GameInfo} from '../../game/interfaces';
import {PlayerService} from '../../player/interfaces';
import {User} from '../../user/User';

@autobind
export class MyGames extends React.Component<{}, MyGames.State> {

  public override state: MyGames.State = {
    games: null,
    error: null
  };

  @Inject(PlayerService.Token)
  private readonly playerService: PlayerService;
  @Inject(User.Token)
  private readonly user: User;

  public override async componentDidMount(): Promise<void> {
    try {
      const games = await this.playerService.getOwnActiveGames();
      this.setState({games});
    } catch(e) {
      this.setState({error: e as Error});
    }
  }

  public override render(): React.ReactNode {
    const {games, error} = this.state;
    if(error) {
      return (
        <>
          <Typography component='h3' sx={{color: 'text.error'}}>Error Loading Games</Typography>
          <Typography component='p'>{error.toString()}</Typography>
        </>
      );
    } else if(games == null) {
      return <Typography component='h3' sx={{color: 'text.error'}}>Loading Games</Typography>;
    // } else if(games.length === 0) {
    //   return <Typography component='h3' sx={{color: 'text.error'}}>No Active Games</Typography>;
    } else {
      return (
        <RouterContext.Consumer>
          {(ctx) => (
            <>
              <Tabs value={ctx.location.pathname}>
                <Tab label="Active Games" value="/games/active" component={Link} to="/games/active"/>
                <Tab label="Pending Games" value="/games/pending" component={Link} to="/games/pending"/>
                <Tab
                  label="Game History"
                  value="/games/history"
                  component={Link}
                  to='/games/history'
                />
              </Tabs>
              <Switch>
                <Route path='/games/active' render={() => this.renderGrid(games)} />
                <Route path='/games/pending' render={() => <div>Pending Games</div>} />
                <Route path='/games/history' render={() => <div>History</div>} />
              </Switch>
            </>
          )}
        </RouterContext.Consumer>
      );
    }
  }

  private renderGrid(games: GameInfo[]): React.ReactNode {
    const rows = games.map(this.buildRow);
    return (
      <Box sx={{
        display: 'flex',
        height: '100%'
      }}>
        <DataGrid rows={rows} columns={MyGames.columns}/>
      </Box>
    );
  }

  private buildRow(gameInfo: GameInfo): MyGames.Row {
    return {
      id: gameInfo.gameId,
      gameId: gameInfo.gameId,
      ownerId: gameInfo.owner,
      opponentId: (gameInfo.players[0] === this.user.playerId) ? gameInfo.players[1] : gameInfo.players[0],
      moveCount: gameInfo.moveCount
    }
  }

  private static readonly columns: GridColDef[] = [{
    field: 'gameId',
    headerName: 'Game ID'
  }, {
    field: 'ownerId',
    headerName: 'Owner ID'
  }, {
    field: 'opponentId',
    headerName: 'Opponent ID'
  }, {
    field: 'moveCount',
    headerName: 'Move Count'
  }];
}

export namespace MyGames {
  export interface State {
    games: GameInfo[]|null;
    error: Error|null;
  }

  export interface Row {
    id: number;
    gameId: number;
    ownerId: number;
    opponentId: number;
    moveCount: number;
  }
}

