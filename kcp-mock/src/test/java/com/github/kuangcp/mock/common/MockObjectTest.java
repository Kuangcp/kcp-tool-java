package com.github.kuangcp.mock.common;

import java.util.Optional;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-09-14 13:38
 */
public class MockObjectTest {

  @Test
  public void testSimpleCombination(){
    Optional<Petal> petalOpt = MockObject.mockObject(Petal.class);
    petalOpt.ifPresent(System.out::println);
  }

  @Test
  public void testCombination(){
    Optional<Flower> flowerOpt = MockObject.mockObject(Flower.class);
    flowerOpt.ifPresent(System.out::println);
  }
}


