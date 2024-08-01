package com.mycompany.plugins.exampleauthenticator.authenticate.representations

import se.curity.identityserver.sdk.haapi.*
import se.curity.identityserver.sdk.http.HttpMethod
import se.curity.identityserver.sdk.http.MediaType
import se.curity.identityserver.sdk.web.Representation
import java.net.URI

/*
 * The HAAPI representation for form 3 of the authentication wizard
 */
class SuccessRepresentationFunction : RepresentationFunction {

    override fun apply(model: RepresentationModel, factory: RepresentationFactory): Representation {

        val baseUrl = model.getString("_authUrl")
        val postbackUrl = "$baseUrl/success"

        return factory.newAuthenticationStep { step: AuthenticationStepConfigurator ->

            step.addFormAction(
                HaapiContract.Actions.Kinds.CONTINUE,
                URI.create(postbackUrl),
                HttpMethod.POST,
                MediaType.X_WWW_FORM_URLENCODED,
                Message.ofKey("authenticator.example.authenticate.meta.title.success"),
                Message.ofKey("authenticator.example.authenticate.view.next")
            )
        }
    }
}
