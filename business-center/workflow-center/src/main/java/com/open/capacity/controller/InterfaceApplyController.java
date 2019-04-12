package com.open.capacity.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 接口申请InterfaceApplyController
 * @author persie
 * @date 2018/07/19
 */
@Controller
@RequestMapping(value = "interfaceApply")
public class InterfaceApplyController {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ProcessEngine processEngine;

    /***************此处为业务代码******************/
    /**
     * 接口申请
     *
     * @param userId    用户Id
     * @param descption 描述
     * http://localhost:7010/interfaceApply/add?userId=owen
     */
    @RequestMapping(value = "add")
    @ResponseBody
    public String addExpense(String userId,String descption) {
        //启动流程
        HashMap<String, Object> map = new HashMap<>();
        map.put("taskUser", userId);
        map.put("clientId", "clientId");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Interface", map);
        return "提交成功.流程Id为：" + processInstance.getId();
    }

    /**
     * 获取审批管理列表
     * http://localhost:7010/interfaceApply/list?userId=per
     * http://localhost:7010/interfaceApply/list?userId=admin
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public String list(String userId) {
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().desc().list();
        
  
        
        for (Task task : tasks) {
        	System.out.println("=======1111=============="+ taskService.getVariable(task.getId(), "clientId"));
            System.out.println(task.toString());
        }
        return tasks.toArray().toString();

    }

    /**
     * 送审批准
     * @param taskId 任务ID
     * http://localhost:7010/interfaceApply/apply?taskId=cedfa2b4-38cb-11e9-b312-000ec6da9d45
     */
    @RequestMapping(value = "apply")
    @ResponseBody
    public String apply(String taskId  ) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        
        if (task == null) {
            throw new RuntimeException("流程不存在");
        }
        
        System.out.println("=======222=============="+ taskService.getVariable(task.getId(), "clientId"));
        
        //通过审核
        HashMap<String, Object> map = new HashMap<>();
        map.put("approve", "通过");
        taskService.complete(taskId, map);
        return "processed ok!";
    }

    /**
     * 送审拒绝  
     */
    @ResponseBody
    @RequestMapping(value = "reject")
    public String reject(String taskId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("approve", "驳回");
        taskService.complete(taskId, map);
        return "reject";
    }

    /**
     * 生成流程图
     *http://localhost:7010/interfaceApply/processDiagram?processId=9f328c57-38ca-11e9-afb9-000ec6da9d45
     * @param processId 任务ID
     */
    @RequestMapping(value = "processDiagram")
    public void genProcessDiagram(HttpServletResponse httpServletResponse, String processId) throws Exception {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();

        //流程走完的不显示图
        if (pi == null) {
            return;
        }
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        String InstanceId = task.getProcessInstanceId();
        List<Execution> executions = runtimeService
                .createExecutionQuery()
                .processInstanceId(InstanceId)
                .list();

        //得到正在执行的Activity的Id
        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }

        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows, engconf.getActivityFontName(), engconf.getLabelFontName(), engconf.getAnnotationFontName(), engconf.getClassLoader(), 1.0);
        OutputStream out = null;
        byte[] buf = new byte[1024];
        int legth = 0;
        try {
            out = httpServletResponse.getOutputStream();
            while ((legth = in.read(buf)) != -1) {
                out.write(buf, 0, legth);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
