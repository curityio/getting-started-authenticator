package com.mycompany.plugins.exampleauthenticator.authentication

import com.mycompany.plugins.exampleauthenticator.config.ExampleAuthenticatorPluginConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import se.curity.identityserver.sdk.attribute.SubjectAttributes
import se.curity.identityserver.sdk.authentication.AuthenticationResult
import se.curity.identityserver.sdk.authentication.AuthenticatorRequestHandler
import se.curity.identityserver.sdk.http.HttpStatus
import se.curity.identityserver.sdk.http.RedirectStatusCode
import se.curity.identityserver.sdk.service.credential.CredentialVerificationResult
import se.curity.identityserver.sdk.web.Request
import se.curity.identityserver.sdk.web.Response
import se.curity.identityserver.sdk.web.Response.ResponseModelScope
import se.curity.identityserver.sdk.web.ResponseModel.templateResponseModel
import se.curity.identityserver.sdk.web.alerts.ErrorMessage
import java.util.*

/*
 * Screen 1 of the wizard based authenticator
 */
class UserCredentialsRequestHandler(private val _config: ExampleAuthenticatorPluginConfig) :
    AuthenticatorRequestHandler<UserCredentialsRequestModel> {

    /*
     * The preProcess handler indicates the HTML forms to render on success or failure
     */
    override fun preProcess(request: Request, response: Response): UserCredentialsRequestModel
    {
        val data = HashMap<String, Any>(2)

        response.setResponseModel(
            templateResponseModel(
                data,
                "authenticate/usercredentials"
            ), ResponseModelScope.NOT_FAILURE
        )

        response.setResponseModel(
            templateResponseModel(
                data,
                "authenticate/usercredentials"
            ), HttpStatus.BAD_REQUEST
        )

        return UserCredentialsRequestModel(request)
    }

    /*
     * The get handler can run auto-actions or update the display if needed
     */
    override fun get(requestModel: UserCredentialsRequestModel, response: Response): Optional<AuthenticationResult>
    {
        return Optional.empty()
    }

    /*
     * The post handler does the main proof verification
     */
    override fun post(requestModel: UserCredentialsRequestModel, response: Response): Optional<AuthenticationResult>
    {
        // Use an SDK object to validate the patient ID and password
        val model = requestModel.postRequestModel
        val subjectAttributes = SubjectAttributes.of(model?.patientId)
        val result = _config.getCredentialManager().verify(subjectAttributes, model?.password)

        if (result is CredentialVerificationResult.Accepted)
        {
            // Navigate to the next page of the authentication wizard
            throw _config.exceptionFactory.redirectException(
                "${_config.authenticatorInformationProvider.fullyQualifiedAuthenticationUri}/userdetails",
                RedirectStatusCode.MOVED_TEMPORARILY)
        }
        else
        {
            // If the deeper validation fails, post back data to avoid losing user input
            response.addErrorMessage(ErrorMessage.withMessage("validation.error.incorrect.credentials"))
            response.setResponseModel(
                templateResponseModel(model?.dataOnError(), "authenticate/usercredentials"),
                HttpStatus.BAD_REQUEST
            )
            return Optional.empty()
        }
    }

    /*
     * This is called if the validation annotations fail
     */
    override fun onRequestModelValidationFailure(
        request: Request,
        response: Response,
        errorMessages: Set<ErrorMessage?>?
    ) {

        // Post back data to avoid losing user input
        _logger.error("*** HIBERNATE VALIDATION FAILURE ***")
        if (request.isPostRequest) {
            val model = Post(request)
            response.putViewData("_postBack", model.dataOnError(), ResponseModelScope.FAILURE)
        }
    }

    /*
     * The logged can output messages during troubleshooting
     */
    companion object {
        private val _logger: Logger = LoggerFactory.getLogger(UserCredentialsRequestHandler::class.java)
    }
}
