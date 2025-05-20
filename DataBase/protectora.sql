-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3308
-- Tiempo de generación: 20-05-2025 a las 10:40:45
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `protectora`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `adopta`
--

CREATE TABLE `adopta` (
                          `idAdopta` int(11) NOT NULL,
                          `idAdoptante` int(11) NOT NULL,
                          `idAnimal` int(11) NOT NULL,
                          `fechaAdopcion` date NOT NULL,
                          `observaciones` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `adoptante`
--

CREATE TABLE `adoptante` (
                             `idAdoptante` int(11) NOT NULL,
                             `telefono` varchar(15) NOT NULL,
                             `direccion` varchar(255) NOT NULL,
                             `idPersona` int(11) NOT NULL,
                             `idAnimal` int(11) NOT NULL,
                             `observaciones` text DEFAULT NULL,
                             `nombre` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `animal`
--

CREATE TABLE `animal` (
                          `idAnimal` int(11) NOT NULL,
                          `nombre` varchar(100) NOT NULL,
                          `chip` varchar(15) DEFAULT NULL,
                          `edad` int(11) NOT NULL,
                          `tipo` varchar(50) NOT NULL,
                          `estado` varchar(50) NOT NULL,
                          `fechaAdopcion` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `animal`
--

INSERT INTO `animal` (`idAnimal`, `nombre`, `chip`, `edad`, `tipo`, `estado`, `fechaAdopcion`) VALUES
    (32, 'Perro', '76435345', 3, 'PERRO', 'Adoptado', '2025-05-16');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cuida`
--

CREATE TABLE `cuida` (
                         `idCuida` int(11) NOT NULL,
                         `idTrabajador` int(11) NOT NULL,
                         `idAnimal` int(11) NOT NULL,
                         `observaciones` varchar(255) DEFAULT NULL,
                         `tipo` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `cuida`
--

INSERT INTO `cuida` (`idCuida`, `idTrabajador`, `idAnimal`, `observaciones`, `tipo`) VALUES
    (19, 33, 32, 'observaciones', 'Tipo Cuidado');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `persona`
--

CREATE TABLE `persona` (
                           `idPersona` int(11) NOT NULL,
                           `nombre` varchar(100) NOT NULL,
                           `email` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `persona`
--

INSERT INTO `persona` (`idPersona`, `nombre`, `email`) VALUES
    (33, 'Daniel', 'danicvillalba@gmail.com');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `trabajador`
--

CREATE TABLE `trabajador` (
                              `idTrabajador` int(11) NOT NULL,
                              `estado` varchar(50) DEFAULT NULL,
                              `email` varchar(100) NOT NULL,
                              `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `trabajador`
--

INSERT INTO `trabajador` (`idTrabajador`, `estado`, `email`, `password`) VALUES
    (33, NULL, 'danicvillalba@gmail.com', 'bd3dae5fb91f88a4f0978222dfd58f59a124257cb081486387cbae9df11fb879');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `adopta`
--
ALTER TABLE `adopta`
    ADD PRIMARY KEY (`idAdopta`),
  ADD KEY `idAdoptante` (`idAdoptante`),
  ADD KEY `idAnimal` (`idAnimal`);

--
-- Indices de la tabla `adoptante`
--
ALTER TABLE `adoptante`
    ADD PRIMARY KEY (`idAdoptante`),
  ADD KEY `idPersona` (`idPersona`),
  ADD KEY `idAnimal` (`idAnimal`);

--
-- Indices de la tabla `animal`
--
ALTER TABLE `animal`
    ADD PRIMARY KEY (`idAnimal`);

--
-- Indices de la tabla `cuida`
--
ALTER TABLE `cuida`
    ADD PRIMARY KEY (`idCuida`),
  ADD KEY `idTrabajador` (`idTrabajador`),
  ADD KEY `idAnimal` (`idAnimal`);

--
-- Indices de la tabla `persona`
--
ALTER TABLE `persona`
    ADD PRIMARY KEY (`idPersona`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `nombre` (`nombre`),
  ADD UNIQUE KEY `email_2` (`email`);

--
-- Indices de la tabla `trabajador`
--
ALTER TABLE `trabajador`
    ADD PRIMARY KEY (`idTrabajador`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `adopta`
--
ALTER TABLE `adopta`
    MODIFY `idAdopta` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `adoptante`
--
ALTER TABLE `adoptante`
    MODIFY `idAdoptante` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `animal`
--
ALTER TABLE `animal`
    MODIFY `idAnimal` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT de la tabla `cuida`
--
ALTER TABLE `cuida`
    MODIFY `idCuida` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT de la tabla `persona`
--
ALTER TABLE `persona`
    MODIFY `idPersona` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `adopta`
--
ALTER TABLE `adopta`
    ADD CONSTRAINT `adopta_ibfk_1` FOREIGN KEY (`idAdoptante`) REFERENCES `adoptante` (`idAdoptante`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `adopta_ibfk_2` FOREIGN KEY (`idAnimal`) REFERENCES `animal` (`idAnimal`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `adoptante`
--
ALTER TABLE `adoptante`
    ADD CONSTRAINT `adoptante_ibfk_1` FOREIGN KEY (`idPersona`) REFERENCES `persona` (`idPersona`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `adoptante_ibfk_2` FOREIGN KEY (`idAnimal`) REFERENCES `animal` (`idAnimal`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `cuida`
--
ALTER TABLE `cuida`
    ADD CONSTRAINT `cuida_ibfk_1` FOREIGN KEY (`idTrabajador`) REFERENCES `trabajador` (`idTrabajador`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `cuida_ibfk_2` FOREIGN KEY (`idAnimal`) REFERENCES `animal` (`idAnimal`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `trabajador`
--
ALTER TABLE `trabajador`
    ADD CONSTRAINT `trabajador_ibfk_1` FOREIGN KEY (`idTrabajador`) REFERENCES `persona` (`idPersona`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
