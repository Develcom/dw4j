CREATE TABLE categoria (
    id_categoria integer DEFAULT nextval('sq_categroria'::regclass) NOT NULL,
    id_libreria integer NOT NULL,
    categoria character varying(200) NOT NULL,
    id_estatus integer NOT NULL
);


CREATE TABLE causa (
    id_causa integer NOT NULL,
    causa character varying(150) NOT NULL
);


CREATE TABLE configuracion (
    id_configuracion integer NOT NULL,
    calidad character(1),
    ruta_temporal character varying(50),
    archivo_tif character varying(50),
    archivo_cod character varying(50),
    log character varying(50),
    foliatura character(1),
    server_name character varying(50),
    database_name character varying(50),
    port integer,
    userbd character varying(50),
    password character varying(50),
    ficha character(1),
    fabrica character(1),
    elimina character(1)
);


CREATE TABLE dato_adicional (
    id_dato_adicional integer DEFAULT nextval('sq_dato_adicional'::regclass) NOT NULL,
    indice_adicional character varying(250) NOT NULL,
    tipo character varying(50) NOT NULL,
    id_documento integer NOT NULL,
    codigo integer
);


CREATE TABLE datos_infodocumento (
    id_datos integer DEFAULT nextval('sq_datos_infodocumento'::regclass) NOT NULL,
    id_infodocumento integer,
    id_doc_eliminado integer,
    fecha_digitalizacion date,
    usuario_digitalizo character varying(30),
    fecha_aprobacion date,
    usuario_aprobacion character varying(30),
    fecha_rechazo date,
    usuario_rechazo character varying(30),
    motivo_rechazo character varying(300),
    causa_rechazo character varying(300),
    fecha_eliminado date,
    usuario_elimino character varying(30),
    motivo_elimino character varying(300),
    causa_elimino character varying(300),
    dato_adicional character varying(30)
);


CREATE TABLE documento_eliminado (
    id_doc_eliminado integer DEFAULT nextval('sq_documento_eliminado'::regclass) NOT NULL,
    id_expediente character varying(50) NOT NULL,
    id_libreria integer NOT NULL,
    id_categoria integer NOT NULL,
    id_subcategoria integer NOT NULL,
    id_documento integer NOT NULL,
    numero_documento integer NOT NULL,
    version integer NOT NULL,
    paginas integer NOT NULL,
    fecha_vencimiento date,
    fecha_eliminado date NOT NULL,
    usuario_elimino character varying(30) NOT NULL
);


CREATE TABLE estatus (
    id_estatus integer NOT NULL,
    estatus character varying(100) NOT NULL
);


CREATE TABLE estatus_documento (
    id_estatus_documento integer NOT NULL,
    estatus_documento character varying(20) NOT NULL
);


CREATE TABLE expedientes (
    id_expedientes integer DEFAULT nextval('sq_expediente'::regclass) NOT NULL,
    expediente character varying(250) NOT NULL,
    id_indice integer NOT NULL,
    valor character varying(250),
    fecha_indice date,
    id_libreria integer NOT NULL,
    id_categoria integer NOT NULL
);


CREATE TABLE fabrica (
    usuario character varying(50) NOT NULL,
    fabrica character(1) NOT NULL
);


CREATE TABLE foliatura (
    id_foliatura integer DEFAULT nextval('sq_foliatura'::regclass) NOT NULL,
    id_infodocumento integer NOT NULL,
    id_documento integer NOT NULL,
    id_expediente character varying(50) NOT NULL,
    pagina integer NOT NULL
);


CREATE TABLE indices (
    id_indice integer DEFAULT nextval('sq_indices'::regclass) NOT NULL,
    id_categoria integer NOT NULL,
    indice character varying(250) NOT NULL,
    tipo character varying(50) NOT NULL,
    codigo integer NOT NULL,
    clave character(1)
);


CREATE TABLE infodocumento (
    id_infodocumento integer DEFAULT nextval('sq_infodocumento'::regclass) NOT NULL,
    id_documento integer NOT NULL,
    id_expediente character varying(50),
    nombre_archivo character varying(1000),
    ruta_archivo character varying(1000),
    formato character varying(4),
    numero_documento integer NOT NULL,
    version integer NOT NULL,
    paginas integer NOT NULL,
    fecha_vencimiento date,
    estatus_documento integer NOT NULL,
    re_digitalizado character(1) NOT NULL
);


CREATE TABLE libreria (
    id_libreria integer DEFAULT nextval('sq_libreria'::regclass) NOT NULL,
    libreria character varying(200) NOT NULL,
    id_estatus integer NOT NULL
);


CREATE TABLE lista_desplegables (
    id_lista integer DEFAULT nextval('sq_combo'::regclass) NOT NULL,
    codigo_indice integer NOT NULL,
    descripcion character varying(200)
);


CREATE TABLE perfil (
    id_perfil integer DEFAULT nextval('sq_perfil'::regclass) NOT NULL,
    id_libreria integer,
    id_categoria integer,
    id_usuario character varying(50) NOT NULL,
    id_rol integer NOT NULL
);


CREATE TABLE reporte (
    id_categoria integer,
    expediente character varying(250),
    indicetexto character varying(250),
    indicenumero integer,
    indicefecha date,
    indicecombo character varying(250)
);


CREATE TABLE rol (
    id_rol integer NOT NULL,
    rol character varying(50) NOT NULL
);



CREATE TABLE subcategoria (
    id_subcategoria integer DEFAULT nextval('sq_subcategroria'::regclass) NOT NULL,
    id_categoria integer NOT NULL,
    subcategoria character varying(200) NOT NULL,
    id_estatus integer NOT NULL
);


CREATE TABLE tipodocumento (
    id_documento integer DEFAULT nextval('sq_tipo_documento'::regclass) NOT NULL,
    id_categoria integer NOT NULL,
    id_subcategoria integer NOT NULL,
    tipo_documento character varying(200) NOT NULL,
    id_estatus integer NOT NULL,
    vencimiento character(1),
    dato_adicional character(1),
    ficha character(1)
);


CREATE TABLE usuario (
    id_usuario character varying(50) NOT NULL,
    nombre character varying(100) NOT NULL,
    apellido character varying(100) NOT NULL,
    cedula character varying(50),
    sexo character(1),
    id_estatus integer NOT NULL,
    password character varying(16)
);


CREATE TABLE valor_dato_adicional (
    id_valor integer DEFAULT nextval('sq_valor_dato_adicional'::regclass) NOT NULL,
    id_dato_adicional integer NOT NULL,
    valor character varying(250) NOT NULL,
    numero integer NOT NULL,
    version integer NOT NULL,
    expediente character varying(250) NOT NULL
);
