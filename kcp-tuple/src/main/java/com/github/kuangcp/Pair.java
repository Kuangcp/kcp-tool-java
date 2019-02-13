package com.github.kuangcp;

import com.github.kuangcp.base.Tuple;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author kuangcp
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Pair<A, B> extends Tuple {

  private A first;

  private B second;

  public static <A, B> Pair<A, B> of(A first, B second) {
    return new Pair<>(first, second);
  }

  @Override
  public Object[] getValueArray() {
    if (Objects.isNull(array)) {
      array = new Object[]{first, second};
    }

    return array;
  }

  @Override
  public String toString() {
    return first + "=" + second;
  }
}
