package com.maeasoftworks.alfaconverter.validators

import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [IsXlsxValidator::class])
@Retention(AnnotationRetention.RUNTIME)
annotation class MustBeXlsx(
	val message: String = "File extension was not allowed",
	val groups: Array<KClass<*>> = [],
	val payload: Array<KClass<out Payload>> = []
)


class IsXlsxValidator : ConstraintValidator<MustBeXlsx, MultipartFile> {
	override fun isValid(value: MultipartFile, context: ConstraintValidatorContext): Boolean {
		return value.originalFilename?.split(".")?.last() == "xlsx"
	}
}