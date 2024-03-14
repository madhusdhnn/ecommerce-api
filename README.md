# ECOMMERCE APPLICATION

## Authentication
Authenticate users with Amazon Cognito. Run the below command in your terminal.

Export the following env vars before running the command.
1. `AWS_PROFILE` - name of your AWS account
2. `AWS_REGION` - The AWS region
3. `COGNITO_APP_CLIENT_ID` - App client ID in Cognito user pool

```shell
./scripts/cognito_auth.sh john.doe@example.com John.Doe@123
```

Copy the `idToken` that you get from Cognito and send it in `Authorization` header by prepending the word `Bearer`.

## Seeding
To seed the local database use the below command in your terminal.

```shell
 psql -U postgres -d ecommerce_db_local --single-transaction < ./scripts/seed.sql
```