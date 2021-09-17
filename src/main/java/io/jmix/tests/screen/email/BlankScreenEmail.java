package io.jmix.tests.screen.email;

import io.jmix.core.Resources;
import io.jmix.email.*;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.TextField;
import io.jmix.ui.screen.Screen;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.io.InputStream;

@UiController("BlankScreenEmail")
@UiDescriptor("blank-screenEmail.xml")
public class BlankScreenEmail extends Screen {
    @Autowired
    private Emailer emailer;
    @Autowired
    private Resources resources;
    @Autowired
    private TextField subject;

    @Autowired
    private Environment environment;

    @Subscribe("sync")
    public void onSyncClick(Button.ClickEvent event) throws EmailException, IOException {
        InputStream resourceAsStream = resources.getResourceAsStream("emailAttach.png");
        byte[] bytes = IOUtils.toByteArray(resourceAsStream);
        EmailAttachment emailAtt = new EmailAttachment(bytes, "emailAttach.png", "icoId");
        EmailInfo emailInfo = EmailInfoBuilder.create("testadress@mail",
                subject.getRawValue(), "Email body").setFrom("jmix.email.fromAddress")
                .setBcc("testBcc@mail")
                .setCc("testCc@mail")
                .setAttachments(emailAtt)
                .build();

        emailer.sendEmail(emailInfo);
    }

    @Subscribe("async")
    public void onAsyncClick(Button.ClickEvent event) {
        EmailInfo emailInfo = EmailInfoBuilder.create("testadress@mail",
                        subject.getRawValue(), "Email body").setFrom("jmix.email.fromAddress")
                .setBcc("testBcc@mail")
                .setCc("testCc@mail")
                .build();
        emailer.sendEmailAsync(emailInfo);
    }

    @Subscribe("send")
    public void onSendClick(Button.ClickEvent event) throws EmailException {
        emailer.processQueuedEmails();
    }
}
