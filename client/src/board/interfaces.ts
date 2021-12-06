export enum Rank {
  _1 = '_1',
  _2 = '_2',
  _3 = '_3',
  _4 = '_4',
  _5 = '_5',
  _6 = '_6',
  _7 = '_7',
  _8 = '_8'
}

export enum File {
  A = 'A',
  B = 'B',
  C = 'C',
  D = 'D',
  E = 'E', 
  F = 'F',
  G = 'G',
  H = 'H'
}

export enum ChessPiece {
  NONE = 'NONE',
  PAWN = 'PAWN',
  ROOK = 'ROOK',
  KNIGHT = 'KNIGHT',
  BISHOP = 'BISHOP',
  QUEEN = 'QUEEN',
  KING = 'KING'
}

export interface Position {
  rank: Rank;
  file: File;
}


export interface MoveIntent {
  chessPiece: ChessPiece;
  from: Position;
  to: Position;
  promotion?: ChessPiece;
}

// these are the types for react-chessboard that they didn't export from the package
export namespace ChessboardLib {

  export type Square =
    | 'a8'
    | 'b8'
    | 'c8'
    | 'd8'
    | 'e8'
    | 'f8'
    | 'g8'
    | 'h8'
    | 'a7'
    | 'b7'
    | 'c7'
    | 'd7'
    | 'e7'
    | 'f7'
    | 'g7'
    | 'h7'
    | 'a6'
    | 'b6'
    | 'c6'
    | 'd6'
    | 'e6'
    | 'f6'
    | 'g6'
    | 'h6'
    | 'a5'
    | 'b5'
    | 'c5'
    | 'd5'
    | 'e5'
    | 'f5'
    | 'g5'
    | 'h5'
    | 'a4'
    | 'b4'
    | 'c4'
    | 'd4'
    | 'e4'
    | 'f4'
    | 'g4'
    | 'h4'
    | 'a3'
    | 'b3'
    | 'c3'
    | 'd3'
    | 'e3'
    | 'f3'
    | 'g3'
    | 'h3'
    | 'a2'
    | 'b2'
    | 'c2'
    | 'd2'
    | 'e2'
    | 'f2'
    | 'g2'
    | 'h2'
    | 'a1'
    | 'b1'
    | 'c1'
    | 'd1'
    | 'e1'
    | 'f1'
    | 'g1'
    | 'h1';

  export type Piece = 'wP' | 'wB' | 'wN' | 'wR' | 'wQ' | 'wK' | 'bP' | 'bB' | 'bN' | 'bR' | 'bQ' | 'bK';

  export type Position = {
    [S in Square]: Piece;
  };
}