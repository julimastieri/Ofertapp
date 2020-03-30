<?PHP
$hostname="localhost";
$database="bd_ofertapp";
$username="root";
$password="";
$json=array();
//si los atributos no son nulos (isset)
	if(isset($_GET["precio"])&& isset($_GET["marca"]) && isset($_GET["categoria"]) && isset($_GET["tipo"]) && isset($_GET["comercio"]) && isset($_GET["fecha_venc"]) && isset($_GET["latitud"]) && isset($_GET["longitud"]) ){
		//traigo los datos de android a las variables, para usarlos en la consulta
		$precio=$_GET['precio'];
		$marca=$_GET['marca'];
		$categoria=$_GET['categoria'];
		$tipo=$_GET['tipo'];
		$comercio=$_GET['comercio'];
		$fecha_venc=$_GET['fecha_venc'];
		$latitud=$_GET['latitud'];
		$longitud=$_GET['longitud'];
		
		//envio los datos a la base
		$conexion=mysqli_connect($hostname,$username,$password,$database);
		
		$consulta="INSERT INTO ofertas(precio, marca, categoria, tipo, comercio, fecha_venc, latitud, longitud) VALUES ('{$precio}','{$marca}' , '{$categoria}','{$tipo}','{$comercio}','{$fecha_venc}', '{$latitud}', '{$longitud}')";
		$resultado=mysqli_query($conexion,$consulta);  //Devuelve el resultado de la consulta

       
		if($consulta){
		   $consulta="SELECT * FROM ofertas  WHERE precio='{$precio}'";
		   $resultado=mysqli_query($conexion,$consulta);

			if($reg=mysqli_fetch_array($resultado)){
				$json['datos'][]=$reg;
			}
			mysqli_close($conexion);
			echo json_encode($json);
		}



		else{
			$results["precio"]='';
			$results["marca"]='';
			$results["categoria"]='';
			$results["tipo"]='';
			$results["comercio"]='';
			$results["fecha_venc"]='';
			$results["latitud"]='';
			$results["longitud"]='';
			$json['datos'][]=$results;
			echo json_encode($json);
		}
		
	}
	else{   
			$results["precio"]='';
			$results["marca"]='';
			$results["categoria"]='';
			$results["tipo"]='';
			$results["comercio"]='';
			$results["fecha_venc"]='';
			$results["latitud"]='';
			$results["longitud"]='';
			$json['datos'][]=$results;
			echo json_encode($json);
		}
?>