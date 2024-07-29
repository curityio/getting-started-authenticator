## Doc

- Frontend Development
  https://curity.io/docs/idsvr/latest/developer-guide/front-end-development/index.html

- Apache Velocity
  https://velocity.apache.org/

- Jakarta Validation
  https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/

- Plugins
  https://curity.io/docs/idsvr/latest/developer-guide/plugins/index.html

- HAAPI
  https://curity.io/docs/idsvr/latest/developer-guide/haapi/index.html

## Tasks

- Password validation failures to maintain input

- Screen 2 to complete
  Use session to get there, with username

- Screen 3 to implement

- Custom JavaScript to limit numeric values for form 1
 
- Set server variable to describe password policy and act on that in JavaScript

- Date of Birth in Date Picker

- Custom image with green tick
  See UI Builder for other icons and handling of images

## PreProcess Handlers

- Can return a get form or a post form
- Can supply data for autofill
- Can return different forms depending on the HTTP status

## Get Handlers

- Get handler usually just renders a form
- Get handler might run logic automatically, eg reset password and return null
- Get handler can update display based on properties of SDK objects

## Post Handlers

- Get form data and validate it 
- Set a response model and status code
- Put form data to response to maintain the user input
- Request model or deeper validation failure
