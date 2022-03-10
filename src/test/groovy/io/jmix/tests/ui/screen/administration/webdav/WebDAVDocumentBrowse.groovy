package io.jmix.tests.ui.screen.administration.webdav

import io.jmix.masquerade.Wire
import io.jmix.masquerade.base.Composite
import io.jmix.masquerade.component.Button
import io.jmix.masquerade.component.FileUploadField
import io.jmix.tests.ui.screen.administration.datatools.traits.TableActionsTrait
import org.testcontainers.shaded.org.apache.commons.io.FileUtils

class WebDAVDocumentBrowse extends Composite<WebDAVDocumentBrowse> implements TableActionsTrait {

    @Wire
    FileUploadField uploadBtn

    @Wire
    Button manageVersionsBtn

    @Wire
    Button removeBtn

    @Wire
    Button lockBtn

    void uploadNewDocument(String filePath, String newName) {

        File file = new File(filePath)
        File newFile = new File(newName)
        FileUtils.copyFile(file, newFile)

        uploadBtn.upload(newFile)
    }

}
