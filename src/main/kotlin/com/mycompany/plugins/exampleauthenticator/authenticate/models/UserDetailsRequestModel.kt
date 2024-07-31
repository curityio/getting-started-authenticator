package com.mycompany.plugins.exampleauthenticator.authenticate.models

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import se.curity.identityserver.sdk.web.Request

class UserDetailsRequestModel(request: Request)
{
    @Valid
    val postRequestModel: UserDetailsPost? = if (request.isPostRequest) UserDetailsPost(request) else null
}

/*
 * The post model deals with validating account ID and password inputs
 */
class UserDetailsPost(request: Request)
{
    private val SOCIAL_SECURITY_NUMBER_PARAM = "socialSecurityNumber"
    private val DATE_OF_BIRTH_PARAM = "dateOfBirth"

    // In this example, the social security number has a specific format
    @NotBlank(message = "validation.error.social-security-number.required")
    val socialSecurityNumber: String? = request.getFormParameterValueOrError(SOCIAL_SECURITY_NUMBER_PARAM)

    @Pattern(regexp = "^[0-9]{4}/[0-9]{2}/[0-9]{2}$", message = "validation.error.date-of-birth.format")
    val dateOfBirth: String? = request.getFormParameterValueOrError(DATE_OF_BIRTH_PARAM)

    // Maintain user input if there has been a validation error
    fun dataOnError(): Map<String, Any> {
        val data = HashMap<String, Any>(1)
        data[SOCIAL_SECURITY_NUMBER_PARAM] = socialSecurityNumber ?: ""
        data[DATE_OF_BIRTH_PARAM] = dateOfBirth ?: ""
        return data
    }
}
