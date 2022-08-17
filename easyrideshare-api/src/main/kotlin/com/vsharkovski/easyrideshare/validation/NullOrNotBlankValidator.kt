package com.vsharkovski.easyrideshare.validation

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class NullOrNotBlankValidator : ConstraintValidator<NullOrNotBlank, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean =
        value == null || value.isNotBlank()
}