<?php
class dbconnect{
  function __construct(){


  }
  function __destruct(){

  }
  function connect(){
    $con=mysqli_connect('localhost','root','password','E_SWASTHYA');
    if(mysqli_connect_errno())
    {
      echo "Failed to connect to the database Mysql says : ".mysqli_connect_errno();
    }
    return $con;
  }
}

 ?>
