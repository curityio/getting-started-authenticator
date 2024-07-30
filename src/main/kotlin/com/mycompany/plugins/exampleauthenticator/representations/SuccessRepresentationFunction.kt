package com.mycompany.plugins.exampleauthenticator.representations

import se.curity.identityserver.sdk.haapi.*
import se.curity.identityserver.sdk.http.HttpMethod
import se.curity.identityserver.sdk.http.MediaType
import se.curity.identityserver.sdk.web.Representation
import java.net.URI
import java.util.*

/*
 * The HAAPI representation for form 3 of the authentication wizard
 */
class SuccessRepresentationFunction : RepresentationFunction {

    override fun apply(model: RepresentationModel, factory: RepresentationFactory): Representation {

        val postbackUrl = model.getString("_authUrl")

        return factory.newAuthenticationStep { step: AuthenticationStepConfigurator ->

            step.addFormAction(
                HaapiContract.Actions.Kinds.LOGIN,
                URI.create(postbackUrl),
                HttpMethod.POST,
                MediaType.X_WWW_FORM_URLENCODED,
                Message.ofKey("meta.title.success"),
                Message.ofKey("view.next")
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
