import {Chessboard} from 'react-chessboard';
import {autobind} from 'core-decorators';
import * as React from 'react';

import {ChessboardLib, MoveIntent, Position, ChessPiece} from '../../../board/interfaces';
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

    //console.log("rankNumToFenStr: " + fen);
    return fen;
  }

  private handleMoveSync(source: ChessboardLib.Square , target: ChessboardLib.Square, piece: ChessboardLib.Piece): boolean {
    // TODO: transform source, target, and piece into MoveIntent
    const moveIntent: MoveIntent = {} as any;
    // TODO: calculate desired GameState from moveIntent
    const pendingGameState: GameState = {} as any;
    this.setState({
      error: null,
      pendingGameState
    });
    this.handleMove(moveIntent);
    return true;
  }

  private toPosition(square: ChessboardLib.Square): Position{

    const position: Position = {} as any;
    return position;
  }

  private toChessPiece(piece: ChessboardLib.Piece): ChessPiece{

    const chessPiece: ChessPiece = {} as any;
    return chessPiece
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

