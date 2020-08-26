<?php
	$dsn = "mysql:host=localhost;dbname=bibliotecavirtual;charset=utf8";
	$usuario = "root";
	$senha = "";
	
	try{
		$PDO = new PDO($dsn, $usuario, $senha);
		
		//echo "conectou com sucesso!!";
		
	} catch (PDOException $erro) {
		//echo "Erro: " . $erro->getMessage();
		echo "conexao_erro";
		exit;
	}
	
?>