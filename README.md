# Eventbridge Event Scheduler

## AWS Setup

- Create a lambda function and note its ARN
- Create a EventBridge Rule with a target of the above lambda function.  Note its ARN too

## Environment Variables set where the application will run

- AWS_REGION
- AWS_LAMBDA_TARGET_FUNCTION_ARN
- AWS_EVENTBRIDGE_SCHEDULER_EXECUTION_ROLE_ARN
