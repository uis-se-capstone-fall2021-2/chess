import {Alert, Tooltip} from '@mui/material';
import {
  AccessTime as AccessTimeIcon,
  Celebration as CelebrationIcon,
  HourglassTop as HourglassTopIcon,
  Input as InputIcon,
  ErrorOutline as ErrorOutlineIcon,
  SentimentDissatisfied as SentimentDissatisfiedIcon
} from '@mui/icons-material';
import * as React from 'react';
import {Link} from 'react-router-dom';

import {GameProvider} from '../../GameProvider';
import {GameData, GameId, GameState, GameStatus} from '../../../interfaces';
import {GameLifecycleProvider} from '../../GameLifecyleProvider';


export function GameStatusCell(props: GameStatusCell.Props): React.ReactElement {
  const {gameId} = props;
  const link = (children: any) => (<Link to={`/games/${props.gameId}`}>{children}</Link>);
  return (
    <GameProvider gameId={gameId}>
      {(gameData: GameData) => {
        switch(gameData.status) {
          case GameStatus.ACTIVE:
            return (
              <GameLifecycleProvider gameState={gameData as GameState}>
                {({isUsersTurn, isUserInCheck}) => (
                  isUserInCheck
                    ? <Tooltip title='In Check!'>
                        {link(<ErrorOutlineIcon color='warning'/>)}
                      </Tooltip>
                    : isUsersTurn
                      ? <Tooltip title='Your Move'>
                          {link(<InputIcon color='success'/>)}
                        </Tooltip>
                      : <Tooltip title="Opponent's turn">
                          {link(<AccessTimeIcon color='info'/>)}
                        </Tooltip>
                )}
              </GameLifecycleProvider>
            );
          case GameStatus.PENDING:
            return <Tooltip title='Awaiting Player'><HourglassTopIcon/></Tooltip>;
          case GameStatus.COMPLETE:
          case GameStatus.TERMINATED:
            return (
              <GameLifecycleProvider gameState={gameData as GameState}>
                {({userIsWinner}) => (
                  userIsWinner
                    ? <Tooltip title='You Won!'>
                        <CelebrationIcon/>
                      </Tooltip>
                    : <Tooltip title='Opponent Won'>
                        <SentimentDissatisfiedIcon/>
                      </Tooltip>
                )}
              </GameLifecycleProvider>
            );
          default:
            return <span>{gameData.status}</span>;
        }
      }}
    </GameProvider>
  );
}

export namespace GameStatusCell {
  export interface Props {
    gameId: GameId;
  }
}