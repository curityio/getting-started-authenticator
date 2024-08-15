# Getting Started Authenticator

[![Quality](https://img.shields.io/badge/quality-demo-red)](https://curity.io/resources/code-examples/status/)
[![Availability](https://img.shields.io/badge/availability-source-blue)](https://curity.io/resources/code-examples/status/)

An example authenticator plugin for training purposes, to explain authentication plugin core behaviors.

## Core Behaviors

The authenticator demonstrates the initial areas you need to understand when getting started with plugins:

- Exposing HTTP endpoints for login forms.
- Frontend development to create both HTML and native forms.
- Backend processing to receive form data, validate it and return error messages.
- Backend identity logic and the use of SDK objects.

The [Getting Started with Authentication using Plugins](https://curity.io/resources/learn/getting-started-authentication-plugins/) tutorial explains the code and core concepts.\
Once this plugin is understood, you are well-placed to implement many other custom authentication use cases.

## Prerequisites

To build the plugin you will need the following tools:

- Java 21 SDK
- Maven

If you want to run the example deployment included in this repository, you will also need:

- Docker,
- an ngrok setup (Follow the instructions from the **ngrok** part of [this tutorial](https://curity.io/resources/learn/expose-local-curity-ngrok/#ngrok) to configure ngrok on your machine.),
- curl,
- jq (https://jqlang.github.io/jq/download/),
- a license for the Curity Identity Server, such as a trial license.

You should put the license into a `license.json` file in the `deployment` directory. The license should grant access to the plugin SDK and the Hypermedia Authentication API. You can get a license from the [Curity Developer Portal](https://developer.curity.io/).

If you want to test the authenticator from a native app you will also need a mobile development environment:

- Android Studio, or
- Xcode

## Building the Plugin

To build the plugin run the following command:

```
mvn package
```

The command will create a plugin `jar` in the `target` directory.

## Installing the Plugin

To install the plugin, copy its `jar` file into `$IDSVR_HOME/usr/share/plugins/example-authenticator`. If you add any dependencies to the code make sure to also copy all their jars into that folder as well. In a production environment make sure to copy the plugin files into all instances of the Curity Identity Server.

If you're using Docker to start the Curity Identity Server, you can put the plugin files in the container with a mounted volume. Add the following option to your `docker run` command:

```bash
-v ./target/example-authenticator-1.0.0-SNAPSHOT.jar:/opt/idsvr/usr/share/plugins/example-authenticator/example-authenticator-1.0.0-SNAPSHOT.jar
```

You can also easily mount the plugin files when using `docker compose`. See the `docker-compose.yml` file to see how we do it in the example deployment.

## Running the Example

You can use the provided resources in the `deployment` directory to run the example setup. This will build the plugin code and start an instance of the Curity Identity Server. The instance will be configured with the authenticator plugin, relevant clients (also for HAAPI integration) and an example user account. With the example setup you can quickly test how the authenticator works using both browser-based clients and native applications.

### Deploy the Authorization Server and Plugin

Run the following script from the `deployment` folder, to deploy the server components:

```bash
./deploy.sh
```

You can inspect the example plugin's configuration settings using the Admin UI. In your browser, navigate to https://localhost:6749/admin and log in with `admin/Password1`. Then navigate to `System / Authentication Service / Authenticators / Example`.

Re-run the `deploy.sh` script whenever you change the plugin's code. Note that this will overwrite any configuration changes you make using the admin UI. If you want to keep the changes export configuration XML from the admin UI using the `Changes` -> `Download` menu option. Then put the downloaded XML in the `resources/curity-config.xml` file.

### Test a Browser-Based Flow Using HTML Forms

Follow the **Configuring a Workspace** part of the [OAuth Tools tutorial](https://curity.io/resources/learn/test-using-oauth-tools/#configuring-a-workspace) to configure OAuth Tools for the example setup.\
Then follow the **Test a Configured Flow** part of that tutorial and use the following details:

- Client ID: demo-web-client
- Client Secret: Password1
- Scope: openid
- Prompt: login (This ensures that you will see the login screen on every login attempt.)

Run the code flow to step through the authentication plugin's screens, as described [#below](#test-custom-logins).

### Test a Hypermedia Authentication API Flow Using Native Forms

You can use the Curity-provided demo mobile apps to test the authenticator using HAAPI.

For **Android** clone the following repo:

```bash
git clone https://github.com/curityio/android-haapi-ui-sdk-demo
```

Then edit the configuration file `app/src/main/java/io/curity/haapidemo/Configuration.kt` and change the base URL to the ngrok value. Next, run the app.

For **iOS** clone the following repo:

```bash
git clone https://github.com/curityio/ios-haapi-ui-sdk-demo
```

Then edit the configuration file `iOS/Configuration.swift` and change the base URL to the ngrok value. Next, run the app.

### Test Custom Logins

The example deployment includes a user account with custom attributes, and the plugin uses them during authentication. (The user account is defined at the end of the [database backup file](./example/resources/data-backup.sql)). The attributes are:

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

### Teardown

Once you are finished with testing the solution run the following command from the `deployment` folder:

```bash
docker compose down
```

Then stop `ngrok` to close the tunnel. On Linux/MacOS you can use this command to find the `ngrok` process ID: `ps aux | grep ngrok`, then close it using `kill`.

## Designing Custom Authentication

There are often better ways to implement custom authentication than a wizard-based approach.\
For example, you can compose [authenticators](https://curity.io/resources/learn/authentication-overview/) and [authentication actions](https://curity.io/resources/learn/control-authentication-using-actions/), to reduce work.\
Yet the example plugin demonstrates that you have full control over all the important areas:

- Forms, form elements and user experience
- Data, validation and identity logic

## More information

- Visit [curity.io](https://curity.io/) for more information about the Curity Identity Server.
