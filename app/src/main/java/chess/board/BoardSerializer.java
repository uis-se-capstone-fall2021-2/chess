package chess.board;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class BoardSerializer {
  public static class Serialize extends JsonSerializer<Board> {
    @Override
    public void serialize(Board board, JsonGenerator jgen, SerializerProvider provider) {
      try {
        if(board.board == null) {
          jgen.writeNull();
        } else {
          jgen.writeArray(board.board, 0, board.board.length);
        }
      } catch(Exception e) {

      }
    }
  }
}
