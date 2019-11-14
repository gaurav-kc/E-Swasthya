<?php

///// Author --- Gaurav Chaudhari */*/*/*/*/
/*This file does the work of communication with the firebase. The actual work of sending notification */
class Firebase{
    //Okay so, the firebase has some rules
    /*It understands 'to' and 'data'. The 'to' must contain the receiver details and 'data' contains the message.
    Since we have 4 ways to send notifications that is
    1. Send to everyone
    2. Send to all doctors
    3. Send to all patients
    4. Send to a particular device (doctor or patient)
    We can say that the 'to' can vary in 3 ways.
    1. All (global) which can be used for case 1
    2. Selected Reg_ids --> which can be used for case 2 and 3
    3. Single Reg_id --> which can be used for case 4.
    According to this now, we will make functions to handle those 3 cases. */

    public function sendToAll($to,$message){
      //deal with that topics thing. I have no idea why it's there. But it works
      $fields = array(
        'to' => '/topics/'.$to,
        'data' => $message,
      );
      return $this->sendPushNotification($fields);
    }

    public function sendToAGroup($reg_ids,$message){
      $fields = array(
        'to' => $reg_ids,
        'data' => $message,
      );
      return $this->sendPushNotification($fields);
    }

    public function sendToAnIndividual($to,$message){
      $fields = array(
        'to' => $to,
        'data' => $message,
      );
      return $this->sendPushNotification($fields);
    }

    //Now its time to define the function sendPushNotification()
    public function sendPushNotification($fields){
      //We need the key here. So import the key File
      require_once('firebase_key.php');
      $url = 'https://fcm.googleapis.com/fcm/send'; //this url need not to be changed
      //Defining the header is important as it has authorization and meta data
      $headers = array(
        'Authorization: key=' .FIREBASE_API_KEY,
        'Content-Type: application/json'
      );
      //thats it. Simple isn't it ?
      //Now comes CURL. CURL is a library that helps communication between your file and other servers (firebase in our case)
      //Open the connection
      $ch = curl_init();
      /*below is the code that defines rules for communication. Read documentation of
      * curl_setopt to know more about every option*/
      // Set the url, number of POST vars, POST data
      curl_setopt($ch, CURLOPT_URL, $url);
      curl_setopt($ch, CURLOPT_POST, true);
      curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
      curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

      // Disabling SSL Certificate support temporarly
      curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
      curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));

      // Execute post
        $result = curl_exec($ch);
        if ($result === FALSE) {
            die('Curl failed: ' . curl_error($ch));
        }

        // Close connection
        curl_close($ch);
        echo "<br>".$result;
        return $result;
    }
}
//bit difficult but finally done. :D
 ?>
