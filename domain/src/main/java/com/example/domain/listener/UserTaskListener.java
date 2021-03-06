package com.example.domain.listener;

import lombok.extern.slf4j.Slf4j;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserTaskListener implements TaskListener {

    private static final long serialVersionUID = -8885457726836630603L;

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("Class Name: {}",this.getClass().getName());

        String name = delegateTask.getName();
        log.info("任务: {}", name);
    }
}
