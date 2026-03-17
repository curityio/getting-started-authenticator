package com.example.plugins.exampleauthenticator.authenticate.handlers

import com.example.plugins.exampleauthenticator.config.ExampleAuthenticatorPluginConfig
import com.example.plugins.exampleauthenticator.authenticate.models.UserDetailsPost
import com.example.plugins.exampleauthenticator.authenticate.models.UserDetailsRequestModel
import se.curity.identityserver.sdk.authentication.AuthenticationResult
import se.curity.identityserver.sdk.authentication.AuthenticatorRequestHandler
import se.curity.identityserver.sdk.http.RedirectStatusCode
import se.curity.identityserver.sdk.web.Request
import se.curity.identityserver.sdk.web.Response
import se.curity.identityserver.sdk.web.Response.ResponseModelScope
import se.curity.identityserver.sdk.web.ResponseModel.templateResponseModel
import se.curity.identityserver.sdk.web.alerts.ErrorMessage
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.curity.identityserver.sdk.attribute.AccountAttributes
import se.curity.identityserver.sdk.attribute.Attribute
import se.curity.identityserver.sdk.errors.ErrorCode
import se.curity.identityserver.sdk.haapi.ProblemContract
import se.curity.identityserver.sdk.web.ResponseModel
import java.util.*

/*
 * Form 2 of the authentication wizard requires the user to prove personal data
 */
class UserDetailsRequestHandler(private val _config: ExampleAuthenticatorPluginConfig) :
    AuthenticatorRequestHandler<UserDetailsRequestModel>
{
    /*
     * The preProcess method indicates the HTML forms to render on success or failure
     */
    override fun preProcess(request: Request, response: Response): UserDetailsRequestModel
    {
        response.setResponseModel(
            templateResponseModel(
                emptyMap(),
                "authenticate/userdetails"
            ), ResponseModelScope.ANY
        )

        return UserDetailsRequestModel(request)
    }

    /*
     * This handler requires no custom page load logic
     */
    override fun get(requestModel: UserDetailsRequestModel, response: Response): Optional<AuthenticationResult>
    {
        return Optional.empty()
    }

    /*
     * The post handler does the main proof verification after the basic model validation
     */
    override fun post(requestModel: UserDetailsRequestModel, response: Response): Optional<AuthenticationResult>
    {
        val model = requestModel.postRequestModel

        // Get the account ID from the session
        val accountId = _config.sessionManager.get("accountId")?.attributeValue?.value as String?
            ?: throw _config.exceptionFactory.badRequestException(
                ErrorCode.MISSING_PARAMETERS,
                "The user details form could not find a account ID in the session")

        // Validate input
        val user = _config.getAccountManager().getByUserName(accountId)
        if (validateSocialSecurityNumber(user, model, response) && validateDateOfBirth(user, model, response))
        {
            // On success, add a value to the session and navigate to the next form of the authentication wizard
            _config.sessionManager.put(Attribute.of("detailsVerified", true));
            throw _config.exceptionFactory.redirectException(
                "${_config.authenticatorInformationProvider.fullyQualifiedAuthenticationUri}/success",
                RedirectStatusCode.MOVED_TEMPORARILY
            )
        }

        // If the deeper validation fails, post back data to avoid HTML forms losing user input
        response.putViewData("_postBack", model?.dataOnError(), ResponseModelScope.FAILURE)

        // Ensure that HAAPI treats this as an invalid input error and does not dismiss the form
        val errorModel = ResponseModel.problemResponseModel(ProblemContract.Types.InvalidInput.TYPE)
        response.setResponseModel(errorModel, ResponseModelScope.FAILURE)

        return Optional.empty()
    }

    /*
     * When model validation fails, this method ensures that HTML forms maintain user input
     */
    override fun onRequestModelValidationFailure(
        request: Request,
        response: Response,
        errorMessages: Set<ErrorMessage?>?
    ) {

        if (request.isPostRequest) {
            val model = UserDetailsPost(request)
            response.putViewData("_postBack", model.dataOnError(), ResponseModelScope.FAILURE)
        }
    }

    private fun validateSocialSecurityNumber(user: AccountAttributes?, model: UserDetailsPost?, response: Response): Boolean
    {
        val accountSocialSecurityNumber = user?.get("socialSecurityNumber")?.attributeValue?.value
        if (accountSocialSecurityNumber != null)
        {
            if (model?.socialSecurityNumber == accountSocialSecurityNumber)
            {
                return true
            }
        }

        response.addErrorMessage(ErrorMessage.withMessage("validation.error.incorrect.social-security-number"))
        return false
    }

    private fun validateDateOfBirth(user: AccountAttributes?, model: UserDetailsPost?, response: Response): Boolean
    {
        val accountDateOfBirth = user?.get("dateOfBirth")?.attributeValue?.value
        if (accountDateOfBirth != null)
        {
            val enteredDateOfBirth = model?.dateOfBirth?.replace("/", "")
            if (enteredDateOfBirth == accountDateOfBirth)
            {
                return true
            }
        }

        response.addErrorMessage(ErrorMessage.withMessage("validation.error.incorrect.date-of-birth"))
        return false
    }

    /*
     * The logger can output messages during troubleshooting
     */
    companion object {
        private val _logger: Logger = LoggerFactory.getLogger(UserDetailsRequestHandler::class.java)
    }
}