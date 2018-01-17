<?php

   include("connection.php");

   if(! $conn ) {
      die('Could not connect: ' . mysql_error());
   }
$sql = "SELECT * FROM `messages`";
   if(isset($_GET['recive_id']))
   {
   
   $sql = "SELECT B.message, B.sent_time, B.reciveusr_id, B.user_id, B.message_id, A.username AS \"Username\", A.Fullname FROM (messages B LEFT OUTER JOIN users A ON B.reciveusr_id = A.id) WHERE (B.user_id = ".$_GET['user_id']." AND B.reciveusr_id=".$_GET['recive_id'].") OR  (B.user_id = ".$_GET['recive_id']." AND B.reciveusr_id=".$_GET['user_id'].") ORDER by B.sent_time ASC";

      }
elseif ($_GET['user_id']!=null) {

   $sql = "SELECT B.message, B.sent_time, B.reciveusr_id, B.user_id, B.message_id, A.username AS \"Username\", A.Fullname FROM (messages B LEFT OUTER JOIN users A ON B.reciveusr_id = A.id) where B.user_id=".$_GET['user_id']." or B.reciveusr_id=".$_GET['user_id']." GROUP BY B.reciveusr_id, B.user_id ORDER BY `B`.`sent_time` ASC";

}


        


   $retval = mysqli_query($conn, $sql);
 //  $row = mysqli_num_rows($retval);
   
 if(! $retval ) {
      die('Could not get data: ' . mysql_error());
   }
   else
   {
     $str = "{\"Sucess\":[";
      while($row = mysqli_fetch_array($retval)) 
      {  if($row['reciveusr_id']==$_GET['user_id'])
         {
            $sql2 = "SELECT * FROM `users` where id=".$row['user_id'];
            $retval2 = mysqli_query($conn, $sql2);
            $row2 = mysqli_fetch_array($retval2);
            
           $str.= "{\"usrid\":\"{$row['user_id']}\", \"reciver_id\":\"{$row['reciveusr_id']}\", \"message_id\":\"{$row['message_id']}\", \"username\":\"{$row2['username']}\", \"Fullname\":\"{$row2['Fullname']}\", \"sent_time\":\"{$row['sent_time']}\", \"Message\":\"{$row['message']}\"},"; 
         }
         else if($row['reciveusr_id']!=$row['user_id'])
         {

            $str.= "{\"usrid\":\"{$row['user_id']}\", \"reciver_id\":\"{$row['reciveusr_id']}\", \"message_id\":\"{$row['message_id']}\", \"username\":\"{$row['Username']}\", \"Fullname\":\"{$row['Fullname']}\", \"sent_time\":\"{$row['sent_time']}\", \"Message\":\"{$row['message']}\"},"; 

         }
      }
   }

   $str.= "]}";

   echo $str;
   mysqli_close($conn);
?>
