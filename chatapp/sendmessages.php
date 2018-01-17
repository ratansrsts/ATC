<?php

include("connection.php");


// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$sql = "INSERT INTO `messages`(`user_id`,`reciveusr_id`, `Message`) 
VALUES (".$_GET["uname"].",".$_GET["rid"].",'".$_GET["message"]."')";

if ($conn->query($sql) === TRUE) {
    echo "suxsex";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

$conn->close();
?>