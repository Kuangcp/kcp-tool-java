package com.github.kuangcp.base;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author kuangcp on 2/13/19-10:50 AM
 */
public abstract class Tuple implements Serializable, Comparable<Tuple> {

  protected Object[] array;

  public abstract Object[] getValueArray();

  // TODO optimize warn
  @Override
  public int compareTo(Tuple target) {
    if (Objects.isNull(target)) {
      return -1;
    }

    Object[] currentArray = this.getValueArray();
    Object[] targetArray = target.getValueArray();

    for (int i = 0; i < currentArray.length && i < targetArray.length; i++) {
      final Comparable cur = (Comparable) currentArray[i];
      final Comparable tar = (Comparable) targetArray[i];

      final int comparison = tar.compareTo(cur);
      if (comparison != 0) {
        return comparison;
      }
    }
    return Integer.compare(currentArray.length, targetArray.length);
  }
}
