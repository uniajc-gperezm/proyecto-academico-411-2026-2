-- Ejecutar en Neon > SQL Editor
CREATE TABLE IF NOT EXISTS estudiante (
    id_estudiante SERIAL PRIMARY KEY,
    nombre   VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email    VARCHAR(150)
);
CREATE TABLE IF NOT EXISTS docente (
    id_docente   SERIAL PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100) NOT NULL
);
CREATE TABLE IF NOT EXISTS materia (
    id_materia     SERIAL PRIMARY KEY,
    nombre_materia VARCHAR(100) NOT NULL,
    creditos       INT NOT NULL CHECK (creditos > 0)
);
CREATE TABLE IF NOT EXISTS grupo (
    id_grupo   SERIAL PRIMARY KEY,
    id_materia INT NOT NULL REFERENCES materia(id_materia),
    id_docente INT NOT NULL REFERENCES docente(id_docente),
    aula       VARCHAR(50) NOT NULL,
    horario    VARCHAR(100) NOT NULL
);
CREATE TABLE IF NOT EXISTS inscripcion_curso (
    id_inscripcion SERIAL PRIMARY KEY,
    id_estudiante  INT NOT NULL REFERENCES estudiante(id_estudiante),
    id_grupo       INT NOT NULL REFERENCES grupo(id_grupo),
    nota_final     REAL NOT NULL DEFAULT 0 CHECK (nota_final BETWEEN 0 AND 5),
    estado         VARCHAR(20) NOT NULL DEFAULT 'INSCRITO'
);
