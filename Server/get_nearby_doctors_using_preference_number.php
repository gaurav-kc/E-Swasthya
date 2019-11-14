<?php

/*This file is to get the nearest n doctors and send JSON object of the location information to requestor.
Author name --- Gaurav Chaudhari */

if(isset($_POST['number']) && isset($_POST['patient_longitude']) && isset($_POST['patient_latitude']))
{
  //store the received values in variables
  $number = $_POST['number'];
  $patient_longitude = $_POST['patient_longitude'];
  $patient_latitude = $_POST['patient_latitude'];
  //establish database connection
  require_once('dbconnect.php');
  $db = new dbconnect();
  $con = $db->connect();
  //con is connection variable and the connection is now established
   //HAVING distance < 25
   $result = mysqli_query($con,"SELECT DOC_ID, (
    6371 * acos(cos(radians($patient_latitude)) *
    cos(radians(DOC_LAT)) *
    cos(radians(DOC_LON) -
    radians($patient_longitude)) +
    sin(radians($patient_latitude)) *
    sin(radians(DOC_LAT )))
    ) AS distance
    FROM DOCTOR
    ORDER BY distance DESC LIMIT 0, $number ;");
    $rows=array();
    if(!empty($result))
    {
      while($row = mysqli_fetch_assoc($result))
      {
        $rows[] = $row;
      }

    }
    echo json_encode($rows);
}else {
  /*response['success']=0;
  response['message']="Incomplete information";*/
}
 ?>
