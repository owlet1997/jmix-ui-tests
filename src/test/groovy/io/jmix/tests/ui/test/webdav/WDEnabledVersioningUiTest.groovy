package io.jmix.tests.ui.test.webdav


import io.jmix.masquerade.component.Button
import io.jmix.tests.ui.screen.administration.webdav.WebDAVDocumentBrowse
import io.jmix.tests.ui.screen.application.wddisabledentity.WDDisabledEntityEditor
import io.jmix.tests.ui.screen.application.wdenabledentity.WDEnabledEntityBrowse
import io.jmix.tests.ui.screen.system.main.MainScreen
import io.jmix.tests.ui.test.BaseUiTest
import io.jmix.tests.ui.test.utils.UiHelper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

import static io.jmix.masquerade.Conditions.*
import static io.jmix.masquerade.Selectors.$j

class WDEnabledVersioningUiTest extends BaseUiTest implements UiHelper {

    public static final String WEBDAV_DOCUMENTS_TABLE_J_TEST_ID = "webdavDocumentsTable"
    public static final String WEBDAV_DOCUMENT_VERSIONS_TABLE_J_TEST_ID = "webdavDocumentVersionsTable"
    public static final String WEBDAV_ENABLED_VERSIONING_TABLE_J_TEST_ID = "enabledVersEntitiesTable"
    public static final String FILENAME = "helloworld.txt"
    public static final String SHOW_VERSION_BTN_J_TEST_ID = "showVersion"
    public static final String DOCUMENT_IS_NOT_LOCKED_NOTIFICATION_CAPTION = "The document is not locked"
    public static final String RESOURCES_PATH = "src/main/resources/"
    public static final String FILE_PATH = "src/main/resources/helloworld.txt"

    @BeforeEach
    void beforeEachTest() {
        loginAsAdmin()
        $j(MainScreen).openWDEnabledEntityBrowse()
    }

    static String getUniqueName(String baseString) {
        return baseString + getGeneratedString()
    }


    static void cleanTempFile(String fileNamePath) {
        File file = new File(fileNamePath)
        file.delete()
    }

    @Test
    @DisplayName("Creates an entity with WD file with enabled versioning")
    void createWDEnabledEntity() {
        def fileName = getUniqueName(FILENAME)
        def fileNamePath = RESOURCES_PATH + fileName

        $j(WDEnabledEntityBrowse).with {
            clickButton(createBtn)
        }

        $j(WDDisabledEntityEditor).with {
            fillTextField(nameField, "name1")
            uploadNewDocument(fileField, FILE_PATH, fileNamePath)
            clickButton(ok)
        }

        $j(WDEnabledEntityBrowse).with {
            checkRecordIsDisplayed("name1", WEBDAV_ENABLED_VERSIONING_TABLE_J_TEST_ID)
        }
        $j(MainScreen).openWebDAVDocumentBrowse()

        $j(WebDAVDocumentBrowse).with {
            checkRecordIsDisplayed(fileName, WEBDAV_DOCUMENTS_TABLE_J_TEST_ID)
            $j(Button, SHOW_VERSION_BTN_J_TEST_ID)
                    .shouldBe(VISIBLE, ENABLED)
                    .shouldHave(caption("v1"))
        }
        cleanTempFile(fileNamePath)
    }

    @Test
    @DisplayName("Creates an entity with WD file with enabled versioning")
    void uploadNewVersionWDEnabledEntity() {
        def fileName = getUniqueName(FILENAME)
        def fileNamePath = RESOURCES_PATH + fileName

        $j(WDEnabledEntityBrowse).with {
            clickButton(createBtn)
        }

        $j(WDDisabledEntityEditor).with {
            fillTextField(nameField, "name1")
            uploadNewDocument(fileField, FILE_PATH, fileNamePath)
            clickButton(ok)
        }

        $j(WDEnabledEntityBrowse).with {
            checkRecordIsDisplayed("name1", WEBDAV_ENABLED_VERSIONING_TABLE_J_TEST_ID)
            selectRowInTableByText("name1", WEBDAV_ENABLED_VERSIONING_TABLE_J_TEST_ID)
            clickButton(editBtn)
        }

        $j(WDDisabledEntityEditor).with {
            uploadNewDocument(fileField, FILE_PATH, fileNamePath)
            clickButton($j(Button, "optionDialog_yes"))
        }


        $j(MainScreen).openWebDAVDocumentBrowse()

        $j(WebDAVDocumentBrowse).with {
            checkRecordIsDisplayed(fileName, WEBDAV_DOCUMENTS_TABLE_J_TEST_ID)
            $j(Button,SHOW_VERSION_BTN_J_TEST_ID).shouldBe(VISIBLE,ENABLED).shouldHave(caption("v2"))
        }
        cleanTempFile(fileNamePath)
    }




}
