package chess.game.service.errorCodes;

public enum DeclineGameInviteErrorCode {
  UNKNOWN_PLAYER,
  GAME_NOT_FOUND,
  UNAUTHORIZED,
  OWN_GAME,
  NON_DECLINABLE_STATE
}
