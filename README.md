# ECOMMERCE APPLICATION

## Authentication
Authenticate users with Amazon Cognito. Run the below command in your terminal.

Export the following env vars before running the command.
1. `AWS_PROFILE` - The name of your AWS account
2. `AWS_REGION` - The AWS region
3. `COGNITO_APP_CLIENT_ID` - App client ID in Cognito user pool

```shell
./scripts/cognito_auth.sh <EMAIL/ USERNAME> <PASSWORD>
```

Copy the `idToken` that you get from Cognito and send it in the `Authorization` header by prepending the word `Bearer`.

## Database Setup

### Environment Variables

Set the following env vars in your current shell. If you are using IDE, configure them in `run configuration`.

```properties
DB_HOST=
DB_NAME=
DB_PASSWORD=
DB_PORT=
DB_USER=
```
### Migrations

This application uses Flyway as mirgation tool.

To create a new migration, use the script file `./scripts/create_migration.js`. 

For example: 
```shell
./scripts/create_migration.js <MIGRATION_NAME>
```

To create "Repeatable" migrations, just pass the `--repeatable` argument in the above command after the migration name

### Seeding
To seed the local database use the below command in your terminal.

```shell
 psql -U $DB_USER -d $DB_NAME --single-transaction < ./scripts/seed.sql
```

## API Documentation

Project is configured to use Swagger UI for API documentation. Visit the below URL.

`http://localhost:9092/ecommerce/swagger-ui/index.html`

## DevOps

Run the following commands to build and deploy the docker image to Amazon ECS

```shell
docker build  --no-cache -t thetechmaddy/ecommerce-api:latest .
```
```shell
aws ecr get-login-password --region $AWS_REGION --profile $AWS_PROFILE | \
docker login --username AWS --password-stdin 426464367463.dkr.ecr.us-east-1.amazonaws.com
```
```shell
docker tag thetechmaddy/ecommerce-api:latest \
426464367463.dkr.ecr.us-east-1.amazonaws.com/ecommerce-api-dev-repo:latest
```
```shell
docker push 426464367463.dkr.ecr.us-east-1.amazonaws.com/ecommerce-api-dev-repo:latest
```
