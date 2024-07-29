package com.mycompany.plugins.exampleauthenticator.authentication

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
import java.util.*

/*
 * Screen 2 of the wizard based authenticator requires the user to prove personal data
 */
class UserDetailsRequestHandler(private val _config: ExampleAuthenticatorPluginConfig) :
    AuthenticatorRequestHandler<UserDetailsRequestModel> {

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
    override fun get(requestModel: UserDetailsRequestModel, response: Response): Optional<AuthenticationResult> {
        return Optional.empty()
    }

    /*
     * The post handler does the main proof verification after the basic model validation
     */
    override fun post(requestModel: UserDetailsRequestModel, response: Response): Optional<AuthenticationResult> {

        val username = "12345678"
        val user = _config.getAccountManager().getByUserName(username)
        if (user != null) {

            val socialSecurityNumber = user.get("socialSecurityNumber")
            if (socialSecurityNumber != null) {
                _logger.error("*** Social Security Number: ${socialSecurityNumber.attributeValue.value}")
            }

            val dateOfBirth = user.get("dateOfBirth")
            if (dateOfBirth != null) {
                _logger.error("*** Date of Birth: ${dateOfBirth.attributeValue.value}")
            }
        }

        throw _config.exceptionFactory.redirectException(
            "${_config.authenticatorInformationProvider.fullyQualifiedAuthenticationUri}/success",
            RedirectStatusCode.MOVED_TEMPORARILY)
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
            val model = Post(request)
            response.putViewData("_postBack", model.dataOnError(), ResponseModelScope.FAILURE)
        }
    }

    /*
     * The logger can output messages during troubleshooting
     */
    companion object {
        private val _logger: Logger = LoggerFactory.getLogger(UserDetailsRequestHandler::class.java)
    }
}