import {Chessboard} from 'react-chessboard';
import {autobind} from 'core-decorators';
import * as React from 'react';

import {Inject} from '../../../di';
import {GameService, GameState} from '../../interfaces';

@autobind
export class GameBoard extends React.Component<GameBoard.Props, GameBoard.State> {

  @Inject(GameService.Token)
  private readonly gameService: GameService;

  public override render(): React.ReactNode {
    const {gameState} = this.props;
    return (
      <Chessboard id={gameState.gameId}/>
    );
  }
}

export namespace GameBoard {
  export interface Props {
    gameState: GameState;
  }

  export interface State {}
}

