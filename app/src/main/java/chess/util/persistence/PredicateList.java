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
    if(filter.size() == 0) {
      return;
    }

    LinkedList<Predicate> predicates = new LinkedList<Predicate>();
    for(Map.Entry<String, Object> entry: filter.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      Predicate predicate = cb.equal(entity.get(key), value);
      predicates.add(predicate);
    }
    add(cb.or(predicates.toArray(Predicate[]::new)));
  }

  public void addAndFilter(AndFilter filter) {
    if(filter.size() == 0) {
      return;
    }

    for(Map.Entry<String, Object> entry: filter.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      Predicate predicate = cb.equal(entity.get(key), value);
      add(cb.and(predicate));
    }
  }
}
