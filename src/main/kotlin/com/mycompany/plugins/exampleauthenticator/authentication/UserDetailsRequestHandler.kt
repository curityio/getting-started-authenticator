package com.mycompany.plugins.exampleauthenticator.authentication

import com.mycompany.plugins.exampleauthenticator.config.ExampleAuthenticatorPluginConfig
import se.curity.identityserver.sdk.authentication.AuthenticationResult
import se.curity.identityserver.sdk.authentication.AuthenticatorRequestHandler
import se.curity.identityserver.sdk.web.Request
import se.curity.identityserver.sdk.web.Response
import se.curity.identityserver.sdk.web.ResponseModel.templateResponseModel
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.curity.identityserver.sdk.http.RedirectStatusCode
import java.util.*

/*
 * Screen 2 of the wizard based authenticator
 */
class UserDetailsRequestHandler(private val _config: ExampleAuthenticatorPluginConfig) :
    AuthenticatorRequestHandler<UserDetailsRequestModel> {

    override fun preProcess(request: Request, response: Response): UserDetailsRequestModel {

        response.setResponseModel(templateResponseModel(
            emptyMap(),
            "authenticate/userdetails"), Response.ResponseModelScope.NOT_FAILURE)

        return UserDetailsRequestModel(request)
    }

    override fun get(requestModel: UserDetailsRequestModel, response: Response): Optional<AuthenticationResult> {
        return Optional.empty()
    }

    override fun post(requestModel: UserDetailsRequestModel, response: Response): Optional<AuthenticationResult> {

        val username = "12345678"
        val user = _config.getAccountManager().getByUserName(username)
        if (user != null) {

            val socialSecurityNumber = user.get("socialSecurityNumber")
            if (socialSecurityNumber != null) {
                _logger.error("*** Social Security Number: ${socialSecurityNumber.attributeValue.value}")
            }
        }

        throw _config.exceptionFactory.redirectException(
            "${_config.authenticatorInformationProvider.fullyQualifiedAuthenticationUri}/success",
            RedirectStatusCode.MOVED_TEMPORARILY)
    }

    companion object {
        private val _logger: Logger = LoggerFactory.getLogger(UserDetailsRequestHandler::class.java)
    }
}