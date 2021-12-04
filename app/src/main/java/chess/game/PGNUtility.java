package chess.game;

import java.util.ArrayList;
import java.util.List;

import chess.ChessPiece;
import chess.File;
import chess.MoveIntent;
import chess.MoveValidator;
import chess.PlayerColor;
import chess.Position;
import chess.Rank;
import chess.board.Board;
import chess.board.InCheck;
/**
 * Utility class with methods related to converting our format of moves to the standard chess notation: <cite><a href="https://en.wikipedia.org/wiki/Algebraic_notation_(chess)">Algebraic Notation</a></cite>
 */
public class PGNUtility {
    public static String convertMove(MoveIntent move, Board board, List<MoveIntent> history){

        if(move.chessPiece.equals(ChessPiece.KING)){
            int fileDifference = (move.from.file.value - move.to.file.value);
            if(fileDifference == 2){
                //queenside castle
                return "O-O-O";
            } else if (fileDifference == -2) {
                //kingside castle
                return "O-O";
            }
        }

        // Begin with the ending location. In some cases this is all that is needed.
        String end = fileToString(move.to.file) + rankToString(move.to.rank);
        String pieceType = getPieceType(move.chessPiece);
        String capture = (board.getPiece(move.to) != 0)
            ? "x"
            : "";
        String disambiguationString;
        // another check for pawn capturing, since end location will not always have an opponent's piece, in the case of en passant.
        if(move.chessPiece.equals(ChessPiece.PAWN) && move.from.file != move.to.file){
            capture = "x";
            // a pawn making a capture is always preceded by the file of the pawn.
            disambiguationString = fileToString(move.from.file);
        } else {
            disambiguationString = disambiguate(move, board, history);
        }
        //set promotion to =(piece letter), or an empty string if there was no promotion.
        String promotion = (!move.promotion.equals(ChessPiece.NONE)) ? "=" + getPieceType(move.promotion) : "";


        Board copiedBoard = board.copy();
        List<MoveIntent> copiedHistory = new ArrayList<>(history);
        copiedHistory.add(move);
        copiedBoard.updateBoard(move);
        InCheck newCheckStatus = copiedBoard.inCheck();
        //If move results in a check, add a + to the end.
        String check = (newCheckStatus != InCheck.NONE) ? "+" : "";

        PlayerColor team = (copiedHistory.size() % 2 == 0)
                ? PlayerColor.WHITE
                : PlayerColor.BLACK;
        if(MoveValidator.getAllValidMoves(copiedBoard, copiedHistory, team).isEmpty()){
            //checkmate, or stalemate.
            switch(newCheckStatus){
                case WHITE:
                case BLACK:
                check = "#";
                break;
                default:
                check = "";
                break;
            }
        }
        return (pieceType + disambiguationString + capture + end + promotion + check);
    }
    private static String disambiguate(MoveIntent move, Board board, List<MoveIntent> history) {
        boolean rankAmbiguity = false;
        boolean fileAmbiguity = false;
        for(int i = 0; i < board.board.length; i++){
            Position positionToCheck = new Position(i);
            ChessPiece currentPieceType = ChessPiece.FromInteger(board.getPiece(positionToCheck));
            PlayerColor team = (board.getPiece(positionToCheck) > 0)
                ? PlayerColor.WHITE
                : PlayerColor.BLACK;
            PlayerColor myTeam = (board.getPiece(move.from) > 0)
                ? PlayerColor.WHITE
                : PlayerColor.BLACK;
            //Same piece and same team, different position and could make a legal move to the same square as the actual move made.
            if(team == myTeam && currentPieceType.equals(move.chessPiece) 
                && !move.from.equals(positionToCheck)
                && MoveValidator.validateMove(new MoveIntent(currentPieceType, positionToCheck, move.to), board, history, team)
            ){
                if(move.from.file.equals(positionToCheck.file)){
                    rankAmbiguity = true;
                }
                if(move.from.rank.equals(positionToCheck.rank)){
                    fileAmbiguity = true;
                }
            }
        }

        if(rankAmbiguity && fileAmbiguity){
            //In rare cases, three of the same piece type could move to the same spot, meaning both file and rank need be provided. This can happen if pawns are promoted.
            return fileToString(move.from.file) + rankToString(move.from.rank);
        } else if (fileAmbiguity) {
            return fileToString(move.from.file);
        } else if (rankAmbiguity){
            return rankToString(move.from.rank);
        } else {
            return "";
        }
    }
    private static String getPieceType(ChessPiece input) {
        switch(input){
            case KNIGHT:
            return "N";
            case BISHOP:
            return "B";
            case ROOK:
            return "R";
            case QUEEN:
            return "Q";
            case KING:
            return "K";
            default:
            return "";
        }
    }
    private static String rankToString(Rank rank){
        switch(rank){
            case _1:
            return "1";
            case _2:
            return "2";
            case _3:
            return "3";
            case _4:
            return "4";
            case _5:
            return "5";
            case _6:
            return "6";
            case _7:
            return "7";
            case _8:
            return "8";
            default:
            throw new IllegalArgumentException();
        }
    }
    private static String fileToString(File file){
        switch(file){
            case A:
            return "a";
            case B:
            return "b";
            case C:
            return "c";
            case D:
            return "d";
            case E:
            return "e";
            case F:
            return "f";
            case G:
            return "g";
            case H:
            return "h";
            default:
            throw new IllegalArgumentException();
        }
    }
}
