package com.example.utils;

import com.example.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ScheduleJobUtil implements Job {

    @Autowired
    private EmailService emailService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDetailMap = jobExecutionContext.getJobDetail().getJobDataMap();
        JobDataMap triggerMap = jobExecutionContext.getTrigger().getJobDataMap();
        JobDataMap mergeMap = jobExecutionContext.getMergedJobDataMap();
        String m_from = (String) jobDetailMap.getString("m_from");
        String m_to = (String) jobDetailMap.getString("m_to");
        String m_subject = (String) jobDetailMap.getString("m_subject");
        String m_content = (String) jobDetailMap.getString("m_content");
        emailService = (EmailService) SpringUtil.getBean("emailService");
        int i = emailService.emailInsert(m_from, m_to, m_subject, m_content, true);
        if (i == 1) {
            log.info("jibDetailMap================>" + jobDetailMap.getString("m_to"));
        } else {
            log.info("定时任务失败--------------------");
        }
    }
}
