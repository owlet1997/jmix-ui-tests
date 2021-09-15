package io.jmix.tests.screen.email;

import io.jmix.email.Emailer;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class EmailSendingJob implements Job {

    @Autowired
    private Emailer emailer;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        emailer.processQueuedEmails();
    }

    @Bean
    JobDetail emailSendingJob() {
        return JobBuilder.newJob()
                .ofType(io.jmix.autoconfigure.email.job.EmailSendingJob.class)
                .storeDurably()
                .withIdentity("emailSending")
                .build();
    }
}
