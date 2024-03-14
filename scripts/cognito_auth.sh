#!/bin/bash
set -e

email=$1
password=$2

usage() {
  cat << EOF
  Usage: cognito_auth.sh <USERNAME> <PASSWORD>

  where
    USERNAME - your registered email
    PASSWORD - your password
EOF
exit 1;
}

usage_env_vars() {
  cat << EOF
  Following environment variables are not set in your terminal session
    1. AWS_PROFILE - name of your AWS account
    2. AWS_REGION - The AWS region
    3. COGNITO_APP_CLIENT_ID - App client ID in Cognito user pool

  For example: export AWS_REGION=us-east-1
EOF
exit 1;
}

login() {

  if [[ -z "$email" || -z "$password" ]]; then
    usage
  elif [[ -z "$AWS_PROFILE" || -z "$AWS_REGION" || -z "$COGNITO_APP_CLIENT_ID" ]]; then
    usage_env_vars
  fi

  aws --profile "$AWS_PROFILE" --region "$AWS_REGION" \
  cognito-idp initiate-auth --auth-flow USER_PASSWORD_AUTH --client-id "$COGNITO_APP_CLIENT_ID" \
  --auth-parameters USERNAME="$1",PASSWORD="$2"
}

trap 'login "$email", "$password"' ERR
trap 'login "$email", "$password"' 0