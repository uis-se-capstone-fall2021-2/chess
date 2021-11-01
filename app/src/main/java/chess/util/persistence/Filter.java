package chess.util.persistence;

import java.util.HashMap;
import java.util.Map;

public abstract class Filter extends HashMap<String, Object> {
  public Filter() {
    super();
  }
  public Filter(Map<String, Object> m) {
    super(m);
  }
}
