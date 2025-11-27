# Eventbridge Event Scheduler

## Overview

This is a simple application that will push a message to AWS EventBridge Scheduler with a json payload to be pushed
to a designated AWS Lambda function at a scheduled time.

## AWS Setup in the Cloud

- Create a lambda function and note its ARN
- Create a EventBridge Rule with a target of the above lambda function.  Note its ARN too

## Environment Variables set where the application will run

- AWS_REGION
- AWS_LAMBDA_TARGET_FUNCTION_ARN
- AWS_EVENTBRIDGE_SCHEDULER_EXECUTION_ROLE_ARN

## AWS Setup in your local environment

In you ~/.aws/credentials file add the following:
```bash
[default]
aws_access_key_id=AKIA...
aws_secret_access_key=6wOJ...
```