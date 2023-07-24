package com.jcoding.expensetracker.test


import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage

import org.junit.Test

class ExampleTestTest {
    @Test
    fun emptyUsernameReturnFalse() {
        val result = ExampleTest.validateRegistrationInput(
            "",
            "apple",
            "apple"
        )
        assertThat(result).isFalse()
    }
    @Test
    fun validUsernameAndValidPasswordReturnTrue() {
        val result = ExampleTest.validateRegistrationInput(
            "delhi",
            "apple",
            "apple"
        )
        assertThat(result).isTrue()
    }
    @Test
    fun userNameAlreadyExistReturnFalse() {
        val result = ExampleTest.validateRegistrationInput(
            "orange",
            "apple",
            "apple"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun passwordHaveLessThan2DigitReturnFalse() {
        val result = ExampleTest.validateRegistrationInput(
            "orange",
            "apple",
            "apple"
        )
        assertThat(result).isFalse()
    }
}