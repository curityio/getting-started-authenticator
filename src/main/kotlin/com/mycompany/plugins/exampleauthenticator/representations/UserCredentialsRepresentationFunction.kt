package com.mycompany.plugins.exampleauthenticator.representations

import se.curity.identityserver.sdk.haapi.*
import se.curity.identityserver.sdk.http.HttpMethod
import se.curity.identityserver.sdk.http.MediaType
import se.curity.identityserver.sdk.web.Representation
import java.net.URI
import java.util.*

class UserCredentialsRepresentationFunction : RepresentationFunction {

    override fun apply(model: RepresentationModel, factory: RepresentationFactory): Representation {

        val postbackUrl = model.getString("_authUrl")

        return factory.newAuthenticationStep { step: AuthenticationStepConfigurator ->

            step.addFormAction(
                HaapiContract.Actions.Kinds.LOGIN,
                URI.create(postbackUrl),
                HttpMethod.POST,
                MediaType.X_WWW_FORM_URLENCODED,
                Message.ofKey("meta.title.screen1"),
                Message.ofKey("view.next")
            ) { fields ->
                fields.addUsernameField("userName", Message.ofKey("view.username"), "")
                fields.addPasswordField("password", Message.ofKey("view.password"))
            }

            step.addLink(
                URI.create("$postbackUrl.second"),
                HaapiContract.Links.Relations.FORGOT_PASSWORD,
                Message.ofKey("view.forgot-password")
            )
        }
    }

    override fun applyError(
        representationModel: ErrorRepresentationModel?,
        factory: ProblemRepresentationFactory?
    ): Optional<out Representation?> {
        return super.applyError(representationModel, factory)
    }
}
