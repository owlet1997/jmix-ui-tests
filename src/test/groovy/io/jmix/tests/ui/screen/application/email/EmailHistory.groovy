package io.jmix.tests.ui.screen.application.email

import com.codeborne.selenide.Selenide
import io.jmix.masquerade.Wire
import io.jmix.masquerade.base.Composite
import io.jmix.masquerade.component.Button
import io.jmix.masquerade.component.Notification
import io.jmix.masquerade.component.Table
import io.jmix.masquerade.component.TextField

import static io.jmix.masquerade.Conditions.*
import static io.jmix.masquerade.Selectors.$j
import static io.jmix.masquerade.Selectors.byCells

class EmailHistory extends Composite<EmailHistory> {

    public static final String ADDRESS = "testadress@mail"
    public static final String CC = "testCc@mail"
    public static final String BCC = "testBcc@mail"
    public static final String QUEUE = 'Queue'
    public static final String SENT = 'Sent'
    public static final String ATTACHMENT = 'emailAttach.png;'

    @Wire
    Button resendEmailBtn
    @Wire(path = ["dialog_ResendMessage", "resendEmailBtn"])
    Button resend
    @Wire
    Table sendingMessageTable
    @Wire(path = ["dialog_ResendMessage", "emailTextField"])
    TextField emailTextField
    @Wire(path = ["dialog_ResendMessage", "ccTextField"])
    TextField ccTextField
    @Wire(path = ["dialog_ResendMessage", "bccTextField"])
    TextField bccTextField
    @Wire(path = ["fg", "attemptsMade"])
    TextField attemptsMade
    @Wire
    TextField datepart

    void resendEmailButtonClick() {
        resendEmailBtn.shouldBe(VISIBLE)
                .click()
    }

    void resend() {
        resend.shouldBe(VISIBLE)
                .click()
        Selenide.sleep(4000)
        $j(Notification.class)
                .shouldBe(VISIBLE)
                .shouldHave(caption("Sent!"))
    }

    void verifyEmailFields(String address, String cc, String bcc) {
        emailTextField
                .shouldBe(VISIBLE)
                .shouldHave(value(address))
        ccTextField
                .shouldBe(VISIBLE)
                .shouldHave(value(cc))
        bccTextField
                .shouldBe(VISIBLE)
                .shouldHave(value(bcc))
    }

    void checkStatus(String subject, String status) {
        sendingMessageTable
                .shouldBe(VISIBLE)
                .selectRow(byCells(subject, status))
                .shouldBe(VISIBLE)
    }

    void checkAttachment(String attachment, String status) {
        sendingMessageTable
                .shouldBe(VISIBLE)
                .selectRow(byCells(attachment, status))
                .shouldBe(VISIBLE)
    }

    void checkAttemptsOfEmail(String subject, String status, String attempts) {
        sendingMessageTable
                .shouldBe(VISIBLE)
                .selectRow((byCells(subject, status)))
                .shouldBe(VISIBLE)
        attemptsMade
                .shouldBe(VISIBLE)
                .shouldHave(value(attempts))
    }

    void clickToRowForResendMail(String subject, String status) {
        sendingMessageTable
                .shouldBe(VISIBLE)
                .selectRow((byCells(subject, status)))
                .shouldBe(VISIBLE)
                .click()
    }

    void checkDeadlineField (){
        sendingMessageTable
        .shouldBe(VISIBLE)
        datepart
        .shouldHave(value(null))
    }
}
