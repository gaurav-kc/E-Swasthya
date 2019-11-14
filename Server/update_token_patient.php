<?php

  /*  This file accpets the patient token and updates it
   *  The fcm token is updated when the app is uninstalled and installed again or installed for first time
   */
   if(isset($_POST['patient_id']) && isset($_POST['fcm_id']))
   {
     $patient_id = $_POST['patient_id'];
     $fcm_id = $_POST['fcm_id'];
     //connect to the database
     require_once('dbconnect.php');
     $db = new dbconnect();
     $con = $db->connect();
     $result=mysqli_query($con,"UPDATE PATIENT SET PAT_FCMID = '$fcm_id' WHERE PATIENT.PAT_ID = $patient_id");
     $newresult = mysqli_query($con,"SELECT PAT_NAME,PAT_FCMID FROM PATIENT WHERE PAT_ID=$patient_id");
     $row = mysqli_fetch_assoc($newresult);
     echo $row['PAT_NAME'];
     echo "<br>".$row['PAT_FCMID'];
   }else {
     echo "Missing parameters";
   }
?>
