package com.github.kuangcp.tuple;

import com.github.kuangcp.tuple.base.Tuple;
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
public class Tuple2<A, B> extends Tuple {

  private A first;
  private B second;

  public static <A, B> Tuple2<A, B> of(A first, B second) {
    return new Tuple2<>(first, second);
  }

  @Override
  public Object[] getValueArray() {
    if (Objects.isNull(contents)) {
      contents = new Object[]{first, second};
    }

    return contents;
  }

  // TODO 如何在父类上声明该方法的抽象方法
  public <C> Tuple3<A, B, C> addData(C data) {
    return new Tuple3<>(first, second, data);
  }

  @Override
  public String toString() {
    return first + "=" + second;
  }
}
