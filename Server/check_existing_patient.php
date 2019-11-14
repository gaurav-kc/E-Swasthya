<?php

//This file checks if the user is an existing user or NoRewindIterator//
    /*  <<<<<-----Author --- Gaurav Chaudhari --- >>>>>>
     *  In the android app when the app is installed, the app will check if the user is existing user or not.
     *  If not,then use the add_patient.php file to register the patient
     *  If yes, then use the update_token_patient.php file to update the token number at the server
     *  As, everytime app is installed, a new unique device specific fcm_id is generated
     */
     if(isset($_POST['patient_email']))
     {
        $email_id = $_POST['patient_email'];
        //connect to the database
        require_once('dbconnect.php');
        $db = new dbconnect();
        $con = $db->connect();
        $result = mysqli_query($con,"Select PAT_ID from PATIENT where PATIENT.PAT_EMAIL = '$email_id';");
        $no_of_rows=mysqli_num_rows($result);
        if($no_of_rows > 0)
        {
            echo "In true";
            return TRUE;
        }
        else {
            echo "In false";
            return FALSE;
        }

     }
     else {
       echo "Incomplete information ";
     }

 ?>
