package com.xxl.job.admin.core.trigger;

import com.xxl.job.admin.core.enums.ExecutorFailStrategyEnum;
import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.model.XxlJobLog;
import com.xxl.job.admin.core.route.ExecutorRouteStrategyEnum;
import com.xxl.job.admin.core.schedule.XxlJobDynamicScheduler;
import com.xxl.job.admin.core.thread.JobFailMonitorHelper;
import com.xxl.job.admin.core.util.I18nUtil;
import com.xxl.job.core.biz.ExecutorBiz;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.biz.model.TriggerParam;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.util.IpUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;

/**
 * xxl-job trigger
 * Created by xuxueli on 17/7/13.
 */
public class XxlJobTrigger {
    private static Logger logger = LoggerFactory.getLogger(XxlJobTrigger.class);

    /**
     * trigger job
     *
     * @param jobId
     */
    public static void trigger(int jobId) {

        // load data
        // 通过JobId从数据库中查询该任务的具体信息
        XxlJobInfo jobInfo = XxlJobDynamicScheduler.xxlJobInfoDao.loadById(jobId);              // job info
        if (jobInfo == null) {
            logger.warn(">>>>>>>>>>>> trigger fail, jobId invalid，jobId={}", jobId);
            return;
        }
        // 获取该类型的执行器信息
        XxlJobGroup group = XxlJobDynamicScheduler.xxlJobGroupDao.load(jobInfo.getJobGroup());  // group info
        // 匹配运行模式
        ExecutorBlockStrategyEnum blockStrategy = ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), ExecutorBlockStrategyEnum.SERIAL_EXECUTION);  // block strategy
        // 匹配失败后的处理模式
        ExecutorFailStrategyEnum failStrategy = ExecutorFailStrategyEnum.match(jobInfo.getExecutorFailStrategy(), ExecutorFailStrategyEnum.FAIL_ALARM);    // fail strategy
        //  获取路由策略
        ExecutorRouteStrategyEnum executorRouteStrategyEnum = ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null);    // route strategy
        // 获取该执行器的集群机器列表
        ArrayList<String> addressList = (ArrayList<String>) group.getRegistryList();

        // broadcast
        // 判断路由策略  是否为  分片广播模式
        if (ExecutorRouteStrategyEnum.SHARDING_BROADCAST == executorRouteStrategyEnum && CollectionUtils.isNotEmpty(addressList)) {
            for (int i = 0; i < addressList.size(); i++) {
                String address = addressList.get(i);

                // 1、save log-id  保存日志
                XxlJobLog jobLog = new XxlJobLog();
                jobLog.setJobGroup(jobInfo.getJobGroup());
                jobLog.setJobId(jobInfo.getId());
                XxlJobDynamicScheduler.xxlJobLogDao.save(jobLog);
                logger.debug(">>>>>>>>>>> xxl-job trigger start, jobId:{}", jobLog.getId());

                // 2、prepare trigger-info
                //jobLog.setExecutorAddress(executorAddress);
                jobLog.setGlueType(jobInfo.getGlueType());
                jobLog.setExecutorHandler(jobInfo.getExecutorHandler());
                jobLog.setExecutorParam(jobInfo.getExecutorParam());
                jobLog.setTriggerTime(new Date());

                ReturnT<String> triggerResult = new ReturnT<String>(null);
                StringBuffer triggerMsgSb = new StringBuffer();
                triggerMsgSb.append(I18nUtil.getString("jobconf_trigger_admin_adress")).append("：").append(IpUtil.getIp());
                triggerMsgSb.append("<br>").append(I18nUtil.getString("jobconf_trigger_exe_regtype")).append("：")
                        .append( (group.getAddressType() == 0)?I18nUtil.getString("jobgroup_field_addressType_0"):I18nUtil.getString("jobgroup_field_addressType_1") );
                triggerMsgSb.append("<br>").append(I18nUtil.getString("jobconf_trigger_exe_regaddress")).append("：").append(group.getRegistryList());
                triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_executorRouteStrategy")).append("：").append(executorRouteStrategyEnum.getTitle()).append("("+i+"/"+addressList.size()+")"); // update01
                triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_executorBlockStrategy")).append("：").append(blockStrategy.getTitle());
                triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_executorFailStrategy")).append("：").append(failStrategy.getTitle());

                // 3、trigger-valid
                if (triggerResult.getCode()==ReturnT.SUCCESS_CODE && CollectionUtils.isEmpty(addressList)) {
                    triggerResult.setCode(ReturnT.FAIL_CODE);
                    triggerMsgSb.append("<br>----------------------<br>").append(I18nUtil.getString("jobconf_trigger_address_empty"));
                }

                if (triggerResult.getCode() == ReturnT.SUCCESS_CODE) {
                    // 4.1、trigger-param
                    TriggerParam triggerParam = new TriggerParam();
                    triggerParam.setJobId(jobInfo.getId());
                    triggerParam.setExecutorHandler(jobInfo.getExecutorHandler());
                    triggerParam.setExecutorParams(jobInfo.getExecutorParam());
                    triggerParam.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
                    triggerParam.setLogId(jobLog.getId());
                    triggerParam.setLogDateTim(jobLog.getTriggerTime().getTime());
                    triggerParam.setGlueType(jobInfo.getGlueType());
                    triggerParam.setGlueSource(jobInfo.getGlueSource());
                    triggerParam.setGlueUpdatetime(jobInfo.getGlueUpdatetime().getTime());
                    // 设置分片标记
                    triggerParam.setBroadcastIndex(i);
                    // 设置分片总数
                    triggerParam.setBroadcastTotal(addressList.size()); // update02

                    // 4.2、trigger-run (route run / trigger remote executor)
                    // 根据参数以及 机器地址，向执行器发送执行信息 ， 此处将会详细讲解runExecutor 这个方法
                    triggerResult = runExecutor(triggerParam, address);     // update03
                    triggerMsgSb.append("<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>"+ I18nUtil.getString("jobconf_trigger_run") +"<<<<<<<<<<< </span><br>").append(triggerResult.getMsg());

                    // 4.3、trigger (fail retry) 失败策略是重试
                    if (triggerResult.getCode()!=ReturnT.SUCCESS_CODE && failStrategy == ExecutorFailStrategyEnum.FAIL_RETRY) {
                        triggerResult = runExecutor(triggerParam, address);  // update04
                        triggerMsgSb.append("<br><br><span style=\"color:#F39C12;\" > >>>>>>>>>>>"+ I18nUtil.getString("jobconf_trigger_fail_retry") +"<<<<<<<<<<< </span><br>").append(triggerResult.getMsg());
                    }
                }

                // 5、save trigger-info
                jobLog.setExecutorAddress(triggerResult.getContent());
                jobLog.setTriggerCode(triggerResult.getCode());
                jobLog.setTriggerMsg(triggerMsgSb.toString());
                XxlJobDynamicScheduler.xxlJobLogDao.updateTriggerInfo(jobLog);

                // 6、monitor trigger
                // 将日志ID，放入队列，便于日志监控线程来监控任务的执行状态
                JobFailMonitorHelper.monitor(jobLog.getId());
                logger.debug(">>>>>>>>>>> xxl-job trigger end, jobId:{}", jobLog.getId());

            }
        } else {
            // 除分片模式外，其他的路由策略均走这里
            // 1、save log-id
            XxlJobLog jobLog = new XxlJobLog();
            jobLog.setJobGroup(jobInfo.getJobGroup());
            jobLog.setJobId(jobInfo.getId());
            XxlJobDynamicScheduler.xxlJobLogDao.save(jobLog);
            logger.debug(">>>>>>>>>>> xxl-job trigger start, jobId:{}", jobLog.getId());

            // 2、prepare trigger-info
            //jobLog.setExecutorAddress(executorAddress);
            jobLog.setGlueType(jobInfo.getGlueType());
            jobLog.setExecutorHandler(jobInfo.getExecutorHandler());
            jobLog.setExecutorParam(jobInfo.getExecutorParam());
            jobLog.setTriggerTime(new Date());

            ReturnT<String> triggerResult = new ReturnT<String>(null);
            StringBuffer triggerMsgSb = new StringBuffer();
            triggerMsgSb.append(I18nUtil.getString("jobconf_trigger_admin_adress")).append("：").append(IpUtil.getIp());
            triggerMsgSb.append("<br>").append(I18nUtil.getString("jobconf_trigger_exe_regtype")).append("：")
                    .append( (group.getAddressType() == 0)?I18nUtil.getString("jobgroup_field_addressType_0"):I18nUtil.getString("jobgroup_field_addressType_1") );
            triggerMsgSb.append("<br>").append(I18nUtil.getString("jobconf_trigger_exe_regaddress")).append("：").append(group.getRegistryList());
            triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_executorRouteStrategy")).append("：").append(executorRouteStrategyEnum.getTitle());
            triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_executorBlockStrategy")).append("：").append(blockStrategy.getTitle());
            triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_executorFailStrategy")).append("：").append(failStrategy.getTitle());

            // 3、trigger-valid
            if (triggerResult.getCode()==ReturnT.SUCCESS_CODE && CollectionUtils.isEmpty(addressList)) {
                triggerResult.setCode(ReturnT.FAIL_CODE);
                triggerMsgSb.append("<br>----------------------<br>").append(I18nUtil.getString("jobconf_trigger_address_empty"));
            }

            if (triggerResult.getCode() == ReturnT.SUCCESS_CODE) {
                // 4.1、trigger-param
                TriggerParam triggerParam = new TriggerParam();
                triggerParam.setJobId(jobInfo.getId());
                triggerParam.setExecutorHandler(jobInfo.getExecutorHandler());
                triggerParam.setExecutorParams(jobInfo.getExecutorParam());
                triggerParam.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
                triggerParam.setLogId(jobLog.getId());
                triggerParam.setLogDateTim(jobLog.getTriggerTime().getTime());
                triggerParam.setGlueType(jobInfo.getGlueType());
                triggerParam.setGlueSource(jobInfo.getGlueSource());
                triggerParam.setGlueUpdatetime(jobInfo.getGlueUpdatetime().getTime());
                triggerParam.setBroadcastIndex(0);//默认分片标记为0
                triggerParam.setBroadcastTotal(1);// 默认分片总数为1

                // 4.2、trigger-run (route run / trigger remote executor)
                // 此处使用了策略模式， 根据不同的策略 使用不同的实现类，此处不再详细说明
                triggerResult = executorRouteStrategyEnum.getRouter().routeRun(triggerParam, addressList);
                triggerMsgSb.append("<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>"+ I18nUtil.getString("jobconf_trigger_run") +"<<<<<<<<<<< </span><br>").append(triggerResult.getMsg());

                // 4.3、trigger (fail retry)
                // 重试机制
                if (triggerResult.getCode()!=ReturnT.SUCCESS_CODE && failStrategy == ExecutorFailStrategyEnum.FAIL_RETRY) {
                    triggerResult = executorRouteStrategyEnum.getRouter().routeRun(triggerParam, addressList);
                    triggerMsgSb.append("<br><br><span style=\"color:#F39C12;\" > >>>>>>>>>>>"+ I18nUtil.getString("jobconf_trigger_fail_retry") +"<<<<<<<<<<< </span><br>").append(triggerResult.getMsg());
                }
            }

            // 5、save trigger-info
            jobLog.setExecutorAddress(triggerResult.getContent());
            jobLog.setTriggerCode(triggerResult.getCode());
            jobLog.setTriggerMsg(triggerMsgSb.toString());
            XxlJobDynamicScheduler.xxlJobLogDao.updateTriggerInfo(jobLog);

            // 6、monitor trigger
            JobFailMonitorHelper.monitor(jobLog.getId());
            logger.debug(">>>>>>>>>>> xxl-job trigger end, jobId:{}", jobLog.getId());
        }

    }

    /**
     * run executor 向执行器发送指令都是从这个方法中执行的
     * @param triggerParam
     * @param address
     * @return  ReturnT.content: final address
     */
    public static ReturnT<String> runExecutor(TriggerParam triggerParam, String address){
        ReturnT<String> runResult = null;
        try {
            //创建一个ExcutorBiz 的对象，重点在这个方法里面
            ExecutorBiz executorBiz = XxlJobDynamicScheduler.getExecutorBiz(address);
            // 这个run 方法不会最终执行，仅仅只是为了触发 proxy object 的 invoke方法，同时将目标的类型传送给服务端， 因为在代理对象的invoke的方法里面没有执行目标对象的方法
            runResult = executorBiz.run(triggerParam);
        } catch (Exception e) {
            logger.error(">>>>>>>>>>> xxl-job trigger error, please check if the executor[{}] is running.", address, e);
            runResult = new ReturnT<String>(ReturnT.FAIL_CODE, ""+e );
        }

        StringBuffer runResultSB = new StringBuffer(I18nUtil.getString("jobconf_trigger_run") + "：");
        runResultSB.append("<br>address：").append(address);
        runResultSB.append("<br>code：").append(runResult.getCode());
        runResultSB.append("<br>msg：").append(runResult.getMsg());

        runResult.setMsg(runResultSB.toString());
        runResult.setContent(address);
        return runResult;
    }

}
