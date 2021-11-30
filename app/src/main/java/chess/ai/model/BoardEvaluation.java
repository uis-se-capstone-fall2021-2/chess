package chess.ai.model;

import chess.board.Board;

import chess.board.Board;

public class BoardEvaluation {
    
    //Square tables for each piece to encourage certain behaviour from ai.
    // The tables are used in the board evalation function, so a pawn on an index with value of 50 according to the pawn square table
    // would be worth 150 rather than the standard 100 for a pawn. Pawns on negative squares are then worth less,
    // encouraging the ai to move the pawn to improve its evaluation.

    // The values used in these tables are from the Chess Programming wiki:
    // https://www.chessprogramming.org/Simplified_Evaluation_Function
    private static final int[][] pawnSquareTable = {
        {
            0,  0,  0,  0,  0,  0,  0,  0,
            50, 50, 50, 50, 50, 50, 50, 50,
            10, 10, 20, 30, 30, 20, 10, 10,
            5,  5, 10, 25, 25, 10,  5,  5,
            0,  0,  0, 20, 20,  0,  0,  0,
            5, -5,-10,  0,  0,-10, -5,  5,
            5, 10, 10,-20,-20, 10, 10,  5,
            0,  0,  0,  0,  0,  0,  0,  0
        },
        {
            0,  0,  0,  0,  0,  0,  0,  0,
            5, 10, 10,-20,-20, 10, 10,  5,
            5, -5,-10,  0,  0,-10, -5,  5,
            0,  0,  0, 20, 20,  0,  0,  0,
            5,  5, 10, 25, 25, 10,  5,  5,
            10, 10, 20, 30, 30, 20, 10, 10,
            50, 50, 50, 50, 50, 50, 50, 50,
            0,  0,  0,  0,  0,  0,  0,  0    
        }
    };

    private static final int[][] knightSquareTable = {
        {
            -50,-40,-30,-30,-30,-30,-40,-50,
            -40,-20,  0,  0,  0,  0,-20,-40,
            -30,  0, 10, 15, 15, 10,  0,-30,
            -30,  5, 15, 20, 20, 15,  5,-30,
            -30,  0, 15, 20, 20, 15,  0,-30,
            -30,  5, 10, 15, 15, 10,  5,-30,
            -40,-20,  0,  5,  5,  0,-20,-40,
            -50,-40,-30,-30,-30,-30,-40,-50 
        },
        {
            -50,-40,-30,-30,-30,-30,-40,-50, 
            -40,-20,  0,  5,  5,  0,-20,-40,
            -30,  5, 10, 15, 15, 10,  5,-30,
            -30,  0, 15, 20, 20, 15,  0,-30,
            -30,  5, 15, 20, 20, 15,  5,-30,
            -30,  0, 10, 15, 15, 10,  0,-30,
            -40,-20,  0,  0,  0,  0,-20,-40,
            -50,-40,-30,-30,-30,-30,-40,-50
        }
    };
    private static final int[][] bishopSquareTable = {
        {
            -20,-10,-10,-10,-10,-10,-10,-20,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -10,  0,  5, 10, 10,  5,  0,-10,
            -10,  5,  5, 10, 10,  5,  5,-10,
            -10,  0, 10, 10, 10, 10,  0,-10,
            -10, 10, 10, 10, 10, 10, 10,-10,
            -10,  5,  0,  0,  0,  0,  5,-10,
            -20,-10,-10,-10,-10,-10,-10,-20
        }, 
        {
            -20,-10,-10,-10,-10,-10,-10,-20,
            -10,  5,  0,  0,  0,  0,  5,-10,
            -10, 10, 10, 10, 10, 10, 10,-10,
            -10,  0, 10, 10, 10, 10,  0,-10,
            -10,  5,  5, 10, 10,  5,  5,-10,
            -10,  0,  5, 10, 10,  5,  0,-10,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -20,-10,-10,-10,-10,-10,-10,-20
        }
       
    };
    private static final int[][] rookSquareTable = {
        {
            0,  0,  0,  0,  0,  0,  0,  0,
            5, 10, 10, 10, 10, 10, 10,  5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            0,  -5,  0,  5,  5,  0,  -5,  0
        },
        {
            0,  -5,  0,  5,  5,  0,  -5,  0,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            5, 10, 10, 10, 10, 10, 10,  5,
            0,  0,  0,  0,  0,  0,  0,  0
        }

    };
    private static final int[][] queenSquareTable = {
        {
            -20,-10,-10, -5, -5,-10,-10,-20,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -10,  0,  5,  5,  5,  5,  0,-10,
             -5,  0,  5,  5,  5,  5,  0, -5,
             -5,  0,  5,  5,  5,  5,  0, 0,
            -10,  0,  5,  5,  5,  5,  5,-10,
            -10,  0,  0,  0,  0,  5,  0,-10,
            -20,-10,-10, -5, -5,-10,-10,-20
        },
        {
            -20,-10,-10, -5, -5,-10,-10,-20,
            -10,  0,  0,  0,  0,  5,  0,-10,
            -10,  0,  5,  5,  5,  5,  5,-10,
             -5,  0,  5,  5,  5,  5,  0, 0,
             -5,  0,  5,  5,  5,  5,  0, -5,
            -10,  0,  5,  5,  5,  5,  0,-10,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -20,-10,-10, -5, -5,-10,-10,-20
        }
    };
    private static final int[][] kingSquareTable = {
        {
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -20,-30,-30,-40,-40,-30,-30,-20,
            -10,-20,-20,-20,-20,-20,-20,-10,
             20, 20,  0,  0,  0,  0, 20, 20,
             20, 30, 10,  0,  0, 10, 30, 20
        },
        {
            20, 30, 10,  0,  0, 10, 30, 20,
            20, 20,  0,  0,  0,  0, 20, 20,
            -10,-20,-20,-20,-20,-20,-20,-10,
            -20,-30,-30,-40,-40,-30,-30,-20,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30
        }

    };
    // returns simple piece values
    protected static int getPieceValue(int piece, int location) {
        if(piece == 0) return 0;
        int team = piece / Math.abs(piece);
        int locationModifier = 0;
        switch(Math.abs(piece)) {
            case 0:
            return 0;
            case 1:
            if(team == -1) {
                locationModifier = -pawnSquareTable[0][location];
            } else {
                locationModifier = pawnSquareTable[1][location];
            }
            return (100 * team) + locationModifier;
            case 2:
            if(team == -1) {
                locationModifier = -rookSquareTable[0][location];
            } else {
                locationModifier = rookSquareTable[1][location];
            }
            return (500 * team) + locationModifier;
            case 3:
            if(team == -1) {
                locationModifier = -knightSquareTable[0][location];
            } else {
                locationModifier = knightSquareTable[1][location];
            }
            return (320 * team) + locationModifier;
            case 4:
            if(team == -1) {
                locationModifier = -bishopSquareTable[0][location];
            } else {
                locationModifier = bishopSquareTable[1][location];
            }
            return (330 * team) + locationModifier;
            case 5:
            if(team == -1) {
                locationModifier = -queenSquareTable[0][location];
            } else {
                locationModifier = queenSquareTable[1][location];
            }
            return (900 * team) + locationModifier;
            case 6:
            if(team == -1) {
                locationModifier = -kingSquareTable[0][location];
            } else {
                locationModifier = kingSquareTable[1][location];
            }
            return (20000 * team) + locationModifier;
            default:
            return 0;

        }
    }
    public static int getBoardScore(Board board) {
        int boardScore = 0;
        for(int i = 0; i < board.board.length; i++){
            int piece = board.board[i];
            boardScore += getPieceValue(piece, i);
        }
        return boardScore;
    }
}
