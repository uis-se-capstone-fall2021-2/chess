import {GridRenderCellParams} from '@mui/x-data-grid';
import {autobind} from 'core-decorators';
import {Button, Tooltip} from '@mui/material';
import {
  PersonRemoveAlt1 as QuitIcon
} from '@mui/icons-material';
import * as React from 'react';

import {Inject} from '../../../di';
import {GameId} from '../../../types';
import {GameProvider} from '../GameProvider';
import {GameData, GameService, GameState, GameStatus} from '../../interfaces';
import {User} from '../../../user/interfaces';



@autobind
export class GameActionsCell extends React.Component<GridRenderCellParams<GameId>, {}> {
  @Inject(User.Token)
  private readonly user: User;
  @Inject(GameService.Token)
  private readonly gameService: GameService;

  public override render() {
    const {value: gameId} = this.props;
    return (
      <GameProvider gameId={gameId}>
        {(gameData: GameData) => {
          const {status} = gameData;
          switch(status) {
            case GameStatus.ACTIVE:
              return <ActiveGameCell game={gameData as GameState} gameService={this.gameService}/>;
            case GameStatus.PENDING:
            case GameStatus.DECLINED:

            case GameStatus.COMPLETE:
            case GameStatus.TERMINATED:

            default:
              return <span/>
          }
        }}
      </GameProvider>
    );
  }
}

function ActiveGameCell (props: {game: GameState, gameService: GameService}): React.ReactElement {
  const {game, gameService} = props;
  
  const quitGame = () => {
    if(window.confirm('Quit Game?')) {
      gameService.quitGame(game.gameId);
    }
  };

  return (
    <Tooltip title='Quit Game'>
      <Button onClick={quitGame} color='primary'>
        <QuitIcon/>
      </Button>
    </Tooltip>
    
  );
}