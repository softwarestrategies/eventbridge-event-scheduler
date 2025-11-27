# Eventbridge Event Scheduler

## AWS Setup in the Cloud

- Create a lambda function and note its ARN
- Create a EventBridge Rule with a target of the above lambda function.  Note its ARN too

## Environment Variables set where the application will run

- AWS_REGION
- AWS_LAMBDA_TARGET_FUNCTION_ARN
- AWS_EVENTBRIDGE_SCHEDULER_EXECUTION_ROLE_ARN

## AWS Setup in your local environment

In you ~/.aws/credentials file add the following:

[default]
aws_access_key_id=AKIA...
aws_secret_access_key=6wOJ...
