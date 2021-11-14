import {Chessboard} from 'react-chessboard';
import {autobind} from 'core-decorators';
import * as React from 'react';

import {ChessboardLib, MoveIntent, Rank, File, Position, ChessPiece} from '../../../board/interfaces';
import {Inject} from '../../../di';
import {GameService, GameState} from '../../interfaces';

@autobind
export class GameBoard extends React.Component<GameBoard.Props, GameBoard.State> {

  @Inject(GameService.Token)
  private readonly gameService: GameService;

  public override state: GameBoard.State = {
    error: null,
    pendingGameState: null
  };

  public override render(): React.ReactNode {
    const {gameId} = this.props.gameState;
    return (
      <Chessboard
        id={gameId}
        position={this.getChessboardPosition()}
        onPieceDrop={this.handleMoveSync}
      />
    );
  }


  private getChessboardPosition(): string {
    const gameState: GameState = this.state.pendingGameState ?? this.props.gameState;
    // TODO: get Chessboard game state from our GameState model
    const board = gameState.board;
    var fen: string = "";
    for(var rank = 7; rank >= 0; rank--){
      fen = fen.concat(this.rankNumToFenStr(rank, board));
      fen = fen.concat("/");
    }
    fen = fen.substring(0, fen.length-1);
    return fen;
  } 

  private numToFenStr(piece: number): string {

    if (Math.abs(piece) == 1)
      return piece == -1 ? "p" : "P";
    else if (Math.abs(piece) == 2)
      return piece == -2 ? "r" : "R";
    else if (Math.abs(piece) == 3)
      return piece == -3 ? "n" : "N";
    else if (Math.abs(piece) == 4)
      return piece == -4 ? "b" : "B";
    else if (Math.abs(piece) == 5)
      return piece == -5 ? "q" : "Q";
    else if (Math.abs(piece) == 6)
      return piece == -6 ? "k" : "K";
  }

  private rankNumToFenStr(rank: number, board: number[]): string{
    var fen: string = "";
    var emptyCount: number = 0;

    // check all files except last
    for(var file = 0; file < 7; file++){
      var piece: number = board[rank * 8 + file];
 
      if(piece == 0){
        emptyCount++;
        if(board[rank * 8 + (file+1)] == 0)
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
    var piece: number = board[rank * 8 + file];
    if (piece == 0) {
      emptyCount++;
      fen = fen.concat(emptyCount.toString());
    }
    else {
      fen = fen.concat(this.numToFenStr(piece));
    }

    return fen;
  }

  private handleMoveSync(source: ChessboardLib.Square , target: ChessboardLib.Square, piece: ChessboardLib.Piece): boolean {
    // TODO: transform source, target, and piece into MoveIntent
    const moveIntent: MoveIntent = {chessPiece: this.toChessPiece(piece), from: this.toPosition(source), to: this.toPosition(target)};
    // TODO: calculate desired GameState from moveIntent
    const pendingGameState: GameState = {} as any;
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
      await this.gameService.move(gameId, moveIntent); 
    } catch(e) {
      this.setState({
        error: e as Error
      });
    } finally {
      this.setState({
        pendingGameState: null
      });
    }
  }

  private fileFromSquare(square: ChessboardLib.Square): File{
    if (square.charAt(0).localeCompare("a") == 0)
      return File.A;
    else if (square.charAt(0).localeCompare("b") == 0)
      return File.B;
    else if (square.charAt(0).localeCompare("c") == 0)
      return File.C;
    else if (square.charAt(0).localeCompare("d") == 0)
      return File.D;
    else if (square.charAt(0).localeCompare("e") == 0)
      return File.E;
    else if (square.charAt(0).localeCompare("f") == 0)
      return File.F;
    else if (square.charAt(0).localeCompare("g") == 0)
      return File.G;
    else if (square.charAt(0).localeCompare("h") == 0)
      return File.H;
  }

  private rankFromSquare(square: ChessboardLib.Square): Rank{
    if (square.charAt(1).localeCompare("1") == 0)
      return Rank._1;
    else if (square.charAt(1).localeCompare("2") == 0)
      return Rank._2;
    else if (square.charAt(1).localeCompare("3") == 0)
      return Rank._3;
    else if (square.charAt(1).localeCompare("4") == 0)
      return Rank._4;
    else if (square.charAt(1).localeCompare("5") == 0)
      return Rank._5;
    else if (square.charAt(1).localeCompare("6") == 0)
      return Rank._6;
    else if (square.charAt(1).localeCompare("7") == 0)
      return Rank._7;
    else if (square.charAt(1).localeCompare("8") == 0)
      return Rank._8;
  }

  private toPosition(square: ChessboardLib.Square): Position{
    const position: Position = {rank: this.rankFromSquare(square), file: this.fileFromSquare(square)};
    return position;
  }

  private toChessPiece(piece: ChessboardLib.Piece): ChessPiece{
    if (piece.charAt(1).localeCompare("P") == 0)
      return ChessPiece.PAWN;
    else if (piece.charAt(1).localeCompare("R") == 0)
      return ChessPiece.ROOK;
    else if (piece.charAt(1).localeCompare("N") == 0)
      return ChessPiece.KNIGHT;
    else if (piece.charAt(1).localeCompare("B") == 0)
      return ChessPiece.BISHOP;
    else if (piece.charAt(1).localeCompare("Q") == 0)
      return ChessPiece.QUEEN;
    else if (piece.charAt(1).localeCompare("K") == 0)
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

