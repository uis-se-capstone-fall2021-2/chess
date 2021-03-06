
import * as React from 'react';
import {Button, Tooltip} from '@mui/material';
import {
  CheckCircle as CheckCircleIcon,
  HighlightOff as HighlightOffIcon
} from '@mui/icons-material';

import {User} from '../../../../user/interfaces';
import {GameData, GameId, GameService, GameStatus} from '../../../interfaces';


export function PendingGameActionsCell (props: PendingGameActionsCell.Props): React.ReactElement {
  const {game, gameService, user, onGameAccepted} = props;

  const {gameId, owner, status} = game;

  if(status !== GameStatus.PENDING) {
    return null;
  }

  if(owner === user.playerId) {
    const cancelInvitation = () => {
      if(window.confirm('Cancel Invitation?')) {
        gameService.cancelGameInvite(gameId);
      }
    };

    return (
      <Tooltip title='Cancel Invitation'>
        <Button onClick={cancelInvitation} color='primary'>
          <HighlightOffIcon/>
        </Button>
      </Tooltip>
    );
  } else {
    const acceptGameInvite = async () => {
      await gameService.acceptGameInvite(gameId);
      onGameAccepted(gameId);
    };
    const declineGameInvite = () => gameService.declineGameInvite(gameId);

    return (
      <>
        <Tooltip title='Accept Invite'>
          <Button onClick={acceptGameInvite}>
            <CheckCircleIcon/>
          </Button>
        </Tooltip>
        <Tooltip title='Decline Invite'>
          <Button onClick={declineGameInvite}>
            <HighlightOffIcon/>
          </Button>
        </Tooltip>
      </>
    );
  }
}

namespace PendingGameActionsCell {
  export interface Props {
    game: GameData;
    user: User;
    gameService: GameService;
    onGameAccepted: (gameId: GameId) => void;
  }
}