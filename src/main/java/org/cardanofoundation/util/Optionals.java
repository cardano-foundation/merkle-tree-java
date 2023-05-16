package org.cardanofoundation.util;

import io.vavr.collection.List;
import java.util.Optional;

public class Optionals {

  /** Finds first item */
  public static <T> Optional<T> findFirst(List<Optional<T>> items) {
    return items.filter(Optional::isPresent).getOrElse(Optional.empty());
  }
}
