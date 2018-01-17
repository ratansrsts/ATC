<?php

include("connection.php");


// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$sql = "INSERT INTO `users`( `username`, `Fullname`, `password`) VALUES ('".$_GET["uname"]."','".$_GET["fname"]."','".$_GET["pwd"]."')";

if ($conn->query($sql) === TRUE) {
    echo "sucess:";
}
else if(strpos(($conn->error), 'Duplicate entry') !== false) {
    echo "err: Username Already Exists";
} 
else {
    echo "err: Invalid Entries.";
}

$conn->close();
?>