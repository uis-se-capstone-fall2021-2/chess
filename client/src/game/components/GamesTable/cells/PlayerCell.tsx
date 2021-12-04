import * as React from 'react';
import {GridRenderCellParams} from '@mui/x-data-grid';

import {Player, PlayerId} from '../../../../player/interfaces';
import {PlayerProvider} from '../../../../player/components/PlayerProvider';


export function PlayerCell(props: GridRenderCellParams<PlayerId>): React.ReactElement {
  const {value: playerId} = props;
  return (
    <PlayerProvider playerId={playerId}>
      {(player: Player) => (player?.displayName ?? playerId)}
    </PlayerProvider>
  );
}