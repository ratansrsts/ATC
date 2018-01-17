<?php

   include("connection.php");

   if(! $conn ) {
      die('Could not connect: ' . mysql_error());
   }
   
   $sql = "SELECT `id`, `username`, `Fullname`, `password` FROM `users` WHERE username='".$_GET["uname"]."' AND password='".$_GET["pwd"]."'";

   $retval = mysqli_query($conn, $sql);
 //  $row = mysqli_num_rows($retval);
   
   if(! $retval ) {
      die('Could not get data: ' . mysql_error());
   }
   if (is_null($retval)) {
     echo "err";
   }
   
   while($row = mysqli_fetch_array($retval)) 
   {  echo "{\"Sucess\":[{\"usrid\":\"{$row['id']}\", \"UserName\":\"{$row['username']}\", \"FullName\":\"{$row['Fullname']}\"}]}"; }
   
   mysqli_close($conn);
?>
