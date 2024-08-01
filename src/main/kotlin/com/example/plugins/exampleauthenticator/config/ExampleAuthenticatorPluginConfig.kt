package com.example.plugins.exampleauthenticator.config;

import se.curity.identityserver.sdk.config.Configuration
import se.curity.identityserver.sdk.config.annotation.Description
import se.curity.identityserver.sdk.service.AccountManager
import se.curity.identityserver.sdk.service.ExceptionFactory
import se.curity.identityserver.sdk.service.SessionManager
import se.curity.identityserver.sdk.service.authentication.AuthenticatorInformationProvider
import se.curity.identityserver.sdk.service.credential.UserCredentialManager

@Description("An example wizard-based authenticator plugin")
interface ExampleAuthenticatorPluginConfig : Configuration
{
    // You must configure an account manager against the plugin
    @Description("The credential manager used to verify account IDs and passwords")
    fun getCredentialManager(): UserCredentialManager

    // You must configure a credential manager against the plugin
    @Description("The account manager used to fetch user details")
    fun getAccountManager(): AccountManager

    // The session manager saves data between forms
    val sessionManager: SessionManager

    // The authentication information provider is used to get full request paths
    val authenticatorInformationProvider: AuthenticatorInformationProvider

    // The exception factory is used to create exceptions
    val exceptionFactory: ExceptionFactory
}
