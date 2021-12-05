import {ChessboardLib, Rank, File, Position, ChessPiece} from './interfaces';

export class BoardUtils {

  public static getFenString(board: number[], moveCount: number): string {
    
    if(!board) {
      return "";
    }
    var fen: string = "";
    for(var rank = 7; rank >= 0; rank--){
      fen = fen.concat(BoardUtils.rankNumToFenStr(rank, board));
      fen = fen.concat("/");
    }
    fen = fen.substring(0, fen.length-1);
    fen = moveCount % 2 === 0 ? fen.concat(" w") : fen.concat(" b");
    fen = fen.concat(" - - 0");
    fen = fen.concat(" " + moveCount);

    return fen;
  }

  public static getRankIntValue(rank: Rank): number{
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

  public static getFileIntValue(file: File): number{
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

  public static numToFenStr(piece: number): string {

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

  public static rankNumToFenStr(rank: number, board: number[]): string{
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
        fen = fen.concat(BoardUtils.numToFenStr(piece));
      }
    }

    // check last file
    piece = board[rank * 8 + file];
    if (piece === 0) {
      emptyCount++;
      fen = fen.concat(emptyCount.toString());
    }
    else {
      fen = fen.concat(BoardUtils.numToFenStr(piece));
    }

    return fen;
  }

  public static fileFromSquare(square: ChessboardLib.Square): File{
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

  public static rankFromSquare(square: ChessboardLib.Square): Rank{
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

  public static toPosition(square: ChessboardLib.Square): Position{
    const position: Position = {rank: BoardUtils.rankFromSquare(square), file: BoardUtils.fileFromSquare(square)};
    return position;
  }

  public static toChessPiece(piece: ChessboardLib.Piece): ChessPiece{
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

  public static getPieceIntValueFromPosition(board: number[], position: Position): number{
    return board[BoardUtils.getRankIntValue(position.rank) * 8 + BoardUtils.getFileIntValue(position.file)];
  }
}