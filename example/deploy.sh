#!/bin/bash

cd "$(dirname "${BASH_SOURCE[0]}")"

#
# Check for a license file
#
if [ ! -f ./license.json ]; then
  echo 'Please copy a license.json file into the root folder'
  exit 1
fi

#
# Prevent accidental check-ins of licenses
#
cp ./hooks/pre-commit ../.git/hooks

#
# Build the latest plugin
#
cd ..
mvn package
if [ $? -ne 0 ]; then
  echo 'Problem encountered building plugin code'
  exit 1
fi

#
# Copy the plugin jar
#
cp ./target/example-authenticator-1.0.0-SNAPSHOT.jar ./example/build/example-authenticator-1.0.0-SNAPSHOT.jar
cd example

#
# Clear the data folder
#
rm -rf data 2>/dev/null

#
# Start ngrok if needed
#
if [ "$(pgrep ngrok)" == '' ]; then
  ngrok http 8443 --log=stdout &
  sleep 5
fi
export BASE_URL=$(curl -s http://localhost:4040/api/tunnels | jq -r '.tunnels[] | select(.proto == "https") | .public_url')

#
# Deploy an instance of the Curity Identity Server
#
docker compose down
docker compose up
