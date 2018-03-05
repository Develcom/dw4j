
INSERT INTO causa (id_causa, causa) VALUES (1, 'Mala Calidad en la Imagen');
INSERT INTO causa (id_causa, causa) VALUES (2, 'Mala tipificación del Documento');
INSERT INTO causa (id_causa, causa) VALUES (3, 'Los Índices no concuerdan con el Documento');
INSERT INTO causa (id_causa, causa) VALUES (4, 'Mala Orientación del Documento');
INSERT INTO causa (id_causa, causa) VALUES (5, 'Visualización Nula del Documento');
INSERT INTO causa (id_causa, causa) VALUES (6, 'Falla Técnica del Sistema');
INSERT INTO causa (id_causa, causa) VALUES (7, 'Eliminacion de Documento');


INSERT INTO configuracion (id_configuracion, calidad, ruta_temporal, archivo_tif, archivo_cod, log, foliatura, server_name, database_name, port, userbd, password, ficha, fabrica, elimina) VALUES (1, '1', 'temp', 'documento.tiff', 'codificado.cod', '/lib/log4j-config.properties', '1', '192.168.0.142', 'dw4j', 5432, 'cG9zdGdyZXM=', 'ZGV2ZWxjb20=', '1', '1', '1');


INSERT INTO estatus (id_estatus, estatus) VALUES (1, 'Activo');
INSERT INTO estatus (id_estatus, estatus) VALUES (2, 'Inactivo');


INSERT INTO estatus_documento (id_estatus_documento, estatus_documento) VALUES (0, 'Pendiente');
INSERT INTO estatus_documento (id_estatus_documento, estatus_documento) VALUES (1, 'Aprobado');
INSERT INTO estatus_documento (id_estatus_documento, estatus_documento) VALUES (2, 'Rechazado');


INSERT INTO perfil (id_perfil, id_libreria, id_categoria, id_usuario, id_rol) VALUES (1, NULL, NULL, 'dw4jconf', 7);
INSERT INTO perfil (id_perfil, id_libreria, id_categoria, id_usuario, id_rol) VALUES (2, NULL, NULL, 'dw4jconf', 8);


INSERT INTO rol (id_rol, rol) VALUES (1, 'ADMINISTRADOR');
INSERT INTO rol (id_rol, rol) VALUES (2, 'APROBADOR');
INSERT INTO rol (id_rol, rol) VALUES (3, 'DIGITALIZADOR');
INSERT INTO rol (id_rol, rol) VALUES (4, 'CONSULTAR');
INSERT INTO rol (id_rol, rol) VALUES (5, 'IMPRIMIR');
INSERT INTO rol (id_rol, rol) VALUES (6, 'REPORTES');
INSERT INTO rol (id_rol, rol) VALUES (7, 'CONFIGURADOR');
INSERT INTO rol (id_rol, rol) VALUES (8, 'MANTENIMIENTO');
INSERT INTO rol (id_rol, rol) VALUES (9, 'ELIMINAR');

INSERT INTO usuario (id_usuario, nombre, apellido, cedula, sexo, id_estatus, password) VALUES ('dw4jconf', 'Usuario', 'Configurador', NULL, NULL, 1, 'RGV2ZWxjb21HRA==');



ALTER TABLE ONLY categoria
    ADD CONSTRAINT categoria_pk PRIMARY KEY (id_categoria);


ALTER TABLE ONLY causa
    ADD CONSTRAINT causa_pk PRIMARY KEY (id_causa);


ALTER TABLE ONLY documento_eliminado
    ADD CONSTRAINT documento_eliminado_pk PRIMARY KEY (id_doc_eliminado);


ALTER TABLE ONLY estatus_documento
    ADD CONSTRAINT estatus_documento_pk PRIMARY KEY (id_estatus_documento);


ALTER TABLE ONLY estatus
    ADD CONSTRAINT estatus_pk PRIMARY KEY (id_estatus);


ALTER TABLE ONLY foliatura
    ADD CONSTRAINT foliatura_pk PRIMARY KEY (id_foliatura);


ALTER TABLE ONLY libreria
    ADD CONSTRAINT libreria_pk PRIMARY KEY (id_libreria);


ALTER TABLE ONLY perfil
    ADD CONSTRAINT perfil_pk PRIMARY KEY (id_perfil);


ALTER TABLE ONLY lista_desplegables
    ADD CONSTRAINT pk_combo PRIMARY KEY (id_lista);


ALTER TABLE ONLY configuracion
    ADD CONSTRAINT pk_configuracion PRIMARY KEY (id_configuracion);


ALTER TABLE ONLY dato_adicional
    ADD CONSTRAINT pk_dato_adicional PRIMARY KEY (id_dato_adicional);


ALTER TABLE ONLY datos_infodocumento
    ADD CONSTRAINT pk_datos_infodocumento PRIMARY KEY (id_datos);


ALTER TABLE ONLY expedientes
    ADD CONSTRAINT pk_expedientes PRIMARY KEY (id_expedientes);


ALTER TABLE ONLY fabrica
    ADD CONSTRAINT pk_fabrica PRIMARY KEY (usuario);


ALTER TABLE ONLY indices
    ADD CONSTRAINT pk_indice PRIMARY KEY (id_indice);


ALTER TABLE ONLY infodocumento
    ADD CONSTRAINT pk_infordocumento PRIMARY KEY (id_infodocumento);


ALTER TABLE ONLY valor_dato_adicional
    ADD CONSTRAINT pk_valor_dato_adicional PRIMARY KEY (id_valor);


ALTER TABLE ONLY rol
    ADD CONSTRAINT rol_pk PRIMARY KEY (id_rol);


ALTER TABLE ONLY subcategoria
    ADD CONSTRAINT subcategoria_pk PRIMARY KEY (id_subcategoria);


ALTER TABLE ONLY tipodocumento
    ADD CONSTRAINT tipodocumento_pk PRIMARY KEY (id_documento);


ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_pk PRIMARY KEY (id_usuario);


ALTER TABLE ONLY dato_adicional
    ADD CONSTRAINT fk_dato_adicional_tipodoc FOREIGN KEY (id_documento) REFERENCES tipodocumento(id_documento);


ALTER TABLE ONLY datos_infodocumento
    ADD CONSTRAINT fk_datoc_infodoc FOREIGN KEY (id_infodocumento) REFERENCES infodocumento(id_infodocumento);


ALTER TABLE ONLY datos_infodocumento
    ADD CONSTRAINT fk_datos_doc_eliminado FOREIGN KEY (id_doc_eliminado) REFERENCES documento_eliminado(id_doc_eliminado);


ALTER TABLE ONLY expedientes
    ADD CONSTRAINT fk_expedientes_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria);


ALTER TABLE ONLY expedientes
    ADD CONSTRAINT fk_expedientes_libreria FOREIGN KEY (id_libreria) REFERENCES libreria(id_libreria);


ALTER TABLE ONLY fabrica
    ADD CONSTRAINT fk_fabrica_usuario FOREIGN KEY (usuario) REFERENCES usuario(id_usuario);


ALTER TABLE ONLY indices
    ADD CONSTRAINT fk_indice_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria);


ALTER TABLE ONLY infodocumento
    ADD CONSTRAINT fk_infodoc_statusdoc FOREIGN KEY (estatus_documento) REFERENCES estatus_documento(id_estatus_documento);


ALTER TABLE ONLY infodocumento
    ADD CONSTRAINT fk_infodoc_tipodoc FOREIGN KEY (id_documento) REFERENCES tipodocumento(id_documento);


ALTER TABLE ONLY perfil
    ADD CONSTRAINT fk_perfil_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria);


ALTER TABLE ONLY perfil
    ADD CONSTRAINT fk_perfil_libreria FOREIGN KEY (id_libreria) REFERENCES libreria(id_libreria);


ALTER TABLE ONLY perfil
    ADD CONSTRAINT fk_perfil_rol FOREIGN KEY (id_rol) REFERENCES rol(id_rol);


ALTER TABLE ONLY perfil
    ADD CONSTRAINT fk_perfil_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario);


ALTER TABLE ONLY tipodocumento
    ADD CONSTRAINT fk_tipodoc_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria);


ALTER TABLE ONLY tipodocumento
    ADD CONSTRAINT fk_tipodoc_subcategoria FOREIGN KEY (id_subcategoria) REFERENCES subcategoria(id_subcategoria);


ALTER TABLE ONLY usuario
    ADD CONSTRAINT fk_usuario_estatus FOREIGN KEY (id_estatus) REFERENCES estatus(id_estatus);


ALTER TABLE ONLY valor_dato_adicional
    ADD CONSTRAINT kf_valor_indice_da FOREIGN KEY (id_dato_adicional) REFERENCES dato_adicional(id_dato_adicional);
