package com.mycompany.plugins.exampleauthenticator.handlers

import com.mycompany.plugins.exampleauthenticator.config.ExampleAuthenticatorPluginConfig
import se.curity.identityserver.sdk.authentication.AuthenticationResult
import se.curity.identityserver.sdk.authentication.AuthenticatorRequestHandler
import se.curity.identityserver.sdk.http.HttpStatus
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
        val data = HashMap<String, Any>(2)

        response.setResponseModel(
            templateResponseModel(
                data,
                "authenticate/userdetails"
            ), ResponseModelScope.NOT_FAILURE
        )

        response.setResponseModel(
            templateResponseModel(
                data,
                "authenticate/userdetails"
            ), HttpStatus.BAD_REQUEST
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
        var validSocialSecurityNumber = false
        var validDateOfBirth = false

        // Get the patient ID from the session
        val patientId = _config.sessionManager.get("patientId")?.attributeValue?.value as String?
            ?: throw _config.exceptionFactory.badRequestException(
                ErrorCode.MISSING_PARAMETERS,
                "The user details form could not find a patient ID in the session")

        val user = _config.getAccountManager().getByUserName(patientId)
        if (user != null) {

            if (validateSocialSecurityNumber(user, model))
            {
                validSocialSecurityNumber = true
            }
            else
            {
                response.addErrorMessage(ErrorMessage.withMessage("validation.error.incorrect.social-security-number"))
            }

            if (validateDateOfBirth(user, model))
            {
                validDateOfBirth = true
            }
            else
            {
                response.addErrorMessage(ErrorMessage.withMessage("validation.error.incorrect.date-of-birth"))
            }
        }

        // On success, add a value to the session and navigate to the next form of the authentication wizard
        if (validSocialSecurityNumber && validDateOfBirth)
        {
            _config.sessionManager.put(Attribute.of("detailsVerified", true));
            throw _config.exceptionFactory.redirectException(
                "${_config.authenticatorInformationProvider.fullyQualifiedAuthenticationUri}/success",
                RedirectStatusCode.MOVED_TEMPORARILY
            )
        }

        // If the deeper validation fails, post back data to avoid losing user input
        response.setResponseModel(
            templateResponseModel(model?.dataOnError(), "authenticate/userdetails"),
            HttpStatus.BAD_REQUEST
        )
        return Optional.empty()
    }

    /*
     * When model validation fails, this method ensures that the user input is maintained
     */
    override fun onRequestModelValidationFailure(
        request: Request,
        response: Response,
        errorMessages: Set<ErrorMessage?>?
    ) {

        // Post back data to avoid losing user input
        if (request.isPostRequest) {
            val model = UserDetailsPost(request)
            response.putViewData("_postBack", model.dataOnError(), ResponseModelScope.FAILURE)
        }
    }

    private fun validateSocialSecurityNumber(user: AccountAttributes, model: UserDetailsPost?): Boolean
    {
        val accountSocialSecurityNumber = user.get("socialSecurityNumber")?.attributeValue?.value
        if (accountSocialSecurityNumber != null)
        {
            val enteredSocialSecurityNumber = model?.socialSecurityNumber
            _logger.error("*** SSN DETAILS {} {} ***", enteredSocialSecurityNumber, accountSocialSecurityNumber)
            if (enteredSocialSecurityNumber == accountSocialSecurityNumber)
            {
                _logger.error("*** Entered SSN is correct ***")
                return true
            }
        }

        return false
    }

    private fun validateDateOfBirth(user: AccountAttributes, model: UserDetailsPost?): Boolean
    {
        val accountDateOfBirth = user.get("dateOfBirth")?.attributeValue?.value
        if (accountDateOfBirth != null)
        {
            val enteredDateOfBirth = model?.dateOfBirth?.replace("/", "")
            _logger.error("*** DOB DETAILS {} {} ***", enteredDateOfBirth, accountDateOfBirth)
            if (enteredDateOfBirth == accountDateOfBirth)
            {
                return true
            }
        }

        return false
    }

    /*
     * The logger can output messages during troubleshooting
     */
    companion object {
        private val _logger: Logger = LoggerFactory.getLogger(UserDetailsRequestHandler::class.java)
    }
}