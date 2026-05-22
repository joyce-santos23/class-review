#!/bin/bash

awslocal sqs create-queue \
  --queue-name critical-feedback

awslocal dynamodb create-table \
  --table-name Feedback \
  --attribute-definitions \
      AttributeName=id,AttributeType=S \
  --key-schema \
      AttributeName=id,KeyType=HASH \
  --billing-mode PAY_PER_REQUEST