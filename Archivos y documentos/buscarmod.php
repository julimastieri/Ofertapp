<?PHP

//constantes de la base de datos
define('DB_HOST', 'localhost');
define('DB_USER','root');
define('DB_PASS','');
define('DB_NAME', 'bd_ofertapp');

//coneccion con la BD y obteniendo el objeto conector
$conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

//chequeando si ocurre algun error durante la conexion con la BD
if (mysqli_connect_errno()){
	echo "Fallo al conectar con MySQL: ". mysqli_connect_error();
	die();
}

$marca=$_GET['marca'];
$categoria=$_GET['categoria'];
$tipo=$_GET['tipo'];
$comercio=$_GET['comercio'];



//creo una consulta (query) precio,marca,categoria,tipo,comercio,fecha_venc,lat,long
if(($marca == "") && ($categoria == "") && ($tipo == "") && ($comercio == "")){
	$stmt = $conn->prepare ("SELECT precio, marca, categoria, tipo, comercio, fecha_venc, latitud, longitud FROM ofertas;");}
else
{
	$sql="SELECT precio, marca, categoria, tipo, comercio, fecha_venc,latitud, longitud FROM ofertas WHERE";
	        if ($marca != "") {
	            $sql=  $sql . " marca = '{$marca}' AND";
	        }if ($categoria != "") {
	            $sql= $sql . " categoria = '{$categoria}' AND";
	        }if ($tipo != "") {
	            $sql= $sql ." tipo = '{$tipo}' AND";
	        }if ($comercio != "") {
	            $sql= $sql . " comercio = '{$comercio}' AND";
	        }  
			$sqlFinal=trim($sql, 'AND'); //elimino el ultimo and
			
	$stmt = $conn->prepare ($sqlFinal);
}


//ejecuto el query
$stmt->execute();

//Vinculando resultados a la consulta
$stmt->bind_result($precio, $marca, $categoria, $tipo, $comercio, $fecha_venc, $latitud, $longitud);

$ofert = array();

//navegando a través del resulset
while ($stmt->fetch()){
$temp = array();
$temp['precio'] = $precio;
$temp['marca'] = $marca;
$temp['categoria'] = $categoria;
$temp['tipo'] = $tipo;
$temp['comercio'] = $comercio;
$temp['fecha_venc'] = $fecha_venc;
$temp['latitud'] = $latitud;
$temp['longitud'] = $longitud;
array_push($ofert, $temp);
}

//despliego el resultado en formato JSON
echo json_encode($ofert);

?>