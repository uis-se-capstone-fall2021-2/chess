import {IconButton, Tooltip} from '@mui/material';
import {Launch as LaunchIcon} from '@mui/icons-material';
import * as React from 'react';
import {Link} from 'react-router-dom';

import {GameId} from '../../../interfaces';

export function GameLinkCell(props: {gameId: GameId}): React.ReactElement {
  return (
    <Tooltip title='Go to game'>
      <Link to={`/games/${props.gameId}`}>
        <IconButton>
          <LaunchIcon/>
        </IconButton>
        <span>{props.gameId}</span>
      </Link>
    </Tooltip>
  );
}