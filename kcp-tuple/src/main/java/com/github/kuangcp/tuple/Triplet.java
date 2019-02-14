package com.github.kuangcp.tuple;

import com.github.kuangcp.tuple.base.Tuple;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author kuangcp on 2/13/19-11:13 AM
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Triplet<A, B, C> extends Tuple {

  private A first;

  private B second;

  private C third;

  public static <A, B, C> Triplet<A, B, C> of(A first, B second, C third) {
    return new Triplet<>(first, second, third);
  }

  @Override
  public Object[] getValueArray() {
    if (Objects.isNull(array)) {
      array = new Object[]{first, second, third};
    }

    return array;
  }
}
