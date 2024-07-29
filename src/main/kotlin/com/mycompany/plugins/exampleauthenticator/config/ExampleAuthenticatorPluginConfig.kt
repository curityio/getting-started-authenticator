package com.mycompany.plugins.exampleauthenticator.config;

import se.curity.identityserver.sdk.config.Configuration
import se.curity.identityserver.sdk.config.annotation.Description
import se.curity.identityserver.sdk.service.AccountManager
import se.curity.identityserver.sdk.service.ExceptionFactory
import se.curity.identityserver.sdk.service.authentication.AuthenticatorInformationProvider
import se.curity.identityserver.sdk.service.credential.UserCredentialManager

interface ExampleAuthenticatorPluginConfig : Configuration
{
    @Description("The Credential Manager used to verify the password")
    fun getCredentialManager(): UserCredentialManager

    @Description("The Account Manager is used to fetch the account")
    fun getAccountManager(): AccountManager

    val exceptionFactory: ExceptionFactory
    val authenticatorInformationProvider: AuthenticatorInformationProvider
}
