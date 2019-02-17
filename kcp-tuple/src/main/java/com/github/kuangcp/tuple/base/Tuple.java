package com.github.kuangcp.tuple.base;

import java.io.Serializable;

/**
 * @author kuangcp on 2/13/19-10:50 AM
 */
public abstract class Tuple<E> implements Serializable {

  protected E[] contents;

  public E get(int index) {
    return contents[index];
  }

  public int size() {
    return contents.length;
  }

  public abstract Object[] getValueArray();
}
