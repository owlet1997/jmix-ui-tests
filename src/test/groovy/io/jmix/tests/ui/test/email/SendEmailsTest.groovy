package io.jmix.tests.ui.test.email

import com.codeborne.selenide.Selenide
import io.jmix.tests.JmixUiTestsApplication
import io.jmix.tests.extension.ChromeExtension
import io.jmix.tests.ui.extension.SpringBootExtension
import io.jmix.tests.ui.initializer.PostgreSQLContextInitializer
import io.jmix.tests.ui.screen.application.email.EmailHistory
import io.jmix.tests.ui.screen.application.email.EmailScreen
import io.jmix.tests.ui.screen.system.main.MainScreen
import io.jmix.tests.ui.test.datatools.BaseDatatoolsUiTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

import static io.jmix.masquerade.Selectors.$j
import static io.jmix.tests.ui.menu.Menus.EMAIL_HISTORY
import static io.jmix.tests.ui.menu.Menus.EMAIL_SCREEN

@ExtendWith([
        SpringBootExtension,
        ChromeExtension
])
@SpringBootTest(classes = JmixUiTestsApplication,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = ['jmix.liquibase.contexts=base'])
@ContextConfiguration(initializers = PostgreSQLContextInitializer)

class SendEmailsTest extends BaseDatatoolsUiTest {

    public static final String TEST_SYNC1 = 'TestSync1'
    public static final String TEST_ASYNC = 'TestAsync'
    public static final String TEST_ASYNC2 = 'TestAsync2'

    @Test
    void syncWithAttachment() {
        loginAsAdmin()

        $j(MainScreen).with {
            sideMenu.openItem(EMAIL_SCREEN)
        }

        $j(EmailScreen) with {
            setSubject(TEST_SYNC1)
            clickButton(sync) //shouldn't be exception
            Selenide.sleep(2000)
        }

        $j(MainScreen).with {
            sideMenu.openItem(EMAIL_HISTORY)
        }

        $j(EmailHistory).with {
            checkStatus(TEST_SYNC1, SENT)
            checkAttachment(ATTACHMENT, SENT)
            checkDeadlineField()
        }
    }

    @Test
    void AsyncSimple() {
        loginAsAdmin()

        $j(MainScreen).with {
            sideMenu.openItem(EMAIL_SCREEN)
        }

        $j(EmailScreen) with {
            setSubject(TEST_ASYNC)
            clickButton(async)
        }
        $j(MainScreen).with {
            sideMenu.openItem(EMAIL_HISTORY)
        }
        $j(EmailHistory).with {
            checkAttemptsOfEmail(TEST_ASYNC, QUEUE, '0')
        }
        $j(MainScreen).with {
            sideMenu.openItem(EMAIL_SCREEN)
        }
        $j(EmailScreen) with {
            setSubject(TEST_ASYNC)
            clickButton(send)
        }
        $j(MainScreen).with {
            sideMenu.openItem(EMAIL_HISTORY)
        }
        $j(EmailHistory).with {
            checkStatus(TEST_ASYNC, SENT)
        }
    }

    @Test
    void AsyncSchedule() {
        loginAsAdmin()

        $j(MainScreen).with {
            sideMenu.openItem(EMAIL_SCREEN)
        }

        $j(EmailScreen) with {
            setSubject(TEST_ASYNC2)
            clickButton(async)
        }

        $j(MainScreen).with {
            sideMenu.openItem(EMAIL_HISTORY)
        }

        $j(EmailHistory).with {
            checkAttemptsOfEmail(TEST_ASYNC2, QUEUE, '0')
            Selenide.sleep(60000)
            //refresh
            checkAttemptsOfEmail(TEST_ASYNC2, SENT, '1')
        }
    }

    @Test
    void ResendEmail() {
        loginAsAdmin()
        $j(MainScreen).with {
            sideMenu.openItem(EMAIL_SCREEN)
        }
        $j(EmailScreen) with {
            setSubject(TEST_SYNC1)
            clickButton(sync)
            Selenide.sleep(2000)
        }
        $j(MainScreen).with {
            sideMenu.openItem(EMAIL_HISTORY)
        }
        $j(EmailHistory).with {
            sendingMessageTable
            clickToRowForResendMail(TEST_SYNC1, SENT)
            resendEmailButtonClick()
            verifyEmailFields(ADDRESS, CC, BCC)
            resend()
            checkStatus(TEST_SYNC1, BCC)
        }
    }
}
