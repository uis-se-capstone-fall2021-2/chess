import * as React from 'react';

import {GameService, GameInfo} from '../../../interfaces';
import {DownloadGame} from './DownloadGame';


export function CompletedGameActionsCell (props: CompletedGameActionsCell.Props): React.ReactElement {
  const {game, gameService} = props;

  return (
    <>
      <DownloadGame gameId={game.gameId} gameService={gameService}/>
    </>
  );
}

export namespace CompletedGameActionsCell {
  export interface Props {
    game: GameInfo,
    gameService: GameService
  }
}