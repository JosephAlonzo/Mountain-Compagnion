<?php

            $valeur= $_POST["valeurToConvert"];
            $choix= $_POST["choixConversion"];
            $monnaieDeBase= $_POST["monnaieBase"];


if($choix == "convertToEuro"){

	if($monnaieDeBase=="Euros"){

		echo $valeur." ".$monnaieDeBase." donne ".$valeur." euros.";
	}elseif(isset($valeur)){

   			echo $valeur." ".$monnaieDeBase." donne ".round(convertToEuro($valeur))." euros.";
		}
}elseif($choix == "convertToFrancs"){

	if($monnaieDeBase=="Francs"){

		echo $valeur." ".$monnaieDeBase." donne ".$valeur." francs.";
	}elseif(isset($valeur)){

			echo $valeur." ".$monnaieDeBase." donne ".round(convertToFrancs($valeur))." francs.";
		}
}


function convertToFrancs($valeur){

    $valeur = $valeur *6.56;
    return $valeur;
}

function convertToEuro($valeur){

	$valeur = $valeur /6.56;
	return $valeur;
}

?>