package com.github.kuangcp.tuple;

import com.github.kuangcp.tuple.base.Tuple;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 2/17/19-10:15 AM
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Tuple1<A> extends Tuple {

  private A first;

  public static <A> Tuple1<A> of(A first) {
    return new Tuple1<>(first);
  }

  @Override
  public Object[] getValueArray() {
    if (Objects.isNull(contents)) {
      contents = new Object[]{first};
    }

    return contents;
  }

  public <B> Tuple2<A, B> addData(B data) {
    return new Tuple2<>(first, data);
  }
}
