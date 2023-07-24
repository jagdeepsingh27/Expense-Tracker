package com.jcoding.expensetracker.test

object ExampleTest {
    private val existingUsername = listOf("Apple","Orange","Mango")
    /**
     * the input is not valid if
     * ----the username/password is empty
     * ----the username is already taken
     * ----the confirm password is nto the same as the real password
     * ----the password contains less than 2 digits
     */
    fun validateRegistrationInput(
        username :String,
        password : String,
        confirmPassword :String
    ):Boolean{
        if(username.isEmpty() || password.isEmpty()){
            return false
        }
        if(username in existingUsername){
            return false
        }
        if(password != confirmPassword){
            return false
        }
        if(password.count { it.isDigit() } < 2){
            return false
        }
        return true
    }
}