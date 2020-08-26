<?PHP
	include "conexao.php";
	
	$sql_read = "SELECT nome, email, telefone, tipo FROM conta";
	$dados = $PDO->query($sql_read);
	
	$resultado = array();
	
	while($rs = $dados->fetch(PDO::FETCH_OBJ)) {
		$resultado[] = array("nome"=>$rs->nome, "email"=>$rs->email, "telefone"=>$rs->telefone , "tipo"=>$rs->tipo);
	}
	
	echo json_encode($resultado);

?>