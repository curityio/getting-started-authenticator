package com.mycompany.plugins.exampleauthenticator.handlers

import com.mycompany.plugins.exampleauthenticator.config.ExampleAuthenticatorPluginConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import se.curity.identityserver.sdk.attribute.Attribute
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
 * Form 1 of the authentication wizard verifies a username and password
 */
class UserCredentialsRequestHandler(private val _config: ExampleAuthenticatorPluginConfig) :
    AuthenticatorRequestHandler<UserCredentialsRequestModel>
{

    /*
     * The preProcess method indicates the HTML forms to render on success or failure
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
     * This handler requires no custom page load logic
     */
    override fun get(requestModel: UserCredentialsRequestModel, response: Response): Optional<AuthenticationResult>
    {
        return Optional.empty()
    }

    /*
     * The post handler does the main proof verification after the basic model validation
     */
    override fun post(requestModel: UserCredentialsRequestModel, response: Response): Optional<AuthenticationResult>
    {
        // Use an SDK object to validate the account ID and password
        val model = requestModel.postRequestModel
        val subjectAttributes = SubjectAttributes.of(model?.accountId)
        val result = _config.getCredentialManager().verify(subjectAttributes, model?.password)

        if (result is CredentialVerificationResult.Accepted)
        {
            // On success, save the account ID to the session and navigate to the next form of the authentication wizard
            _config.sessionManager.put(Attribute.of("accountId", model?.accountId));
            throw _config.exceptionFactory.redirectException(
                "${_config.authenticatorInformationProvider.fullyQualifiedAuthenticationUri}/userdetails",
                RedirectStatusCode.MOVED_TEMPORARILY)
        }

        // If the deeper validation fails, post back data to avoid losing user input
        response.addErrorMessage(ErrorMessage.withMessage("validation.error.incorrect.credentials"))
        response.putViewData("_postBack", model?.dataOnError(), ResponseModelScope.FAILURE)
        return Optional.empty()
    }

    /*
     * When model validation fails, this method ensures that the user input is maintained
     */
    override fun onRequestModelValidationFailure(
        request: Request,
        response: Response,
        errorMessages: Set<ErrorMessage?>?
    )
    {
        // Post back data to avoid losing user input
        if (request.isPostRequest) {
            val model = UserCredentialsPost(request)
            response.putViewData("_postBack", model.dataOnError(), ResponseModelScope.FAILURE)
        }
    }

    /*
     * The logger can output messages during troubleshooting
     */
    companion object {
        private val _logger: Logger = LoggerFactory.getLogger(UserCredentialsRequestHandler::class.java)
    }
}
