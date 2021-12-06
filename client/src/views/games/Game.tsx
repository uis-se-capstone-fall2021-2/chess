import {autobind} from 'core-decorators';
import {Alert} from '@mui/material';
import * as React from 'react';


import {ActiveGame} from '../../game/components/ActiveGame/ActiveGame';
import {CompletedGame} from '../../game/components/CompletedGame';
import {DeclinedGame} from '../../game/components/DeclinedGame';
import {GameProvider} from '../../game/components/GameProvider';
import {PendingGame} from '../../game/components/PendingGame';
import {GameData, GameId, GameState, GameStatus} from '../../game/interfaces';


@autobind
export class GameView extends React.Component<GameView.Props, GameView.State> {
  public override render(): React.ReactNode {
    return (
      <GameProvider gameId={this.props.gameId} errorRenderer={this.ErrorRenderer}>
        {(game: GameData) => {
          switch(game.status) {
            case GameStatus.ACTIVE:
              return <ActiveGame game={game}/>
            case GameStatus.PENDING:
              return <PendingGame gameInfo={game} />
            case GameStatus.DECLINED:
              return <DeclinedGame gameInfo={game} />
            default:
              return <CompletedGame game={game} />
          }
        }}
      </GameProvider>
    );
  }

  private ErrorRenderer(message: string) {
    return <Alert severity='error'>{message}</Alert>;
  }
}

export namespace GameView {
  export interface Props {
    gameId: GameId;
  }

  export interface State {}
}