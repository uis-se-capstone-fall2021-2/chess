import {Alert} from '@mui/material';
import {autobind} from 'core-decorators';
import {isEqual} from 'lodash';
import * as React from 'react';
import {Chessboard} from 'react-chessboard';

import {ChessboardLib, MoveIntent, Rank, File, Position, ChessPiece} from '../../../board/interfaces';
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
    const {gameId, players, board} = this.props.gameState;
    const {pendingGameState} = this.state;

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
                    position={this.getFenString(pendingGameState?.board ?? board)}
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


  private getFenString(board: number[]): string {
    
    if(!board) {
      return "";
    }
    var fen: string = "";
    for(var rank = 7; rank >= 0; rank--){
      fen = fen.concat(this.rankNumToFenStr(rank, board));
      fen = fen.concat("/");
    }
    fen = fen.substring(0, fen.length-1);
    fen = this.props.gameState.moveCount % 2 === 0 ? fen.concat(" w") : fen.concat(" b");
    fen = fen.concat(" - - 0");
    fen = fen.concat(" " + this.props.gameState.moveCount);

    return fen;
  }

  private handleMoveSync(source: ChessboardLib.Square , target: ChessboardLib.Square, piece: ChessboardLib.Piece): boolean {
    // transform source, target, and piece into MoveIntent
    const moveIntent: MoveIntent = {chessPiece: this.toChessPiece(piece), from: this.toPosition(source), to: this.toPosition(target)};
    
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
    temp.load(this.getFenString(this.props.gameState.board));
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
    const nextBoard: number[] = [...this.props.gameState.board];
    const fromIndex: number = this.getRankIntValue(moveIntent.from.rank) * 8 + this.getFileIntValue(moveIntent.from.file);
    const toIndex: number = this.getRankIntValue(moveIntent.to.rank) * 8 + this.getFileIntValue(moveIntent.to.file);
    const piece: number = this.getPieceIntValueFromPosition(moveIntent.from);

    nextBoard[fromIndex] = 0;
    nextBoard[toIndex] = piece;

    return nextBoard;
  }

  private getPieceIntValueFromPosition(position: Position): number{
    return this.props.gameState.board[this.getRankIntValue(position.rank) * 8 + this.getFileIntValue(position.file)];
  }

  private getRankIntValue(rank: Rank): number{
    if (rank.localeCompare("_1") === 0)
      return 0;
    else if (rank.localeCompare("_2") === 0)
      return 1;
    else if (rank.localeCompare("_3") === 0)
      return 2;
    else if (rank.localeCompare("_4") === 0)
      return 3;
    else if (rank.localeCompare("_5") === 0)
      return 4;
    else if (rank.localeCompare("_6") === 0)
      return 5;
    else if (rank.localeCompare("_7") === 0)
      return 6;
    else if (rank.localeCompare("_8") === 0)
      return 7;
  }

  private getFileIntValue(file: File): number{
    if (file === "A")
      return 0;
    else if (file === "B")
      return 1;
    else if (file === "C")
      return 2;
    else if (file === "D")
      return 3;
    else if (file === "E")
      return 4;
    else if (file === "F")
      return 5;
    else if (file === "G")
      return 6;
    else if (file === "H")
      return 7;
  }

  private numToFenStr(piece: number): string {

    if (Math.abs(piece) === 1)
      return piece === -1 ? "p" : "P";
    else if (Math.abs(piece) === 2)
      return piece === -2 ? "r" : "R";
    else if (Math.abs(piece) === 3)
      return piece === -3 ? "n" : "N";
    else if (Math.abs(piece) === 4)
      return piece === -4 ? "b" : "B";
    else if (Math.abs(piece) === 5)
      return piece === -5 ? "q" : "Q";
    else if (Math.abs(piece) === 6)
      return piece === -6 ? "k" : "K";
  }

  private rankNumToFenStr(rank: number, board: number[]): string{
    var fen: string = "";
    var emptyCount: number = 0;

    // check all files except last
    for(var file = 0; file < 7; file++){
      var piece: number = board[rank * 8 + file];
 
      if(piece === 0){
        emptyCount++;
        if(board[rank * 8 + (file+1)] === 0)
          continue;
        else{
          fen = fen.concat(emptyCount.toString());
          emptyCount = 0;
        }
      }
      else{
        fen = fen.concat(this.numToFenStr(piece));
      }
    }

    // check last file
    piece = board[rank * 8 + file];
    if (piece === 0) {
      emptyCount++;
      fen = fen.concat(emptyCount.toString());
    }
    else {
      fen = fen.concat(this.numToFenStr(piece));
    }

    return fen;
  }

  private fileFromSquare(square: ChessboardLib.Square): File{
    if (square.charAt(0).localeCompare("a") === 0)
      return File.A;
    else if (square.charAt(0).localeCompare("b") === 0)
      return File.B;
    else if (square.charAt(0).localeCompare("c") === 0)
      return File.C;
    else if (square.charAt(0).localeCompare("d") === 0)
      return File.D;
    else if (square.charAt(0).localeCompare("e") === 0)
      return File.E;
    else if (square.charAt(0).localeCompare("f") === 0)
      return File.F;
    else if (square.charAt(0).localeCompare("g") === 0)
      return File.G;
    else if (square.charAt(0).localeCompare("h") === 0)
      return File.H;
  }

  private rankFromSquare(square: ChessboardLib.Square): Rank{
    if (square.charAt(1).localeCompare("1") === 0)
      return Rank._1;
    else if (square.charAt(1).localeCompare("2") === 0)
      return Rank._2;
    else if (square.charAt(1).localeCompare("3") === 0)
      return Rank._3;
    else if (square.charAt(1).localeCompare("4") === 0)
      return Rank._4;
    else if (square.charAt(1).localeCompare("5") === 0)
      return Rank._5;
    else if (square.charAt(1).localeCompare("6") === 0)
      return Rank._6;
    else if (square.charAt(1).localeCompare("7") === 0)
      return Rank._7;
    else if (square.charAt(1).localeCompare("8") === 0)
      return Rank._8;
  }

  private toPosition(square: ChessboardLib.Square): Position{
    const position: Position = {rank: this.rankFromSquare(square), file: this.fileFromSquare(square)};
    return position;
  }

  private toChessPiece(piece: ChessboardLib.Piece): ChessPiece{
    if (piece.charAt(1).localeCompare("P") === 0)
      return ChessPiece.PAWN;
    else if (piece.charAt(1).localeCompare("R") === 0)
      return ChessPiece.ROOK;
    else if (piece.charAt(1).localeCompare("N") === 0)
      return ChessPiece.KNIGHT;
    else if (piece.charAt(1).localeCompare("B") === 0)
      return ChessPiece.BISHOP;
    else if (piece.charAt(1).localeCompare("Q") === 0)
      return ChessPiece.QUEEN;
    else if (piece.charAt(1).localeCompare("K") === 0)
      return ChessPiece.KING;
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

