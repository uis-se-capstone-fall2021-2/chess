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
}