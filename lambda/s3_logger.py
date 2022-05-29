def extract_image_name(event):
    return event['Records'][0]['s3']['object']['key']

def lambda_handler(event, context):
    print(f'Uploaded image: {extract_image_name(event)}')

    return {
        'statusCode': 200
    }