import {Button, Tooltip} from '@mui/material';
import {
  Download as DownloadIcon
} from '@mui/icons-material';
import * as React from 'react';


import {GameService, GameId} from '../../../interfaces';

export function DownloadGame(props: {gameService: GameService, gameId: GameId}): React.ReactElement {
  const download = () => props.gameService.download(props.gameId);
  return (
    <Tooltip title='Download PGN'>
      <Button onClick={download} color='primary'>
        <DownloadIcon/>
      </Button>
    </Tooltip>
  );
}