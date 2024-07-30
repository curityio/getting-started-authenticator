# Getting Started Authenticator

An example authenticator plugin to teach the main foundational behaviors:

- Implementing Forms and Navigation between them
- Dealing with Input and Validation
- Managing State and using Custom Data

## Example Use Case

The authenticator follows a wizard that uses three screens to collect data, after which the user is authenticated:

IMAGES

## Valid Authentication Details

- Account ID: 12345678
- Password: Password1
- Social Security Number: AB11223344
- Date of Birth: 1990/06/30

## HTTP Endpoints and Handlers

- Each form has an HTTP endpoint and uses handlers
- Handlers manage data and validation
- Handlers use SDK objects to perform the deeper work

## Advanced Cases