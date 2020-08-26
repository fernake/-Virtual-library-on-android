<?php
	include "conexao.php";
	
	$nome = $_POST['nome_app'];
	$email = $_POST['email_app'];
	$senha = $_POST['senha_app'];
	$tipo = $_POST['tipo_app'];
	$telefone = $_POST['telefone_app'];
	
	$sql_verifica = "SELECT * FROM conta WHERE email = :EMAIL";
	$stmt = $PDO->prepare($sql_verifica);
	$stmt->bindParam(':EMAIL', $email);
	$stmt->execute();
	
	if($stmt->rowCount() > 0){
		//email já cadastrado
		$retornoApp = array("CADASTRO"=>"ID_ERRO");
	} else{
		//vai ser cadastrado
		
		$sql_insert = "INSERT INTO conta (ID, TIPO, SENHA, NOME, EMAIL, TELEFONE) VALUES (NULL, :TIPO, :SENHA, :NOME, :EMAIL, :TELEFONE);";
		$stmt = $PDO->prepare($sql_insert);
			
		$stmt->bindParam(':TIPO', $tipo);
		$stmt->bindParam(':SENHA', $senha);
		$stmt->bindParam(':NOME', $nome);
		$stmt->bindParam(':EMAIL', $email);
		$stmt->bindParam(':TELEFONE', $telefone);
			
		if($stmt->execute()) {
			$retornoApp = array("CADASTRO"=>"SUCESSO");
		} else{
			$retornoApp = array("CADASTRO"=>"ERRO");
		}
	}
	
	
	
	
	echo json_encode($retornoApp);
?>