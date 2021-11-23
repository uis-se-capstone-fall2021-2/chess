package chess.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chess.MoveIntent;
import chess.MoveValidator;
import chess.PlayerColor;
import chess.board.Board;

public class BoardTree {
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

    public int minimax(int depth, int alpha, int beta, Node node, boolean maximize) {
        treeSize++;
        if(depth == 0) {
            return ChessAI.getBoardScore(node.board);
        }
        PlayerColor team = (node.history.size() % 2 == 0) ? PlayerColor.WHITE : PlayerColor.BLACK;
        List<MoveIntent> legalMoves = MoveValidator.getAllValidMoves(node.board, node.history, team);
        if(maximize) {
            //maximize
            int bestMove = -9999;
            for(int i = 0; i < legalMoves.size(); i++) {
                Board tempBoard = node.board.copy();
                List<MoveIntent> updatedHistory = new ArrayList<>(node.history);
                tempBoard.updateBoard(legalMoves.get(i));

                Node newNode = new Node(tempBoard, updatedHistory);
                node.children.add(newNode);
                bestMove = Math.max(bestMove, minimax(depth - 1, alpha, beta, newNode, !maximize ));
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
                node.children.add(newNode);
                bestMove = Math.min(bestMove, minimax(depth - 1, alpha, beta, newNode, !maximize ));
                beta = Math.min(bestMove, alpha);
                if(beta <= alpha) {
                    return bestMove;
                }
            }
            return bestMove;
        }
        
    }

    public MoveIntent findMinimaxMove() {
        PlayerColor cteam = (head.history.size() % 2 == 0) ? PlayerColor.WHITE : PlayerColor.BLACK;
        List<MoveIntent> potentialMoves = MoveValidator.getAllValidMoves(head.board, head.history, cteam);
        List<Integer> minimaxValues = new ArrayList<>();

        for(MoveIntent move : potentialMoves) {
            Node newNode = new Node(head.board, head.history);
            newNode.board.updateBoard(move);
            newNode.history.add(move);
            int newNodeValue = minimax(ChessAI.MINIMAX_DEPTH, -9999, 9999, newNode, cteam == PlayerColor.WHITE);
            minimaxValues.add(newNodeValue);
        }
        List<Integer> goodMoves = new ArrayList<>();
        int smallest = 9999;
        int largest = -9999;

        for(int i = 0; i < minimaxValues.size(); i++){
            if(minimaxValues.get(i) > largest){
                largest = minimaxValues.get(i);
            }
            if(minimaxValues.get(i) < smallest) {
                smallest = minimaxValues.get(i);
            }
        }
        if(cteam == PlayerColor.WHITE) {
            //get a largest
            System.out.println("(Largest) Chosen move value is : " + largest);
            for(int i = 0; i < minimaxValues.size(); i++) {
                if(minimaxValues.get(i) == largest) {
                    goodMoves.add(i);
                }
            }
        } else {
            System.out.println("(Smallest) Chosen move value is : " + smallest);
            for(int i = 0; i < minimaxValues.size(); i++) {
                if(minimaxValues.get(i) == smallest) {
                    goodMoves.add(i);
                }
            }
        }

        int chosenIndex = r.nextInt(goodMoves.size());
        return potentialMoves.get(chosenIndex);
    }

}
