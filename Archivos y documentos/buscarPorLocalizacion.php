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

$latitud=$_GET['latitud'];
$longitud=$_GET['longitud'];
$distanciaMax=$_GET['distanciaMax'];

//creo una consulta (query) precio,marca,categoria,tipo,comercio,fecha_venc,lat,long
$sql="SELECT precio, marca, categoria, tipo, comercio, fecha_venc, latitud, longitud, (6371 * acos(cos(radians('{$latitud}')) * cos(radians(latitud)) * cos(radians(longitud) - radians('{$longitud}')) + sin(radians('{$latitud}')) * sin(radians(latitud)))) AS distance FROM ofertas WHERE DATE(fecha_venc) >= CURDATE() HAVING distance < '{$distanciaMax}' ORDER BY distance";
	
$stmt = $conn->prepare ($sql);


//ejecuto el query
$stmt->execute();

//Vinculando resultados a la consulta
$stmt->bind_result($precio, $marca, $categoria, $tipo, $comercio, $fecha_venc, $latitud, $longitud, $distance);

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
$temp['distance'] = $distance;
array_push($ofert, $temp);
}

//despliego el resultado en formato JSON
echo json_encode($ofert);

?>