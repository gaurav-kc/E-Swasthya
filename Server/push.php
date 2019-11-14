<?php

/*Author ---- Gaurav Chaudhari */
/*This file acts as a getter setter class for data transfer */

  class Push
{
  private $title; //The bold main text of notification
  private $message;  //The sub-text below main text displayed in notification
  private $image;    //the image url is stored here
  private $data;    //data has the extra payload to be sent to the device
  /*The is_background is a flag which will be useful to deal the situations 'whether the app is open (foreground)'
  or the app is not open (but running in background)
  */
  private $is_background;
  function __construct()
  {
    //do nothing on object creation
  }
  //now setters for all those variables
  public function setTitle($title){     $this->title = $title;    }
  public function setMessage($message){     $this->message = $message;    }
  public function setImageUrl($imageUrl){     $this->image = $imageUrl;    }
  public function setPayload($data){     $this->data = $data;    }
  public function setIsBackground($is_background){     $this->is_background = $is_background;    }

  //the getter function. Instead of making get functions for each variable ,
  //the data will be returned in the form of Array

  public function getPush(){
    $res = array();
    $res['data']['title'] = $this->title;
    $res['data']['is_background'] = $this->is_background;
    $res['data']['message'] = $this->message;
    $res['data']['image'] = $this->image;
    $res['data']['payload'] = $this->data;
    $res['data']['timestamp'] = date('Y-m-d G:i:s');
    return $res;
  }
}
/* That's it. The getter setter is ready to rock n roll :P*/

?>
