package io.jmix.tests.app;

import org.quartz.CronScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class EmailTrigger {
    @Autowired
    private EmailBean emailBean;

    @Bean
    Trigger emailSendingTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(emailBean.emailSendingJob())
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("59 * * ? * * *"))
                .build();
    }
}