package com.mycompany.plugins.exampleauthenticator.descriptor

import com.mycompany.plugins.exampleauthenticator.handlers.SuccessRequestHandler
import com.mycompany.plugins.exampleauthenticator.handlers.UserCredentialsRequestHandler
import com.mycompany.plugins.exampleauthenticator.handlers.UserDetailsRequestHandler
import com.mycompany.plugins.exampleauthenticator.config.ExampleAuthenticatorPluginConfig
import com.mycompany.plugins.exampleauthenticator.representations.SuccessRepresentationFunction
import com.mycompany.plugins.exampleauthenticator.representations.UserCredentialsRepresentationFunction
import com.mycompany.plugins.exampleauthenticator.representations.UserDetailsRepresentationFunction
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
