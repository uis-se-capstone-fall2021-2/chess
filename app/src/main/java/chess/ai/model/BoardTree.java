package chess.ai.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chess.MoveIntent;
import chess.MoveValidator;
import chess.PlayerColor;
import chess.board.Board;

public class BoardTree {
    static final int MINIMAX_DEPTH = 3;
    int treeSize;
    Random r;
    class Node {
        int score;
        Board board;
        boolean visited;
        List<MoveIntent> history;
        List<Node> children;
        Node(Board board, List<MoveIntent> history){
            this.board = board.copy();
            this.history = new ArrayList<>(history);
            score = 0;
            children = new ArrayList<>();
            visited = false;
        }
    }



    Node head;

    BoardTree(Board board, List<MoveIntent> history) {
        head = new Node(board, history);
        treeSize = 1;
        r = new Random();
    }




    
    /** 
     *  Returns the 'minimax value' of the provided node. When maximize is true, this 
     *  value is the highest minimax value of the node's children, whose own minimax values are the lowest
     *  value from their children, repeating until depth is zero, where the minimax value is simply the 
     *  board's current evaluation according to {@link #BoardEvaluation.getBoardScore(Board) getBoardScore} method.
     * 
     * 
     * @param depth the depth to recursively search future board states
     * @param alpha the highest value seen 
     * @param beta the lowest value seen
     * @param node the current board state being looked at
     * @param maximize true when assuming white is moving
     * @param team the team of the ai using the minimax method
     * @return int the minimax value of the current node
     */
    public int minimax(int depth, int alpha, int beta, Node node, boolean maximize, PlayerColor team) {
        treeSize++;
        if(depth == 0) {
            return BoardEvaluation.getBoardScore(node.board);
        }
    
        PlayerColor opponent = (team == PlayerColor.WHITE) ? PlayerColor.BLACK : PlayerColor.WHITE;

        List<MoveIntent> legalMoves = MoveValidator.getAllValidMoves(node.board, node.history, team);
        if(maximize) {
            //maximize
            int bestMove = -9999;
            for(int i = 0; i < legalMoves.size(); i++) {
                Board tempBoard = node.board.copy();
                List<MoveIntent> updatedHistory = new ArrayList<>(node.history);
                tempBoard.updateBoard(legalMoves.get(i));

                Node newNode = new Node(tempBoard, updatedHistory);
                bestMove = Math.max(bestMove, minimax(depth - 1, alpha, beta, newNode, !maximize , opponent));
                alpha = Math.max(bestMove, alpha);
                if(beta <= alpha) {
                    return bestMove;
                }
            }
            return bestMove;
        } else {
            //minimize
            int bestMove = 9999;
            for(int i = 0; i < legalMoves.size(); i++) {
                Board tempBoard = node.board.copy();
                List<MoveIntent> updatedHistory = new ArrayList<>(node.history);
                tempBoard.updateBoard(legalMoves.get(i));
                Node newNode = new Node(tempBoard, updatedHistory);
                bestMove = Math.min(bestMove, minimax(depth - 1, alpha, beta, newNode, !maximize , opponent));
                beta = Math.min(bestMove, beta);
                if(beta <= alpha) {
                    return bestMove;
                }
            }
            return bestMove;
        }
        
    }

    
    /** 
     *  Looks at each available move for the current board state, and returns the move with the highest
     *  or lowest minimax value depending on the team.
     * 
     * @param team the currently moving player
     * @return MoveIntent the preferred move based on the minimax values of each potential move
     */
    public MoveIntent findMinimaxMove(PlayerColor team) {
        List<MoveIntent> potentialMoves = MoveValidator.getAllValidMoves(head.board, head.history, team);
        PlayerColor opponent = (team == PlayerColor.WHITE) ? PlayerColor.BLACK : PlayerColor.WHITE;

        int largest = -9999;
        int smallest = 9999;
        int largestIndex = 0;
        int smallestIndex = 0;
        for(int i = 0; i < potentialMoves.size(); i++) {
            MoveIntent move = potentialMoves.get(i);
            Node newNode = new Node(head.board, head.history);
            newNode.board.updateBoard(move);
            newNode.history.add(move);
            int newNodeValue;
            newNodeValue = (team == PlayerColor.BLACK) 
            ? minimax(MINIMAX_DEPTH, -9999, 9999, newNode, true, opponent)
            : minimax(MINIMAX_DEPTH, -9999, 9999, newNode, false, opponent);

            if(newNodeValue > largest) {
                largest = newNodeValue;
                largestIndex = i;
            }
            if(newNodeValue < smallest) {
                smallest = newNodeValue;
                smallestIndex = i;
            }
        }
        if(team == PlayerColor.WHITE) {
            return potentialMoves.get(largestIndex);
        } else {
            return potentialMoves.get(smallestIndex);
        }
    }

}
