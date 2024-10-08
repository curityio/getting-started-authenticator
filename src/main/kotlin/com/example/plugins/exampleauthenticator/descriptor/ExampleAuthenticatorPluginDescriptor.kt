package com.example.plugins.exampleauthenticator.descriptor

import com.example.plugins.exampleauthenticator.authenticate.handlers.SuccessRequestHandler
import com.example.plugins.exampleauthenticator.authenticate.handlers.UserCredentialsRequestHandler
import com.example.plugins.exampleauthenticator.authenticate.handlers.UserDetailsRequestHandler
import com.example.plugins.exampleauthenticator.config.ExampleAuthenticatorPluginConfig
import com.example.plugins.exampleauthenticator.authenticate.representations.SuccessRepresentationFunction
import com.example.plugins.exampleauthenticator.authenticate.representations.UserCredentialsRepresentationFunction
import com.example.plugins.exampleauthenticator.authenticate.representations.UserDetailsRepresentationFunction
import se.curity.identityserver.sdk.authentication.AuthenticatorRequestHandler
import se.curity.identityserver.sdk.haapi.RepresentationFunction
import se.curity.identityserver.sdk.plugin.descriptor.AuthenticatorPluginDescriptor

class ExampleAuthenticatorPluginDescriptor : AuthenticatorPluginDescriptor<ExampleAuthenticatorPluginConfig>
{
    override fun getConfigurationType(): Class<out ExampleAuthenticatorPluginConfig> =
        ExampleAuthenticatorPluginConfig::class.java
    
    override fun getPluginImplementationType(): String = "example"

    override fun getAuthenticationRequestHandlerTypes(): Map<String, Class<out AuthenticatorRequestHandler<*>>> =

        mapOf(
            "index" to UserCredentialsRequestHandler::class.java,
            "userdetails" to UserDetailsRequestHandler::class.java,
            "success" to SuccessRequestHandler::class.java
        )

    override fun getRepresentationFunctions(): Map<String, Class<out RepresentationFunction?>> =

        mapOf(
            "authenticate/usercredentials" to UserCredentialsRepresentationFunction::class.java,
            "authenticate/userdetails" to UserDetailsRepresentationFunction::class.java,
            "authenticate/success" to SuccessRepresentationFunction::class.java,
        )
}
