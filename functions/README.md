## LNMO api callback URL with firebase functions
Implementation of a callback URL with google cloud functions  
### Endpoint:  
``
http://127.0.0.1:5001/test-cloud-functions-79991/us-central1/api/mpesa
``  
### Allowed methods: POST 
Expected response format:  
``
{
    success: true|false,
    message: "Message from cloud function"
}
``
