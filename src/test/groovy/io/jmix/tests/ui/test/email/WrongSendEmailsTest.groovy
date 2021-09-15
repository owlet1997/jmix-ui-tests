package io.jmix.tests.ui.test.email

import io.jmix.masquerade.component.Table
import io.jmix.tests.JmixUiTestsApplication
import io.jmix.tests.extension.ChromeExtension
import io.jmix.tests.ui.extension.SpringBootExtension
import io.jmix.tests.ui.initializer.PostgreSQLContextInitializer
import io.jmix.tests.ui.screen.application.email.EmailScreen
import io.jmix.tests.ui.screen.system.main.MainScreen
import io.jmix.tests.ui.test.datatools.BaseDatatoolsUiTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

import static com.codeborne.selenide.Condition.text
import static io.jmix.masquerade.Conditions.VISIBLE
import static io.jmix.masquerade.Selectors.$j
import static io.jmix.masquerade.Selectors.byText
import static io.jmix.tests.ui.menu.Menus.EMAIL_HISTORY
import static io.jmix.tests.ui.menu.Menus.EMAIL_SCREEN

@ExtendWith([
        SpringBootExtension,
        ChromeExtension
])
@SpringBootTest(classes = JmixUiTestsApplication,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        //Todo change email
        properties = ['jmix.email.fromAddress=test.ereer@mail.ru'])
@ContextConfiguration(initializers = PostgreSQLContextInitializer)

class WrongSendEmailsTest extends BaseDatatoolsUiTest {


    @Test
    void clickButtonSync() {
        loginAsAdmin()

        $j(MainScreen).with {
            sideMenu.openItem(EMAIL_SCREEN)
        }

        $j(EmailScreen) with {
            clickButton(sync) //shouldn't be exception
        }

        $j(MainScreen).with {
            sideMenu.openItem(EMAIL_HISTORY)
        }

        $j(Table.class, 'sendingMessageTable_composition').with {
            getRow(byText('test@mail.ru')).shouldHave(text('Not sent'))
            shouldBe(VISIBLE)
        }
    }
}
