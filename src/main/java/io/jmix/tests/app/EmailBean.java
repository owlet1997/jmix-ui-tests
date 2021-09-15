package io.jmix.tests.app;

import io.jmix.autoconfigure.email.job.EmailSendingJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class EmailBean {

    @Bean
    JobDetail emailSendingJob() {
        return JobBuilder.newJob()
                .ofType(EmailSendingJob.class)
                .storeDurably()
                .withIdentity("emailSending")
                .build();
    }

}