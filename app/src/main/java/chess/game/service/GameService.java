package chess.game.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import chess.game.Game;

@Service
public class GameService {
  private final HashMap<Long, Game> games = new HashMap<Long, Game>();
  
}
