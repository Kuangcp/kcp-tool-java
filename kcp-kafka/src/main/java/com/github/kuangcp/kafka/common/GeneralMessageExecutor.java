package com.github.kuangcp.kafka.common;


/**
 * @author https://github.com/kuangcp on 2019-11-13 09:49
 */
public interface GeneralMessageExecutor<T> extends MessageExecutor<Message<T>> {

  Class<T> getContentClass();
}