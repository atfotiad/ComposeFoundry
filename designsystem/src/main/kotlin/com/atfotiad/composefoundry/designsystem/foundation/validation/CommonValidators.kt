package com.atfotiad.composefoundry.designsystem.foundation.validation

import com.atfotiad.composefoundry.designsystem.foundation.resources.UiText

class EmailValidator(private val errorMessage: String = "Invalid email address") : Validator<String> {
    override fun validate(value: String): ValidationResult {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()
        return if (emailRegex.matches(value)) ValidationResult.Valid
        else ValidationResult.Invalid(UiText.DynamicString(errorMessage))
    }
}

class RequiredValidator(private val errorMessage: String = "This field is required") : Validator<String> {
    override fun validate(value: String): ValidationResult {
        return if (value.isNotBlank()) ValidationResult.Valid
        else ValidationResult.Invalid(UiText.DynamicString(errorMessage))
    }
}

class MinLengthValidator(private val minLength: Int, private val errorMessage: String? = null) : Validator<String> {
    override fun validate(value: String): ValidationResult {
        return if (value.length >= minLength) ValidationResult.Valid
        else ValidationResult.Invalid(UiText.DynamicString(errorMessage ?: "Must be at least $minLength characters"))
    }
}
