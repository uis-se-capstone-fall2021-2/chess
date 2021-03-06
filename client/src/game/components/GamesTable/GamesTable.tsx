import {Box} from '@mui/material';
import {DataGrid, GridColDef, GridRenderCellParams} from '@mui/x-data-grid';
import {Typography} from '@mui/material';
import {autobind} from 'core-decorators';
import * as React from 'react';


import {Inject} from '../../../di';
import {GameData, GameId} from '../../interfaces';
import {PlayerColor, PlayerId, PlayerService} from '../../../player/interfaces';
import {User} from '../../../user/interfaces';
import {GameActionsCell} from './cells/GameActionsCell';
import {PlayerCell} from './cells/PlayerCell';
import {GameLinkCell} from './cells/GameLinkCell';
import {DateCell} from './cells/DateCell';
import {GameWinnerCell} from './cells/GameWinnerCell';
import {GameStatusCell} from './cells/GameStatusCell';


@autobind
export abstract class GamesTable extends React.Component<{}, GamesTable.State> {

  @Inject(User.Token)
  private readonly user: User;
  @Inject(PlayerService.Token)
  protected readonly playerService: PlayerService;

  protected abstract loadGames(): Promise<GameData[]>;
  // protected abstract columns: GridColDef[];

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
        <Box sx={{
          height: 'calc(100% - 50px)',
          flex: '1 0'
        }}>
          <DataGrid
            rows={rows}
            columns={GamesTable.columns}
            density='compact'/>
        </Box>
      </Box>
    );
  }

  private buildRow(gameData: GameData): GamesTable.Row {
    return {
      id: gameData.gameId,
      gameId: gameData.gameId,
      ownerId: gameData.owner,
      opponentId: (gameData.players[0] === this.user.playerId) ? gameData.players[1] : gameData.players[0],
      winner: gameData.gameId,
      playerColor: (gameData.players[0] === this.user.playerId) ? PlayerColor.WHITE : PlayerColor.BLACK,
      moveCount: gameData.moveCount,
      actions: gameData.gameId,
      createdAt: gameData.createdAt,
      updatedAt: gameData.updatedAt,
      completedAt: gameData.completedAt,
      gameStatus: gameData.gameId
    }
  }

  private static readonly columns: GridColDef[] = [{
    field: 'gameId',
    headerName: 'Game ID',
    headerAlign: 'center',
    align: 'center',
    renderCell: (params: GridRenderCellParams<GameId>) => (<GameLinkCell gameId={params.value}/>)
  }, {
    field: 'opponentId',
    headerName: 'Opponent',
    headerAlign: 'center',
    align: 'center',
    renderCell: (params: GridRenderCellParams<PlayerId>) => (<PlayerCell {...params}/>)
  }, {
    field: 'gameStatus',
    headerName: 'Status',
    headerAlign: 'center',
    align: 'center',
    renderCell: (params: GridRenderCellParams<GameId>) => (<GameStatusCell gameId={params.value}/>)
  }, {
    field: 'ownerId',
    headerName: 'Owner',
    headerAlign: 'center',
    align: 'center',
    renderCell: (params: GridRenderCellParams<PlayerId>) => (<PlayerCell {...params}/>)
  }, {
    field: 'actions',
    headerName: 'Actions',
    headerAlign: 'center',
    align: 'center',
    width: 150,
    renderCell: (params: GridRenderCellParams<GameId>) => (<GameActionsCell {...params}/>)
  }, {
    field: 'moveCount',
    headerName: 'Move Count',
    headerAlign: 'center',
    align: 'right'
  }, {
    field: 'playerColor',
    headerName: 'Player Color',
    headerAlign: 'center',
    align: 'center'
  }, {
    field: 'winner',
    headerName: 'Winner',
    headerAlign: 'center',
    align: 'center',
    renderCell: (params: GridRenderCellParams<GameId>) => (<GameWinnerCell {...params}/>)
  },{
    field: 'updatedAt',
    width: 250,
    headerName: 'Last Updated',
    headerAlign: 'center',
    align: 'center',
    renderCell: DateCell
  }, {
    field: 'createdAt',
    width: 250,
    headerName: 'Date Created',
    headerAlign: 'center',
    align: 'center',
    renderCell: DateCell
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
    winner: GameId;
    playerColor: PlayerColor;
    moveCount: number;
    actions: GameId;
    createdAt: Date;
    updatedAt: Date;
    completedAt: Date;
    gameStatus: GameId;
  }
}