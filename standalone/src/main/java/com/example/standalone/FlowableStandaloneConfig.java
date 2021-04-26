package com.example.standalone;

import com.example.domain.listener.DefaultFlowableEventListener;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.engine.impl.history.HistoryLevel;
import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class FlowableStandaloneConfig {

    private final static String DATASOURCE_URL = "jdbc:h2:tcp://host.docker.internal:9091/mem:flowable;DB_CLOSE_DELAY=-1";
    private final static String DATASOURCE_USERNAME = "flowable";
    private final static String DATASOURCE_PASSWORD = "flowable";
    private final static String DATASOURCE_DRIVER = "org.h2.Driver";

    private ProcessEngine processEngine;

    public static ProcessEngine processEngine() {

        /**
         * 数据源配置
         */
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl(DATASOURCE_URL)
                .setJdbcUsername(DATASOURCE_USERNAME)
                .setJdbcPassword(DATASOURCE_PASSWORD)
                .setJdbcDriver(DATASOURCE_DRIVER)
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
                .setAsyncExecutorActivate(true)
                .setHistory(HistoryLevel.AUDIT.getKey())
                .setCreateDiagramOnDeploy(true)
                /**
                 * Mail配置
                 */
                .setMailServerHost("smtp.163.com")
                .setMailServerPort(25)
                .setMailServerDefaultFrom("abc@163.com")
                .setMailServerUsername("abc@163.com")
                .setMailServerPassword("123456");

        // 事件侦听器
        // cfg.setEventListeners(getEventListener());
        // 类型事件侦听器
        // cfg.setTypedEventListeners(getTypeEventListener());

        ProcessEngine processEngine = cfg.buildProcessEngine();
        String pName = processEngine.getName();
        String ver = ProcessEngine.VERSION;
        log.info("ProcessEngine [" + pName + "] Version: [" + ver + "]");

        return processEngine;
    }

    private static Map<String, List<FlowableEventListener>> getTypeEventListener() {
        Map<String, List<FlowableEventListener>> map = new HashMap<>();
        FlowableEventListener myJobEventListener = new DefaultFlowableEventListener();
        List<FlowableEventListener> objects = new ArrayList<>();
        objects.add(myJobEventListener);
        map.put("JOB_EXECUTION_SUCCESS,JOB_EXECUTION_FAILURE", objects);
        return map;
    }

    private static List<FlowableEventListener> getEventListener() {
        List<FlowableEventListener> list = new ArrayList<>();
        list.add(new DefaultFlowableEventListener());
        return list;
    }

    // 存储服务
    public RepositoryService repositoryService() {
        return processEngine.getRepositoryService();
    }

    // 运行时服务
    public RuntimeService runtimeService() {
        return processEngine.getRuntimeService();
    }

    // 任务服务
    public TaskService taskService() {
        return processEngine.getTaskService();
    }

    // 用户身份服务
    public IdentityService identityService() {
        return processEngine.getIdentityService();
    }

    // 管理服务
    public ManagementService managementService() {
        return processEngine.getManagementService();
    }

    // 表单服务
    public FormService formService() {
        return processEngine.getFormService();
    }

    // 历史记录
    public HistoryService historyService() {
        return processEngine.getHistoryService();
    }
}