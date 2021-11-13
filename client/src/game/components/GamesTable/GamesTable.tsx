import {Box} from '@mui/material';
import {DataGrid, GridColDef, GridRenderCellParams} from '@mui/x-data-grid';
import {Typography} from '@mui/material';
import {autobind} from 'core-decorators';
import * as React from 'react';


import {Inject} from '../../../di';
import {GameData, GameStatus} from '../../interfaces';
import {PlayerService} from '../../../player/interfaces';
import {GameId, PlayerId} from '../../../types';
import {User} from '../../../user/User';
import {GameActionsCell} from './GameActionsCell';
import {PlayerCell} from './PlayerCell';
import {GameLinkCell} from './GameLinkCell';


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

    const rows = games.map(this.buildRow);
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
      winnerId: (gameData.winner),
      moveCount: gameData.moveCount,
      actions: gameData.gameId,
      createdAt: gameData.createdAt,
      updatedAt: gameData.updatedAt,
      gameStatus: gameData.status
    }
  }

  private static readonly columns: GridColDef[] = [{
    field: 'gameId',
    headerName: 'Game ID',
    renderCell: (params: GridRenderCellParams<GameId>) => (<GameLinkCell gameId={params.value}/>)
  }, {
    field: 'opponentId',
    headerName: 'Opponent',
    renderCell: (params: GridRenderCellParams<PlayerId>) => (<PlayerCell {...params}/>)
  }, {
    field: 'ownerId',
    headerName: 'Owner',
    renderCell: (params: GridRenderCellParams<PlayerId>) => (<PlayerCell {...params}/>)
  }, {
    field: 'actions',
    headerName: 'Actions',
    renderCell: (params: GridRenderCellParams<GameId>) => (<GameActionsCell {...params}/>)
  }, {
    field: 'moveCount',
    headerName: 'Move Count'
  }, {
    field: 'updatedAt',
    headerName: 'Last Updated'
  }, {
    field: 'createdAt',
    headerName: 'Date Created'
  }, {
    field: 'gameStatus',
    headerName: 'Status'
  }];
}

export namespace GamesTable {
  export interface State {
    games: GameData[];
    error: Error;
  }

  export interface Row {
    id: GameId;
    gameId: GameId;
    ownerId: PlayerId;
    opponentId: PlayerId;
    winnerId: PlayerId;
    moveCount: number;
    actions: GameId;
    createdAt: Date;
    updatedAt: Date;
    gameStatus: GameStatus;
  }
}