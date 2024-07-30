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

## Code Tasks

Start with web and then do HAAPI

### Phase 1

- Screen 2 request model
- Watermarks
- handler / post validation failures to maintain input
  Use session to communicate data
- Custom labels for screens 2 and 3
- Custom JavaScript to limit numeric values
- Custom JavaScript for date picker with third party dependency 
- Set server variable to describe password policy and act on that in JavaScript
- Custom image with green tick
- Run in UI Builder

### Phase 2

- Same behaviors in HAAPI

## Doc Tasks

Explain main concepts

### PreProcess Handlers

- Can return a get form or a post form
- Can supply data for autofill
- Can return different forms depending on the HTTP status

### Get Handlers

- Get handler usually just renders a form
- Get handler might run logic automatically, eg reset password and return null
- Get handler can update display based on properties of SDK objects

### Post Handlers

- Get form data and validate it 
- Set a response model and status code
- Put form data to response to maintain the user input
- Request model or deeper validation failure

## Other Behaviors

- Research on other plugins
- Polling / Waiting
- Links
- Other Input Elements