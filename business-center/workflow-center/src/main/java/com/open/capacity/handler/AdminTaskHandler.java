package com.open.capacity.handler;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

/**
 * 管理员审批
 * @author persie
 * @date 2018/07/19
 */
public class AdminTaskHandler implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.setAssignee("admin");
    }

}
