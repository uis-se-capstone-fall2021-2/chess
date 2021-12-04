import {GridRenderCellParams} from '@mui/x-data-grid';
import * as React from 'react';

import {GameProvider} from '../../GameProvider';
import {GameData, GameId, GameStatus} from '../../../interfaces';
import {PlayerProvider} from '../../../../player/components/PlayerProvider';
import {Player} from '../../../../player/interfaces';

export function GameWinnerCell(props: GridRenderCellParams<GameId>): React.ReactElement {
  const {value: gameId} = props;
  return (
    <GameProvider gameId={gameId}>
      {(gameData: GameData) => {
        const {status, winner: winningPlayerId} = gameData;
        switch(status) {
          case GameStatus.COMPLETE:
          case GameStatus.TERMINATED:
            return (
              <PlayerProvider playerId={winningPlayerId}>
                {(player: Player) => (player?.displayName ?? winningPlayerId)}
              </PlayerProvider>
            );
          default:
            return <span>{'N/A'}</span>;
        }
      }}
    </GameProvider>
  );
}





