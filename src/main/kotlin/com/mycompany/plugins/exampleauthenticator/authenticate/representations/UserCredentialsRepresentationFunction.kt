package com.mycompany.plugins.exampleauthenticator.authenticate.representations

import se.curity.identityserver.sdk.haapi.*
import se.curity.identityserver.sdk.http.HttpMethod
import se.curity.identityserver.sdk.http.MediaType
import se.curity.identityserver.sdk.web.Representation
import java.net.URI

/*
 * The HAAPI representation for form 1 of the authentication wizard
 */
class UserCredentialsRepresentationFunction : RepresentationFunction {

    override fun apply(model: RepresentationModel, factory: RepresentationFactory): Representation {

        val postbackUrl = model.getString("_authUrl")
        val accountIdValue = ""

        return factory.newAuthenticationStep { step: AuthenticationStepConfigurator ->

            step.addFormAction(
                HaapiContract.Actions.Kinds.LOGIN,
                URI.create(postbackUrl),
                HttpMethod.POST,
                MediaType.X_WWW_FORM_URLENCODED,
                Message.ofKey("authenticator.example.authenticate.meta.title.usercredentials"),
                Message.ofKey("authenticator.example.authenticate.view.next")
            ) { fields ->
                fields.addUsernameField("accountId", Message.ofKey("authenticator.example.authenticate.view.accountid"), accountIdValue)
                fields.addPasswordField("password", Message.ofKey("authenticator.example.authenticate.view.password"))
            }
        }
    }
}
