<?php
    /*  send_notification_to_patient is a php file that sends notification to the patient's by it's id
    *   Author ---- Gaurav Chaudhari ---- /////
    *   Works only for sending notification to patient. A seperate file is made to handle notification to doctors
    */
    if(isset($_POST['pat_id']) && isset($_POST['title']) && isset($_POST['message']))
      {
        //variables initialization
        $pat_id = $_POST['pat_id'];
        $title = $_POST['title'];
        $message = $_POST['message'];
        //error reporting helps know firebase erros if any.
        error_reporting(-1);
        ini_set('display_errors', 'On');

        require_once('firebase.php');
        require_once('push.php');
        require_once('dbconnect.php');

        //create objects of files imported
        $firebase = new Firebase();
        $push = new Push();
        $db = new dbconnect();

        //connection with the database
        $con = $db->connect();
        $result = mysqli_query($con,"Select PAT_FCMID,PAT_NAME,PAT_ADD from PATIENT where PAT_ID=$pat_id;");

        //Since ID is unique, we need not put fetching data in while loop. We can access directly from result.
        $row = mysqli_fetch_assoc($result);
        $regId = $row['PAT_FCMID'];

        // payload is useful to send supplementary data to receiver
        /* As is this project it will be used to send request, id and other stuff */
        $payload = array();
        $payload['patient_name'] = 'Gaurav';
        $payload['symptom_string'] = '5566998864';

        //setting the parameters to be send via notification
        $push->setTitle($title);
        $push->setMessage($message);
        $push->setImageUrl('');
        $push->setIsBackground(FALSE);
        $push->setPayload($payload);

        $json = '';
        $response = '';
        //get data stored in push file and store it in a variable in json format
        $json = $push->getPush();
        //since this file intends to send notification to a single user, it uses sendToAnIndividual()
        $response = $firebase->sendToAnIndividual($regId, $json);

      }
      else
      {
        echo "Missing parameters";
      }
?>
