package com.example.contactappv1.Utils

import android.content.Context
import android.util.Patterns
import android.widget.Toast

object ContactValidator {
    fun validateInfor(context: Context, name: String, phone: String, email: String): Boolean{
        var isValid = true
        var message = ""
        if(name.isBlank()){
            message = "Name is blank "
            isValid = false
        }
        else if(!phone.startsWith("0") || phone.length != 10) {
            message = "Invalid Phone number"
            isValid = false
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() && email != ""){
            message = "Invalid email"
            isValid = false
        }
        if(!isValid){
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
        return isValid
    }
}