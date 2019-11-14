<?php

/*This file creates an entry in the patient table.*/
/*Author --- Gaurav Chaudhari*/


$response = array();
//response is just an acknowledgment variable. Status and error messages can be passed through response
//we check for availability of all the elements
if(isset($_POST['doctor_name']) && isset($_POST['doctor_address']) && isset($_POST['doctor_mobile']) &&
  isset($_POST['doctor_dob']) && isset($_POST['doctor_latitude']) && isset($_POST['doctor_longitude']) &&
  isset($_POST['doctor_type']) && isset($_POST['doctor_email']) && isset($_POST['doctor_fcmid'])){

    $name = $_POST['doctor_name'];
    $address = $_POST['doctor_address'];
    $mobile = $_POST['doctor_mobile'];
    $dob = $_POST['doctor_dob'];
    $noofcusts = 0;
    $lat = $_POST['doctor_latitude'];
    $lon = $_POST['doctor_longitude'];
    $type = $_POST['doctor_type'];
    $email = $_POST['doctor_email'];
    $fcmid = $_POST['doctor_fcmid'];
    echo $name .$address .$mobile. $dob. $doj. $nooftests. $lat. $lon. $type. $email. $fcmid;
    require_once('dbconnect.php');
    $db = new dbconnect();
    $con = $db->connect();
    $result = mysqli_query($con,"INSERT INTO DOCTOR(DOC_NAME,DOC_ADD,DOC_MOB,DOC_DOB,DOC_DOJ,DOC_NOOFCUST,
      DOC_LAT,DOC_LON,DOC_TYPE,DOC_EMAIL,DOC_FCMID) VALUES('$name','$address',$mobile,'$dob',CURRENT_TIMESTAMP,$noofcusts,
        $lat,$lon,'$type','$email','$fcmid');");
    if($result)
    {
      //successfully inserted
      $response["success"]=1;
      $response["message"]="Doctor successfully registered";
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
