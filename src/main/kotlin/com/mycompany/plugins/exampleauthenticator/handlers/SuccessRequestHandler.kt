package com.mycompany.plugins.exampleauthenticator.handlers

import com.mycompany.plugins.exampleauthenticator.config.ExampleAuthenticatorPluginConfig
import se.curity.identityserver.sdk.authentication.AuthenticationResult
import se.curity.identityserver.sdk.authentication.AuthenticatorRequestHandler
import se.curity.identityserver.sdk.web.Request
import se.curity.identityserver.sdk.web.Response
import se.curity.identityserver.sdk.web.ResponseModel.templateResponseModel
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.curity.identityserver.sdk.errors.ErrorCode
import java.util.*

/*
 * Form 3 of the authentication wizard just displays a success message and sets the authenticated result
 */
class SuccessRequestHandler(private val _config: ExampleAuthenticatorPluginConfig) :
    AuthenticatorRequestHandler<SuccessRequestModel> {

    /*
     * The preProcess method indicates the HTML forms to render
     */
    override fun preProcess(request: Request, response: Response): SuccessRequestModel {

        response.setResponseModel(templateResponseModel(
            emptyMap(),
            "authenticate/success"), Response.ResponseModelScope.NOT_FAILURE)

        return SuccessRequestModel(request)
    }

    /*
     * This handler requires no custom page load logic
     */
    override fun get(requestModel: SuccessRequestModel, response: Response): Optional<AuthenticationResult> {

        return Optional.empty()
    }

    /*
     * The post handler sets the authentication result to complete authentication
     */
    override fun post(requestModel: SuccessRequestModel, response: Response): Optional<AuthenticationResult> {

        val accountId = _config.sessionManager.get("accountId")?.attributeValue?.value as String?
            ?: throw _config.exceptionFactory.badRequestException(
                ErrorCode.MISSING_PARAMETERS,
                "The success form could not find the account ID field n the session")

        val detailsVerified = _config.sessionManager.get("detailsVerified")?.attributeValue?.value as Boolean?
            ?: throw _config.exceptionFactory.badRequestException(
                ErrorCode.MISSING_PARAMETERS,
                "The success form could not find the detailsVerified field in the session")

        return Optional.of(AuthenticationResult(accountId))
    }

    /*
     * The logger can output messages during troubleshooting
     */
    companion object {
        private val _logger: Logger = LoggerFactory.getLogger(SuccessRequestHandler::class.java)
    }
}
