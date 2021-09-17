package io.jmix.tests.ui.screen.application.email

import io.jmix.masquerade.Wire
import io.jmix.masquerade.base.Composite
import io.jmix.masquerade.component.Button
import io.jmix.masquerade.component.TextField

import static io.jmix.masquerade.Conditions.VISIBLE

class EmailScreen extends Composite<EmailScreen> {
    @Wire
    Button sync

    @Wire
    Button async

    @Wire
    Button send

    @Wire
    TextField subject

    void setSubject(String subjectName) {
        subject.shouldBe(VISIBLE).setValue(subjectName)
    }
}
