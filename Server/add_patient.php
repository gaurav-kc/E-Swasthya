<?php

/*This file creates an entry in the patient table.*/
/*Author --- Gaurav Chaudhari*/


$response = array();
//response is just an acknowledgment variable. Status and error messages can be passed through response
//we check for availability of all the elements
if(isset($_POST['patient_name']) && isset($_POST['patient_address']) && isset($_POST['patient_mobile']) &&
  isset($_POST['patient_dob']) && isset($_POST['patient_latitude']) &&
  isset($_POST['patient_longitude']) && isset($_POST['patient_email']) && isset($_POST['patient_fcmid'])){

    $name = $_POST['patient_name'];
    $address = $_POST['patient_address'];
    $mobile = $_POST['patient_mobile'];
    $dob = $_POST['patient_dob'];
    $doj = $_POST['patient_doj'];
    $nooftests = 0;
    $lat = $_POST['patient_latitude'];
    $lon = $_POST['patient_longitude'];
    $email = $_POST['patient_email'];
    $fcmid = $_POST['patient_fcmid'];
    echo $name .$address .$mobile. $dob. $doj. $nooftests. $lat. $lon. $email. $fcmid;
    require_once('dbconnect.php');
    $db = new dbconnect();
    $con = $db->connect();
    echo "Connnected";
    $result = mysqli_query($con,"INSERT INTO PATIENT(PAT_NAME,PAT_ADD,PAT_MOB,PAT_DOB,PAT_DOJ,PAT_NOOFTESTS,
      PAT_LON,PAT_LAT,PAT_EMAIL,PAT_FCMID) VALUES('$name','$address',$mobile,'$dob',CURRENT_TIMESTAMP,$nooftests,
        $lat,$lon,'$email','$fcmid');");
    if($result)
    {
      //successfully inserted
      $response["success"]=1;
      $response["message"]="Patient successfully registered";
      //encoding json response
      echo json_encode($response);

    }else{
      //failed to insert row
      $response["success"]=0;
      $response["message"]="An unexpected error occured";
      echo json_encode($response);
    }

  }else{
    //parameters are missing
    $response["success"]=0;
    $response["message"]="Incomplete information.Please try again";
    echo json_encode($response);
  }

?>
