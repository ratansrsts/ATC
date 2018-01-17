<?php

   include("connection.php");

   if(! $conn ) {
      die('Could not connect: ' . mysql_error());
   }

   if ($_GET['Username']!=null) {

   $sql = "SELECT * FROM `users` WHERE username=\"".$_GET['Username']."\"";

}

   $retval = mysqli_query($conn, $sql);
 //  $row = mysqli_num_rows($retval);
   
   if(! $retval ) {
      die('Could not get data: ' . mysql_error());
   }
   else{
      echo "{\"Sucess\":[";
   while($row = mysqli_fetch_array($retval)) 
   { echo "{\"usrid\":\"{$row['id']}\", \"username\":\"{$row['username']}\", \"Fullname\":\"{$row['Fullname']}\"}"; }
   }echo "]}";




   
   mysqli_close($conn);
?>
