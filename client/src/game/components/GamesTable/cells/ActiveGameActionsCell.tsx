
import {Button, Tooltip} from '@mui/material';
import {
  PersonRemoveAlt1 as QuitIcon
} from '@mui/icons-material';
import * as React from 'react';

import {GameService, GameState} from '../../../interfaces';


export function ActiveGameActionsCell (props: ActiveGameActionsCell.Props): React.ReactElement {
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

export namespace ActiveGameActionsCell {
  export interface Props {
    game: GameState,
    gameService: GameService
  }
}