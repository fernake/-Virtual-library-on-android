<?php
	include "conexao.php";

	$email = $_POST['email_app'];
	$nome = $_POST['nome_app'];
	
	
	$procurar = $PDO->prepare("SELECT nome, email, telefone, tipo  FROM conta WHERE nome LIKE :NOME OR email LIKE :EMAIL");
	$procurar->bindParam(':EMAIL', $email, PDO::PARAM_STR);
	$procurar->bindParam(':NOME', $nome ,PDO::PARAM_STR);
	
	$conta = array();
	
	if($procurar->execute()) {
		$rs = $procurar->fetch(PDO::FETCH_OBJ);
		$conta[] = array("nome"=>$rs->nome, "email"=>$rs->email, "telefone"=>$rs->telefone , "tipo"=>$rs->tipo);
		
		if($rs->nome == null && $rs->email == null){
			$conta = array("ERRO");
		}
	} else {
		//throw new PDOException("Erro: Não foi possível executar a declaração sql");	
		$conta = array("ERRO NO SQL CODE");
	}
	
	echo json_encode($conta);
?>