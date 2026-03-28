#!/bin/bash
awslocal sqs create-queue --queue-name nexuspay-user-registered-queue
echo "Fila SQS criada com sucesso!"
