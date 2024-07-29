package com.mycompany.plugins.exampleauthenticator.authentication

import com.mycompany.plugins.exampleauthenticator.config.ExampleAuthenticatorPluginConfig
import se.curity.identityserver.sdk.authentication.AuthenticationResult
import se.curity.identityserver.sdk.authentication.AuthenticatorRequestHandler
import se.curity.identityserver.sdk.web.Request
import se.curity.identityserver.sdk.web.Response
import se.curity.identityserver.sdk.web.ResponseModel.templateResponseModel
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*

class SuccessRequestHandler(private val _config: ExampleAuthenticatorPluginConfig) :
    AuthenticatorRequestHandler<SuccessRequestModel> {

    override fun preProcess(request: Request, response: Response): SuccessRequestModel {

        response.setResponseModel(templateResponseModel(
            emptyMap(),
            "authenticate/success"), Response.ResponseModelScope.NOT_FAILURE)

        return SuccessRequestModel(request)
    }

    override fun get(requestModel: SuccessRequestModel, response: Response): Optional<AuthenticationResult> {
        return Optional.empty()
    }

    override fun post(requestModel: SuccessRequestModel, response: Response): Optional<AuthenticationResult> {

        val username = "demouser"
        return Optional.of(AuthenticationResult(username))
    }

    companion object {
        private val _logger: Logger = LoggerFactory.getLogger(SuccessRequestHandler::class.java)
    }
}
