# Getting Started Authenticator

An example authenticator for training purposes, to explain core behaviors when you implement custom authentication.

## Core Behaviors

The authenticator demonstrates the initial areas you need to understand when getting started with plugins:

- Exposing HTTP endpoints for getting forms and receiving form data
- Frontend development to create both HTML and native forms
- Backend processing to receive form data, validate it and return error messages
- Backend identity logic and the use of SDK objects

The [Implement Custom Authentication using Plugins](resources/learn/getting-started-authentication-plugins/) tutorial explains the code and concepts.\
Once this plugin is understood, you are well-placed to implement many other custom authentication use cases.

## Prerequisites

First ensure that you have these prerequisites:

- Java 21 SDK
- Maven
- Docker
- An [ngrok setup](https://curity.io/resources/learn/expose-local-curity-ngrok/)
- Copy a license.json file for the Curity Identity Server to the root folder

To run the plugin in native mode you also need a mobile development environment:

- Android Studio
- Xcode

## Deploy the Authorization Server and Plugin

Run the following script to deploy the server components:

```bash
./deploy.sh
```

This will give you an external base URL such as the following:

```text
https://aa96-86-189-132-17.ngrok-free.app
```

Re-run the `deploy.sh` script whenever you change the plugin's code.

## Test a Browser-Based Flow using HTML Forms

Follow the [OAuth tools website tutorial](https://curity.io/resources/learn/test-using-oauth-tools/) to enable a test setup.\
Create an environment using the ngrok base URL, then add a code flow with the following details:

- Client ID: demo-web-client
- Client Secret: Password1
- Scope: openid
- Prompt: login

Run the code flow to step through the authenticator's screens.

## Test a Hypermedia Authentication API using Native Forms

Clone the HAAPI code examples with these commands:

```bash
git clone https://github.com/curityio/android-haapi-ui-sdk-demo
git clone https://github.com/curityio/ios-haapi-ui-sdk-demo
```

Edit both of these files and change the base URL to the ngrok value, then run the apps:

- For Android, the configuration file is at `app/src/main/java/io/curity/haapidemo/Configuration.kt`.
- For iOS, the configuration file is at `iOS/Configuration.swift`.

## Test Custom Logins

An example user is created at the end of the [deployment data script](./resources/data-backup.sql).\
The user account includes custom attributes, and the plugin shows how to include them in authentication:

- Account ID: 12345678
- Password: Password1
- Social Security Number: AB11223344
- Date of Birth: 1990/06/30

The example plugin uses a wizard-based approach to present multiple forms, so that you understand navigation.\
First, the user enters an account ID and password:

![Wizard form 1](images/wizard-form-1.jpg)

Next, the user provides their social security number and date of birth as further proofs of identity:

![Wizard form 2](images/wizard-form-2.jpg)

Finally, there is an authentication success screen:

![Wizard form 3](images/wizard-form-3.jpg)

## Designing Authentication

There are often better ways to implement authentication than a wizard-based approach.\
For example, you can compose authenticators and authentication actions to reduce work.\
Yet the example plugin demonstrates that you have full control over the important areas:

- Forms, form elements and user experience
- Data, validation and identity logic

## Further information

- Read the [Authentication Overview](https://curity.io/resources/learn/authentication-overview/) to learn more about authentication capabilities
- Visit [curity.io](https://curity.io/) for more information about the Curity Identity Server.
