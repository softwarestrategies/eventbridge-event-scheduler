# ☁️Eventbridge Event Scheduler

## Overview

This is an example of how to use AWS EventBridge Scheduler for scheduled (or deferred) event processing.

It consists of an application that pushes events to EventBridge Scheduler and a web application setup to receive 
those events and process them based on their type.  

- NOTE: There's an assumption that a lambda function exists that can receive events from EventBridge and push them to the web application.

## AWS Setup in the Cloud

- Create a lambda function and note its ARN
- Create a EventBridge Rule with a target of the above lambda function.  Note its ARN too

## AWS Setup Locally (or where application will run)

### Environment Variables need to be set

- AWS_REGION
- AWS_LAMBDA_TARGET_FUNCTION_ARN
- AWS_EVENTBRIDGE_SCHEDULER_EXECUTION_ROLE_ARN

### AWS Credentials need to be set

In your ~/.aws/credentials file, add the following:
```
[default]
aws_access_key_id=AKIA...
aws_secret_access_key=6wOJ...
```