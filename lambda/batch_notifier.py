import boto3

QUEUE_URL = 'https://sqs.us-east-1.amazonaws.com/244976608449/dzmitry-karnyshou-uploads-notification-queue'
MAX_NUMBER_OF_MESSAGES = 2
WAIT_TIMEOUT = 2 # in seconds

MESSAGE_SEPARATOR = '\n-----------------------\n'

TOPIC_ARN = 'arn:aws:sns:us-east-1:244976608449:dzmitry-karnyshou-uploads-notification-topic'

sqs = boto3.client('sqs')
sns = boto3.client('sns')


def poll_messages():
    messages = []

    for _ in range(MAX_NUMBER_OF_MESSAGES):
        response = sqs.receive_message(
            QueueUrl=QUEUE_URL,
            MaxNumberOfMessages=MAX_NUMBER_OF_MESSAGES,
            WaitTimeSeconds=WAIT_TIMEOUT
        )

        if 'Messages' in response:
            for message in response['Messages']:
                messages.append(message)
                sqs.delete_message(
                    QueueUrl=QUEUE_URL,
                    ReceiptHandle=message['ReceiptHandle']
                )

    return messages

def extract_message_body(message):
    return message['Body']

def publish_notification(body):
    sns.publish(
        TopicArn=TOPIC_ARN,
        Message=body
    )

def lambda_handler(event, context):
    source = event['detail-type'] if 'detail-type' in event else 'API'
    print(f'Request source: {source}')

    message_bodies = [extract_message_body(m) for m in poll_messages()]
    messages_count = len(message_bodies)

    if len(message_bodies) != 0:
        notification_body = MESSAGE_SEPARATOR.join(message_bodies)
        publish_notification(notification_body)

    return {
        'statusCode': 200,
        'body': f'Processed {messages_count} messages'
    }
