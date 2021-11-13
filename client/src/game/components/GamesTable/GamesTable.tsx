import {Box} from '@mui/material';
import {DataGrid, GridColDef, GridRenderCellParams} from '@mui/x-data-grid';
import {Typography} from '@mui/material';
import {autobind} from 'core-decorators';
import * as React from 'react';


import {Inject} from '../../../di';
import {GameData} from '../../interfaces';
import {PlayerService} from '../../../player/interfaces';
import {PlayerId} from '../../../types';
import {User} from '../../../user/User';
import {PlayerCell} from './PlayerCell';

@autobind
export abstract class GamesTable extends React.Component<{}, GamesTable.State> {

  @Inject(User.Token)
  private readonly user: User;
  @Inject(PlayerService.Token)
  protected readonly playerService: PlayerService;

  protected abstract loadGames(): Promise<GameData[]>;

  public override state: GamesTable.State = {
    games: [],
    error: null
  };

  public override async componentDidMount(): Promise<void> {
    try {
      const games = await this.loadGames();
      this.setState({games});
    } catch(e) {
      this.setState({error: e as Error});
    }
  }

  

  public override render(): React.ReactNode {
    const {games, error} = this.state;
    if(error) {
      return (
        <Typography sx={{color: 'text.error'}}>
          {error.message}
        </Typography>
      );
    }

    const rows = this.state.games.map(this.buildRow);
    return (
      <Box sx={{
        display: 'flex',
        height: '100%'
      }}>
        <DataGrid rows={rows} columns={GamesTable.columns}/>
      </Box>
    );
  }

  private buildRow(gameData: GameData): GamesTable.Row {
    return {
      id: gameData.gameId,
      gameId: gameData.gameId,
      ownerId: gameData.owner,
      opponentId: (gameData.players[0] === this.user.playerId) ? gameData.players[1] : gameData.players[0],
      moveCount: gameData.moveCount
    }
  }

  private static readonly columns: GridColDef[] = [{
    field: 'gameId',
    headerName: 'Game ID'
  }, {
    field: 'ownerId',
    headerName: 'Owner ID',
    renderCell: (params: GridRenderCellParams<PlayerId>) => (<PlayerCell {...params}/>)
  }, {
    field: 'opponentId',
    headerName: 'Opponent ID',
    renderCell: (params: GridRenderCellParams<PlayerId>) => (<PlayerCell {...params}/>)
  }, {
    field: 'moveCount',
    headerName: 'Move Count'
  }];
}

export namespace GamesTable {
  export interface State {
    games: GameData[];
    error: Error;
  }

  export interface Row {
    id: number;
    gameId: number;
    ownerId: number;
    opponentId: number;
    moveCount: number;
  }
}