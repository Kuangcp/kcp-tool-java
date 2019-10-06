package com.github.kuangcp.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * create queue for every thread
 *
 * @author https://github.com/kuangcp on 2019-09-20 23:33
 */
public class MultiQueueExecutorService {

  List<ExecutorService> threadPool = new ArrayList<>();

  public static void init(int threadCount) {
    for (int i = 0; i < threadCount; i++) {

    }

  }

}
