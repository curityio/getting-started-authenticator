package com.mycompany.plugins.exampleauthenticator.config;

import se.curity.identityserver.sdk.config.Configuration
import se.curity.identityserver.sdk.config.annotation.Description
import se.curity.identityserver.sdk.service.AccountManager
import se.curity.identityserver.sdk.service.ExceptionFactory
import se.curity.identityserver.sdk.service.SessionManager
import se.curity.identityserver.sdk.service.authentication.AuthenticatorInformationProvider
import se.curity.identityserver.sdk.service.credential.UserCredentialManager

interface ExampleAuthenticatorPluginConfig : Configuration
{
    // You must configure an account manager against the plugin
    @Description("The Credential Manager used to verify the password")
    fun getCredentialManager(): UserCredentialManager

    // You must configure a credential manager against the plugin
    @Description("The Account Manager is used to fetch the account")
    fun getAccountManager(): AccountManager

    // The session manager saves data between forms
    val sessionManager: SessionManager

    // The authentication information provider is used to get full request paths
    val authenticatorInformationProvider: AuthenticatorInformationProvider

    // The exception factory is used to create exceptions
    val exceptionFactory: ExceptionFactory
}
