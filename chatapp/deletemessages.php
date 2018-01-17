<?php

include("connection.php");


// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$sql = "DELETE FROM `messages` WHERE `messages`.`message_id` = ".$_GET['message_id'];

if ($conn->query($sql) === TRUE) {
    echo "Sucess:";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

$conn->close();
?>