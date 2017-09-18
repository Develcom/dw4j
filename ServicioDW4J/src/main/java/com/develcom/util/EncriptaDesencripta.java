package com.develcom.util;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.Vector;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Clase para encriptar y desencriptar
 *
 * @author develcom
 */
@Component
public final class EncriptaDesencripta {

    private static final Logger TRAZA = LoggerFactory.getLogger(EncriptaDesencripta.class);
    private static final String CRYPTOGHRAPHY_KEY = "k23kwpEMHKrLgNkYDzs+YVfzXSp9Xyx";
    private static final String CRYPTOGHRAPHY_IV = "G1oPqIGmVOk=";

    /**
     * Encripta una palabra
     *
     * @param arg La palabra a encrptar
     * @return La palabra encriptada
     */
    public static String encripta(String arg) {
        try {
            char[] HEXADECIMAL = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(arg.getBytes());

            StringBuilder sb = new StringBuilder(2 * bytes.length);
            for (int i = 0; i < bytes.length; i++) {
                int low = bytes[i] & 0xF;
                int high = (bytes[i] & 0xF0) >> 4;
                sb.append(HEXADECIMAL[high]);
                sb.append(HEXADECIMAL[low]);
            }
            arg = sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            TRAZA.trace("error encriptando", ex);
            //this.trazas.trazas(new StringBuilder().append("Error al encriptar ").append(ex.toString()).toString(), Level.ERROR);
            //ex.printStackTrace();
        }
        return arg;
    }

    /**
     * Este método se encarga de Cifra con el algoritmo TripleDes las cadenas
     * enviadas
     *
     * @param plainText : Texto en claro a cifrar
     * @return Una cadena codificada a 64
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws UnsupportedEncodingException
     */
    public static String encriptar(final String plainText)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException,
            UnsupportedEncodingException {


        final byte[] plaintext = plainText.getBytes("UTF-8");
        final byte[] tdesKeyData = BASE64DecoderStream.decode(CRYPTOGHRAPHY_KEY.getBytes());
        final byte[] myIV = BASE64DecoderStream.decode(CRYPTOGHRAPHY_IV.getBytes());

        //SecretKey desedeKey    = KeyGenerator.getInstance("DESede").generateKey();

        final Cipher c3des = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        //final Cipher c3des = Cipher.getInstance(desedeKey.getAlgorithm());
        final SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, "DESede");
        final IvParameterSpec ivspec = new IvParameterSpec(myIV);

        c3des.init(Cipher.ENCRYPT_MODE, myKey, ivspec);
        //c3des.init(Cipher.ENCRYPT_MODE,desedeKey);
        final byte[] cipherText = c3des.doFinal(plaintext);


        return new String(BASE64EncoderStream.encode(cipherText));
    }

    /**
     * Este metodo se encarga de descifrar las cadenas codificads
     *
     * @param plainText : texto Cifrado
     * @return 
     * @return: String que contiene el texto descifrado
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws UnsupportedEncodingException
     */
    public static String desencriptar(final String plainText)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException,
            UnsupportedEncodingException {


        byte[] plaintext = plainText.getBytes();
        final byte[] tdesKeyData = BASE64DecoderStream.decode(CRYPTOGHRAPHY_KEY.getBytes());
        final byte[] myIV = BASE64DecoderStream.decode(CRYPTOGHRAPHY_IV.getBytes());

        plaintext = BASE64DecoderStream.decode(plaintext);

        final Cipher c3des = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        final SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, "DESede");
        final IvParameterSpec ivspec = new IvParameterSpec(myIV);

        c3des.init(Cipher.DECRYPT_MODE, myKey, ivspec);
        final byte[] cipherText = c3des.doFinal(plaintext);


        return new String(cipherText, "UTF-8");
        //			  
    }

    /**
     * Método que utilizamos para devolver en un arreglo de bytes el contenido
     * de un archivo.
     *
     * @author christian garrido
     * @param file Archivo que deseamos convertir a un arreglo de bytes.
     * @return un arreglo de bytes que contiene el archivo que enviamos.
     * @throws IOException
     */
    public static byte[] getBytesFromFile(final File file) throws IOException {
        final InputStream inpStr = new FileInputStream(file);

        // Obtiene el tama�o del archivo
        final long length = file.length();

        // No se puede crear un arreglo utilizando el tipo de dato long.
        // Debe ser un entero.
        // Antes de convertirlo nos aseguramos de que no exceda el tama�o maximo
        // del entero Integer.MAX_VALUE.
        //log.debug("tama�o en long: " + length + " tama�o maximo de int: " Integer.MAX_VALUE);
//		if (length > Integer.MAX_VALUE) {
//			log.warn("El tama�o del archivo es mayor al tama�o permitido");
//		}
        // Se crea el arreglo de datos que contiene el archivo
        final byte[] bytes = new byte[(int) length];

        int offset = 0;
        final int numRead = inpStr.read(bytes, offset, bytes.length - offset);
        while (offset < bytes.length && numRead >= 0) {
            offset += numRead;
        }

        // Nos aseguramos que todos los bytes ah sido leidos
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }

        // Cerramos el inpuStream
        inpStr.close();
        return bytes;
    }

    public static void imprimirHashMap(HashMap<Object, Object> hm) {
        try {
            Iterator<Entry<Object, Object>> it = hm.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<Object, Object> e = (Map.Entry<Object, Object>) it.next();
                //System.out.println(e.getKey() + " | " + e.getValue());
                TRAZA.trace(e.getKey() + " | " + e.getValue());
            }
        } catch (Exception e) {
            TRAZA.trace("error al imprimir hashmap", e);
            //e.printStackTrace();
        }

    }
    private static Properties urls;

    /**
     * <p> Obtiene los valores del properties (configuracion.properties) Una vez
     * que se lee un valor se deja en memoria, si el archivo cambia hay que
     * reinicar la aplicaci�n para que tome los cambios. </p>
     *
     * @param llave
     * @return String
     */
    public static String getValor(String llave) {

        //log.debug("llave: " + llave);
        try {
            if (urls == null) {
                //log.debug("Cargando URLs...");
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                urls = new Properties();
                InputStream in = cl.getResourceAsStream("Configuracion.properties");
                try {
                    urls.load(in);
                } finally {
                    in.close();
                }
            }

            //log.debug("Valor:" + urls.getProperty(llave));

            return urls.getProperty(llave);
        } catch (Exception ex) {
            //log.error("", ex);
            return null;
        }
    }
    /**
     * <p> Mensajes. </p>
     */
    private static PropertyResourceBundle mensajes;

    /**
     * <p> Devuelve el texto del mensaje asociado a la clave recibida. </p>
     *
     * @param clave
     * @return
     */
    public static String getMensaje(String clave) {
        return getMensaje(clave, null);
    }

    /**
     * <p> Devuelve el texto del mensaje asociado a la clave recibida con los
     * valores aplicados al texto del mensaje. </p>
     *
     * @param clave
     * @param valores
     * @return
     */
    public static String getMensaje(String clave, Object[] valores) {
        try {
            if (mensajes == null) {
                synchronized (clave) {
                    //log.debug("Cargando mensajes");
                    ClassLoader cl = Thread.currentThread().getContextClassLoader();
                    InputStream in = cl.getResourceAsStream("Mensajes.properties");
                    mensajes = new PropertyResourceBundle(in);
                }
            }
            String mensaje = mensajes.getString(clave);
            if (mensaje != null) {
                return MessageFormat.format(mensaje, valores);
            }
        } catch (IOException ex) {
            TRAZA.error("error al cargar archivo propiedades de mensajes", ex);
            //log.error("", ex);
        }
        return clave;
    }

    /**
     * Convierte un objeto a XML
     *
     * @param resultado
     * @return
     */
    public static String objectToXml(Object resultado) {

        String salida = null;
        try {

            ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();

            XMLEncoder enc = new XMLEncoder(out);

            //enc = new XMLEncoder(out);
            enc.writeObject(resultado);
            enc.close();
            salida = "\n" + out.toString();

        } catch (Exception e) {
            TRAZA.trace("error al convertir objeto a xml", e);
            //e.printStackTrace();
        }

        return salida;
    }

    /**
     * <p> Convierte un strin a fecha, utilizado la mascara que se le envia. En
     * caso de error retorna null. </p>
     *
     * @param fecha
     * @param mascara
     * @return
     */
//	public static Date string2Date(String fecha, String mascara) {
//		return string2Date(fecha, mascara, log);
//	}
    /**
     * <p> Convierte un strin a fecha, utilizado la mascara que se le envia. En
     * caso de error retorna null. </p>
     *
     * @param fecha
     * @param mascara
     * @param log
     * @return
     */
    public static Date string2Date(String fecha, String mascara, Logger log) {
        try {
            DateFormat format = new SimpleDateFormat(mascara);
            return format.parse(fecha);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * <p> Convierte un string a Double, en caso de error retorna null. </p>
     *
     * @param numero
     * @return
     */
    public static Double string2Double(String numero) {
        return string2Double(numero);
    }

    /**
     * <p> Convierte un string a Double, en caso de error retorna null. </p>
     *
     * @param numero
     * @param log
     * @return
     */
    public static Double string2Double(String numero, Logger log) {
        try {
            return Double.valueOf(numero);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * <p> Convierte un string a Integer, en caso de error retorna null. </p>
     *
     * @param numero
     * @return
     */
    public static Integer string2Integer(String numero) {
        return string2Integer(numero);
    }

    /**
     * <p> Convierte un string a Integer, en caso de error retorna null. </p>
     *
     * @param numero
     * @param log
     * @return
     */
    public static Integer string2Integer(String numero, Logger log) {
        try {
            return Integer.valueOf(numero);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * Retorna un Vector con una fila por mes a partir de un rango de fechas
     *
     * @param fechaInicio
     * @param fechaFin
     * @return
     * @throws Exception
     */
    public static Vector<HashMap<String, Date>> fechas(Date fechaInicio,
            Date fechaFin) throws Exception {

        Vector<HashMap<String, Date>> vector = new Vector<HashMap<String, Date>>();
        HashMap<String, Date> fechas = new HashMap<String, Date>();

        Calendar cI = Calendar.getInstance();
        cI.setTime(fechaInicio);

        Calendar cF = Calendar.getInstance();
        cF.setTime(fechaFin);

        if (cI.after(cF)) {
            throw new Exception("La fecha fin debe ser mayor o igual a la inicio");
        }

        // si es la misma fecha hace el return
        // si es el mismo mes hace el return
        if (cF.equals(cI) || cI.get(Calendar.MONTH) == cF.get(Calendar.MONTH)) {
            fechas.put("INICIO", fechaFin);
            fechas.put("FIN", fechaFin);
            vector.add(fechas);
            return vector;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(cI.getTime());

        // si es mas de un mes calcula la lista
        while (!c.after(cF)) {
            Date inicio = new Date();
            Date fin = new Date();

            inicio.setTime(c.getTime().getTime());

            if (c.before(cF)
                    && !(c.get(Calendar.MONTH) == cF.get(Calendar.MONTH) && c
                    .get(Calendar.YEAR) == cF.get(Calendar.YEAR))) {

                // setea el ultimo d�a del mes
                c.set(Calendar.DAY_OF_MONTH, c
                        .getActualMaximum(Calendar.DAY_OF_MONTH));

                fin.setTime(c.getTime().getTime());

            } else if (c.equals(cF)) {

                fin.setTime(c.getTime().getTime());

            } else {
                fin.setTime(cF.getTime().getTime());
                c.setTime(cF.getTime());
            }

            System.out.println("-------");
            System.out.println(inicio);
            System.out.println(fin);

            HashMap<String, Date> fechasTmp = new HashMap<String, Date>();

            fechasTmp.put("INICIO", inicio);
            fechasTmp.put("FIN", fin);
            vector.add(fechasTmp);

            // corre al siguiente mes
            c.add(Calendar.DAY_OF_MONTH, 1);
        }

        return vector;
    }
    protected static String ENTERO_FMT = "([0-9]*)";

    public static boolean esNumeroValido(String cadena) {
        return (cadena.matches(ENTERO_FMT));
    }
}
