const functions = require("firebase-functions");
// firebase admin Sdk to access FireStore
const admin = require("firebase-admin");
admin.initializeApp();

const express = require("express");
const app = express();
app.use(express.json());
app.disable("x-powered-by");

app.post("/mpesa", (req, res) => {
  const body = req.body;
  // check if request was successful
  if (body.Body.stkCallback.ResultCode === 0) {

    // check if the user should get fined
    let spinDate = admin.database().ref("cycles");
    

    const data = {
      amount: body.Body.stkCallback.CallbackMetadata.Item[0].Value,
      mPesaReceiptNumber: body.Body.stkCallback.CallbackMetadata.Item[1].Value,
      transactionDate: body.Body.stkCallback.CallbackMetadata.Item[3].Value,
      phoneNumber: body.Body.stkCallback.CallbackMetadata.Item[4].Value,
    }
    // save data to Realtime Database
    admin.database().ref("payments").push(data)

    res.status(200).send({
      success: true,
      message: `${data.mPesaReceiptNumber} Confirmed. Payment of KES ${data.amount} by ${data.phoneNumber} received successfully`
    });
    return;
  }
  // if request was not successful
  res.status(400).send({
    success: false,
    message: "Unable to complete transaction: Details: "+body.Body.stkCallback.ResultDesc
  });

});

exports.api = functions.https.onRequest(app);