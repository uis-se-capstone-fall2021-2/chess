import {Alert} from '@mui/material';
import {autobind} from 'core-decorators';

import {isEqual} from 'lodash';
import * as React from 'react';
import {Chessboard} from 'react-chessboard';

import {ChessboardLib, MoveIntent} from '../../../board/interfaces';
import {BoardUtils} from '../../../board/BoardUtils';
import {Inject} from '../../../di';
import {GameService, GameData, GameState} from '../../interfaces';
import {RectContext} from '../../../utils/layout/RectContext';
import {User} from '../../../user/interfaces';
import {GameLifecycleProvider} from '../GameLifecyleProvider';
import {PlayerProvider} from '../../../player/components/PlayerProvider';

import './styles.css';

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

  public override async componentDidMount() {
    try {
      await this.gameService.fetchGameState(this.props.game.gameId);
    } catch(ignore) {}
  }

  private Opponent(): React.ReactElement {
    const opponentId = this.props.game.players.find(playerId => playerId !== this.user.playerId);
    return (
      <PlayerProvider playerId={opponentId}>
        {(player) =>  player?.displayName || `Player ${opponentId}`}
      </PlayerProvider>
    );
  }

  public override render(): React.ReactNode {
    const game = this.state.pendingGameState ?? this.props.game
    const {gameId, players, moveCount, updatedAt} = game;

    return (
      <RectContext.Consumer>
        {(dimensions: RectContext.Dimensions) => {
          const width = BoardUtils.getBoardWidth(dimensions);
          return (
            <GameLifecycleProvider game={game}>
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
                  <div style={{width}} className='active-game-details'>
                    <span>{'Opponent: '}<this.Opponent/></span>
                    <span>{`Move Count: ${moveCount}`}</span>
                    <span>{`Last Move: ${updatedAt.toLocaleString()}`}</span>
                  </div>
                </>
              )}
            </GameLifecycleProvider>
          );
        }}
      </RectContext.Consumer>
    );
  }

  private getFenString(): string {
    const {board, moveCount} = this.props.game;
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
    if(!isEqual(prevProps.game.board, this.props.game.board)) {
      this.setState({
        pendingGameState: null
      });
    }
  }

  private handleMoveSync(source: ChessboardLib.Square , target: ChessboardLib.Square, piece: ChessboardLib.Piece): boolean {

    const moveIntent: MoveIntent = {
      chessPiece: BoardUtils.toChessPiece(piece),
      from: BoardUtils.squareToPosition(source),
      to: BoardUtils.squareToPosition(target)
    };

    const pendingGameState: GameState = {
      ...this.props.game,
      moveCount: this.props.game.moveCount + 1,
      playerInCheck: null, 
      board: this.optimisticallyCalculateNextBoard(moveIntent)
    };

    this.setState({
      error: null,
      pendingGameState
    });
    this.handleMove(moveIntent);
  
    return true;
  }

  private async handleMove(moveIntent: MoveIntent): Promise<void> {
    const {gameId} = this.props.game;
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

  private optimisticallyCalculateNextBoard(moveIntent: MoveIntent): number[] {
    const board: number[] = [...this.props.game.board];
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
    game: GameData;
  }

  export interface State {
    pendingGameState: GameState;
    error: Error;
  }
}

