package com.example.domain.listener;

import cn.hutool.json.JSONUtil;
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
        log.info("事件: {}", event.getType());
    }

    /**
     * 调度事件时抛出异常时的行为。返回false时，将忽略该异常。
     */
    @Override
    public boolean isFailOnException() {
        log.info("抛出异常监听……");
        return false;
    }

    /**
     * 是否触发事务生命周期事件。支持的事务生命周期事件的值是：COMMITTED，ROLLED_BACK，COMMITTING，ROLLINGBACK
     */
    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        log.info("事务事件监听……回滚前");
        return false;
    }

    @Override
    public String getOnTransaction() {
        log.info("事务事件监听……回滚时");
        return null;
    }
}
