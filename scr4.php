<?php
@session_start();
if($_POST["action"] == "login"){
	login();
}else if($_POST["action"] == "registrer"){
	registrer();
}


function login(){
	$user = $_POST["user"];
	$password = $_POST["password"];

	$lines = file("../file.txt");

	foreach($lines as $linea){   
		if (strstr($linea,$user)){
	        $_SESSION["user"] = $user;
	        echo 1;
	    }else{
	        echo "Nom ou Mot de passe invalides";
	    }
	}
}


function registrer(){
	$user = $_POST["user"];
	$password = $_POST["password"];

	$data = $user.$password;

	$file = fopen("../file.txt","a");
	$lines = file("../file.txt");

	if(!$lines){
		fwrite($file, $data."\r");
		echo "User enregistré";
	}else{
		foreach($lines as $linea){   
			if (strstr($linea,$data)){
		        echo "Votre user est déjà enregistré.";
		    }else{
		        echo "User enregistré";
		        fwrite($file, $data."\r");
		    }
		}
	}
	fclose($file);
}

?>