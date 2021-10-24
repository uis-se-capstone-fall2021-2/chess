package chess.util.persistence;

import java.util.LinkedList;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PredicateList<U> extends LinkedList<Predicate> {
  private final CriteriaBuilder cb;
  private final Root<U> entity;

  public boolean add(Predicate predicate) {
    return super.add(predicate);
  }

  public void addOrFilter(OrFilter filter) {
    for(Map.Entry<String, Object> entry: filter.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      Predicate predicate = cb.equal(entity.get(key), value);
      add(cb.or(predicate));
    }
  }

  public void addAndFilter(AndFilter filter) {
    for(Map.Entry<String, Object> entry: filter.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      Predicate predicate = cb.equal(entity.get(key), value);
      add(cb.and(predicate));
    }
  }
}
