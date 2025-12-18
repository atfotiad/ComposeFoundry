package com.atfotiad.composefoundry.designsystem.foundation.validation

import com.atfotiad.composefoundry.designsystem.foundation.resources.UiText

/**
 * The result of a validation check.
 */
sealed interface ValidationResult {
    data object Valid : ValidationResult
    data class Invalid(val error: UiText) : ValidationResult
}

/**
 * Interface for any validation logic (Email, Password, etc.)
 */
interface Validator<T> {
    fun validate(value: T): ValidationResult
}
