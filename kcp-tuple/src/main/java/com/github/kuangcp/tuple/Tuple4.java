package com.github.kuangcp.tuple;

import com.github.kuangcp.tuple.base.Tuple;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author kuangcp on 2/13/19-11:39 AM
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Tuple4<A, B, C, D> extends Tuple {

  private A first;

  private B second;

  private C third;

  private D fourth;

  public static <A, B, C, D> Tuple4<A, B, C, D> of(A first, B second, C third, D fourth) {
    return new Tuple4<>(first, second, third, fourth);
  }

  @Override
  public Object[] getValueArray() {
    if (Objects.isNull(contents)) {
      contents = new Object[]{first, second, third, fourth};
    }

    return contents;
  }
}
