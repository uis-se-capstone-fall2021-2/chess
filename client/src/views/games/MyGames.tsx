import {Box, Typography} from '@mui/material';
import {DataGrid, GridColDef} from '@mui/x-data-grid';
import {autobind} from 'core-decorators';
import * as React from 'react';

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
    } else if(games.length === 0) {
      return <Typography component='h3' sx={{color: 'text.error'}}>No Active Games</Typography>;
    } else {
      return this.renderGrid(games); 
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

