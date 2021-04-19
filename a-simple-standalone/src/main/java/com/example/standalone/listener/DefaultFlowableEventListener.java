package com.example.standalone.listener;

import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;

/**
 * 参考 BaseEntityEventListener
 *
 * @author Adam
 */
@Slf4j
public class DefaultFlowableEventListener implements FlowableEventListener {

    @Override
    public void onEvent(FlowableEvent event) {
        log.info("DefaultEventListener.onEvent: {}", event);
    }

    /**
     * 调度事件时抛出异常时的行为。返回false时，将忽略该异常。
     */
    @Override
    public boolean isFailOnException() {
        log.info("DefaultEventListener.isFailOnException: {}", false);
        return false;
    }

    /**
     * 是否触发事务生命周期事件。支持的事务生命周期事件的值是：COMMITTED，ROLLED_BACK，COMMITTING，ROLLINGBACK
     */
    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        log.info("DefaultEventListener.isFireOnTransactionLifecycleEvent: {}", false);
        return false;
    }

    @Override
    public String getOnTransaction() {
        log.info("DefaultEventListener.getOnTransaction: null");
        return null;
    }
}
