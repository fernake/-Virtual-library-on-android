<?php
	include "conexao.php";
	
	$email = $_POST['email_app'];
	$senha = $_POST['senha_app'];
	
	$sql_login = "SELECT tipo FROM conta WHERE email = :EMAIL AND senha = :SENHA";
	$stmt = $PDO->prepare($sql_login);
	$stmt->bindParam(':EMAIL', $email);
	$stmt->bindParam(':SENHA', $senha);
	$stmt->execute();
	
	;
	
	if($stmt->rowCount() > 0){
		//SUCESSO
		//$retornoApp = array("LOGIN"=>"SUCESSO");
		
		$procurar = $PDO->prepare($sql_login);
		$procurar->bindParam(':EMAIL', $email, PDO::PARAM_STR);
		$procurar->bindParam(':SENHA', $senha, PDO::PARAM_STR);
		
		if($procurar->execute()) {
		$rs = $procurar->fetch(PDO::FETCH_OBJ);
		$retornoApp = array("LOGIN"=>$rs->tipo);
	} else {
		throw new PDOException("Erro: Não foi possível executar a declaração sql");	
	}
		
		
	} else{
		$retornoApp = array("LOGIN"=>"ERRO");
	}
	
	echo json_encode($retornoApp);
?>