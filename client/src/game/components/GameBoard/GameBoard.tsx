import {Alert} from '@mui/material';
import {autobind} from 'core-decorators';
// @ts-ignore
import {Chess} from 'chess.ts';
import {isEqual} from 'lodash';
import * as React from 'react';
import {Chessboard} from 'react-chessboard';

import {ChessboardLib, MoveIntent, Rank, ChessPiece} from '../../../board/interfaces';
import {BoardUtils} from '../../../board/BoardUtils';
import {Inject} from '../../../di';
import {GameService, GameState} from '../../interfaces';
import {RectContext} from '../../../utils/layout/RectContext';
import {User} from '../../../user/interfaces';
import {GameLifecycleProvider} from '../GameLifecyleProvider';


@autobind
export class GameBoard extends React.Component<GameBoard.Props, GameBoard.State> {

  @Inject(GameService.Token)
  private readonly gameService: GameService;
  @Inject(User.Token)
  private readonly user: User;

  public override state: GameBoard.State = {
    error: null,
    pendingGameState: null
  };

  public override componentDidMount() {
    (async () => {
      try {
        await this.gameService.fetchGameState(this.props.gameState.gameId);
      } catch(ignore) {}
    })();
  }

  public override render(): React.ReactNode {
    const {gameId, players} = this.props.gameState;

    return (
      <RectContext.Consumer>
        {(dimensions: RectContext.Dimensions) => {
          const width = this.getBoardWidth(dimensions);
          return (
            <GameLifecycleProvider gameState={this.props.gameState}>
              {({isUsersTurn, isUserInCheck}) => (
                <>
                  <this.Banner width={width} isUsersTurn={isUsersTurn} isUserInCheck={isUserInCheck}/>
                  <Chessboard
                    id={gameId}
                    boardWidth={width}
                    position={this.getFenString()}
                    onPieceDrop={this.handleMoveSync}
                    boardOrientation={this.user.playerId === players[0] ? 'white' : 'black'}
                    arePiecesDraggable={isUsersTurn}
                  />
                </>
              )}
            </GameLifecycleProvider>
          );
        }}
      </RectContext.Consumer>
    );
  }

  private getFenString(): string {
    const {board, moveCount} = this.props.gameState;
    const {pendingGameState} = this.state;

    const [viewBoard, viewMoveCount] = pendingGameState
      ? [pendingGameState.board, pendingGameState.moveCount]
      : [board, moveCount];

    return BoardUtils.getFenString(viewBoard, viewMoveCount)
  }

  private Banner(params: {isUserInCheck: boolean, isUsersTurn: boolean, width: number}): React.ReactElement {
    const {error} = this.state;
    const {width, isUsersTurn, isUserInCheck} = params;
    return (
      error
        ? <Alert sx={{width, marginBottom: 1}} severity='error'>{error.message}</Alert>
        : isUserInCheck
          ? <Alert sx={{width, marginBottom: 1}} severity='warning'>Your King is in Check!</Alert>
          : isUsersTurn
            ? <Alert sx={{width, marginBottom: 1}} severity='success'>It's Your Turn</Alert>
            : <Alert sx={{width, marginBottom: 1}} severity='info'>Waiting for opponent...</Alert>
    );
  }

  public override componentDidUpdate(prevProps: GameBoard.Props): void {
    if(!isEqual(prevProps.gameState.board, this.props.gameState.board)) {
      this.setState({
        pendingGameState: null
      });
    }
  }

  private getBoardWidth({height, width}: RectContext.Dimensions): number {
    if(height >= 560 && width >= 560) {
      return 560 - 20;
    } else if(height > width) {
      return Math.max(100, width - 20);
    } else {
      return Math.max(100, height - 20);
    }
  }


  

  private handleMoveSync(source: ChessboardLib.Square , target: ChessboardLib.Square, piece: ChessboardLib.Piece): boolean {

    const moveIntent: MoveIntent = {
      chessPiece: BoardUtils.toChessPiece(piece),
      from: BoardUtils.toPosition(source),
      to: BoardUtils.toPosition(target)
    };

    if(this.validateMove(moveIntent, source, target)) {
      const pendingGameState: GameState = {
        ...this.props.gameState,
        moveCount: this.props.gameState.moveCount + 1,
        playerInCheck: null, 
        board: this.optimisticallyCalculateNextBoard(moveIntent)
      };
  
      this.setState({
        error: null,
        pendingGameState
      });
      this.handleMove(moveIntent);
    } else {
      this.setState({
        error: new Error('Illegal Move')
      });
    }

    return true;
  }

  private async handleMove(moveIntent: MoveIntent): Promise<void> {
    const {gameId} = this.props.gameState;
    try {
      const nextState: GameState = await this.gameService.move(gameId, moveIntent);
      this.setState({
        pendingGameState: nextState
      });
    } catch(e) {
      this.setState({
        error: e as Error,
        pendingGameState: null
      });
    }
  }


  private validateMove(moveIntent: MoveIntent, source: ChessboardLib.Square , target: ChessboardLib.Square): boolean {
    const temp = new Chess();
    temp.load(BoardUtils.getFenString(this.props.gameState.board, this.props.gameState.moveCount));
    if(
      (moveIntent.chessPiece == ChessPiece.PAWN) &&
      (moveIntent.to.rank == Rank._1 || moveIntent.to.rank == Rank._8)){
        if(temp.move({from: source, to: target, promotion: 'q'})){
          return true;
        }
    } else if(temp.move({from: source, to: target})) {
      return true;
    }
    return false;
  }

  private optimisticallyCalculateNextBoard(moveIntent: MoveIntent): number[] {
    const board: number[] = [...this.props.gameState.board];
    const fromIndex: number = BoardUtils.getRankIntValue(moveIntent.from.rank) * 8 + BoardUtils.getFileIntValue(moveIntent.from.file);
    const toIndex: number = BoardUtils.getRankIntValue(moveIntent.to.rank) * 8 + BoardUtils.getFileIntValue(moveIntent.to.file);
    const piece: number = BoardUtils.getPieceIntValueFromPosition(board, moveIntent.from);

    board[fromIndex] = 0;
    board[toIndex] = piece;

    return board;
  }

  
}

export namespace GameBoard {
  export interface Props {
    gameState: GameState;
  }

  export interface State {
    pendingGameState: GameState;
    error: Error;
  }
}

