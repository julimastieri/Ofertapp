-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 29-03-2020 a las 00:17:12
-- Versión del servidor: 10.4.11-MariaDB
-- Versión de PHP: 7.4.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `bd_ofertapp`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ofertas`
--

CREATE TABLE `ofertas` (
  `id` int(11) NOT NULL,
  `precio` double NOT NULL,
  `marca` varchar(70) COLLATE utf8_unicode_ci NOT NULL,
  `categoria` varchar(70) COLLATE utf8_unicode_ci NOT NULL,
  `tipo` varchar(70) COLLATE utf8_unicode_ci NOT NULL,
  `comercio` varchar(70) COLLATE utf8_unicode_ci NOT NULL,
  `fecha_venc` date NOT NULL,
  `latitud` double NOT NULL,
  `longitud` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `ofertas`
--

INSERT INTO `ofertas` (`id`, `precio`, `marca`, `categoria`, `tipo`, `comercio`, `fecha_venc`, `latitud`, `longitud`) VALUES
(1, 157.5, 'Casancrem', 'Alimentos', 'Queso untable', 'Monarca', '2021-03-06', -37.326517140532744, -59.13760419934988),
(2, 106.5, 'Yogurisimo', 'Alimentos', 'Yogurt', 'Monarca', '2021-04-14', -37.32614308688269, -59.13761157542467),
(3, 39.99, 'Dia', 'Alimentos', 'Cerveza', 'Dia', '2023-03-25', -37.31759186728301, -59.13448512554169),
(4, 59.99, 'Pureza', 'Alimentos', 'Harina', 'Carrefour', '2023-12-14', -37.32621240558991, -59.135106727480895),
(5, 52, 'Bagley', 'Alimentos', 'Galletitas', 'Carrefour', '2025-12-12', -37.32615961673407, -59.13513958454132),
(6, 29.99, 'Higienol', 'Limpieza', 'Papel higenico', 'Dia', '2024-02-13', -37.31757746867446, -59.13449250161648),
(7, 99.99, 'Carrefour', 'Alimentos', 'Carne', 'Carrefour', '2022-02-13', -37.32393365289424, -59.11442823708057),
(8, 19, 'Dos hermanos', 'Alimentos', 'Arroz', 'Carrefour', '2022-04-15', -37.32389579307552, -59.11444164812565),
(9, 180, 'Amanda', 'Alimentos', 'Yerba', 'Vea', '2022-04-29', -37.3270767511767, -59.13238260895014),
(10, 25, 'Molto', 'Alimentos', 'Fideos', 'Vea', '2022-04-24', -37.327011165771346, -59.13239803165198),
(11, 39, 'Sancor', 'Alimentos', 'Leche', 'Monarca', '2028-07-01', -37.31730069488511, -59.1350819170475),
(12, 5, 'Arcor', 'Alimentos', 'Turrón de maní', 'Golopolis', '2021-03-25', -37.32227793454809, -59.117930196225636);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `ofertas`
--
ALTER TABLE `ofertas`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `ofertas`
--
ALTER TABLE `ofertas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=63;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
