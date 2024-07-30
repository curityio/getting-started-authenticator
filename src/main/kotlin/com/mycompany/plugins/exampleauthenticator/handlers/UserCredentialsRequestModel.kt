package com.mycompany.plugins.exampleauthenticator.handlers

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import se.curity.identityserver.sdk.web.Request

/*
 * The user credentials request handler has no special get logic so only needs a post model
 */
class UserCredentialsRequestModel(request: Request)
{
    @Valid
    val postRequestModel: UserCredentialsPost? = if (request.isPostRequest) UserCredentialsPost(request) else null
}

/*
 * The post model deals with validating patient ID and password inputs
 */
class UserCredentialsPost(request: Request)
{
    private val PATIENT_ID_PARAM = "patientId"
    private val PASSWORD_PARAM = "password"

    // In this example, the patient ID must be 8 numeric digits
    @Pattern(regexp = "^[0-9]{8}$", message = "validation.error.patientid.format")
    val patientId: String? = request.getFormParameterValueOrError(PATIENT_ID_PARAM)

    @NotBlank(message = "validation.error.password.required")
    val password: String? = request.getFormParameterValueOrError(PASSWORD_PARAM)

    // Maintain user input if there has been a validation error
    fun dataOnError(): Map<String, Any> {
        val data = HashMap<String, Any>(1)
        data[PATIENT_ID_PARAM] = patientId ?: ""
        return data
    }
}
