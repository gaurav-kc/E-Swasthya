<?php

  /*  This file accpets the patient token and updates it
   *  The fcm token is updated when the app is uninstalled and installed again or installed for first time
   */
   if(isset($_POST['doctor_id']) && isset($_POST['fcm_id']))
   {
     $doctor_id = $_POST['doctor_id'];
     $fcm_id = $_POST['fcm_id'];
     //connect to the database
     require_once('dbconnect.php');
     $db = new dbconnect();
     $con = $db->connect();
     $result=mysqli_query($con,"UPDATE DOCTOR SET DOC_FCMID = '$fcm_id' WHERE DOCTOR.DOC_ID = $doctor_id");
     $newresult = mysqli_query($con,"SELECT DOC_NAME,DOC_FCMID FROM DOCTOR WHERE DOC_ID=$doctor_id");
     $row = mysqli_fetch_assoc($newresult);
     echo $row['DOC_NAME'];
     echo "<br>".$row['DOC_FCMID'];
   }else {
     echo "Missing parameters";
   }
?>
