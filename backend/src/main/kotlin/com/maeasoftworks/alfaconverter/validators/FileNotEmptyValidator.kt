package com.maeasoftworks.alfaconverter.validators

import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass


@Constraint(validatedBy = [FileNotEmptyValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class FileNotEmpty(
	val message: String = "File or filename was empty.",
	val groups: Array<KClass<*>> = [],
	val payload: Array<KClass<out Payload>> = []
)

class FileNotEmptyValidator : ConstraintValidator<FileNotEmpty, MultipartFile> {
	override fun isValid(value: MultipartFile, context: ConstraintValidatorContext?): Boolean {
		return !(value.originalFilename == null || value.originalFilename == "") && try { value.bytes; true } catch (e: IOException) { false }
	}
}