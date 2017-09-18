/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.tools;

import com.develcom.dao.ManejoSesion;
import com.develcom.tools.cryto.CodDecodArchivos;
import com.develcom.tools.trazas.Traza;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import com.lowagie.text.pdf.codec.TiffImage;
import com.sun.media.imageio.plugins.tiff.TIFFImageWriteParam;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import javax.imageio.IIOImage;
import javax.imageio.IIOParamController;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.log4j.Level;
import uk.co.mmscomputing.imageio.tiff.TIFFImageReader;

/**
 * Clase de utilidad para docuementos digitalizadoz
 *
 * @author develcom
 */
public class ToolsFiles {

    /**
     * Archivo tif para guardar
     */
    private File fileImageTif;
    /**
     * Ruta completa del archivo tiff
     */
    private static String archivoTif;
    /**
     * Ruta completa del archivo
     */
    private String tempPath;
    /**
     * Ruta completa del archivo codificado
     */
    private static String archivoCodificado;
    /**
     * Directorio temporal
     */
    private File dirTemporal;
    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(ToolsFiles.class);

    /**
     * Constructor, crea el directorio temporarl, arma las rutas del los archivo
     * tif y codificado e inicializa las propiedades
     */
    public ToolsFiles() {

        String rutaTemp = ManejoSesion.getConfiguracion().getPathTmp(); //ManejoSesion.getPropedades().getProperty("pathTmp");
        String userdir = System.getProperty("user.dir");
        dirTemporal = new File(userdir + "/" + rutaTemp);

        if (!dirTemporal.exists()) {
            dirTemporal.mkdir();
        }

        tempPath = userdir + "/" + rutaTemp + "/";
        archivoTif = userdir + "/" + rutaTemp + "/" + ManejoSesion.getConfiguracion().getFileTif();//ManejoSesion.getPropedades().getProperty("fileTif");
        archivoCodificado = userdir + "/" + rutaTemp + "/" + ManejoSesion.getConfiguracion().getFileCode();//ManejoSesion.getPropedades().getProperty("fileCode");
        fileImageTif = new File(archivoTif);
        
    }

    /**
     * Abre un archivo tiff
     *
     * @param filename Ruta del archivo
     * @return Una lista con bufer de imagenes (BufferedImage)
     * @throws IOException Lanza un IOException si no consigue el archivo
     */
    public ArrayList<BufferedImage> open(String filename) throws IOException, IndexOutOfBoundsException, OutOfMemoryError {

        ArrayList<BufferedImage> bufferImagenesTiff = new ArrayList<BufferedImage>();
        ImageInputStream iis;
        File archivo;
        ImageReader imageReader;
//        TIFFImageReader imageReader;
        Iterator it;
        String ext="tiff";
        long t1, t2, dif;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4:30"));
        Calendar tiempoInicio = Calendar.getInstance();
        Calendar tiempoFinal;
        GregorianCalendar tiempo = new GregorianCalendar();

        int cantPag, i;

//        try {

            t1 = tiempoInicio.getTimeInMillis();
            traza.trace("abriendo la imagen " + filename, Level.INFO);            

//            ext = filename.substring(filename.lastIndexOf('.') + 1);
//            readers = ImageIO.getImageReadersByFormatName(ext);
//            it = ImageIO.getImageReadersByFormatName("tiff");
            it = ImageIO.getImageReadersByMIMEType("image/tiff");

            if (!it.hasNext()) {
                throw new IOException(getClass().getName() + ".open:\n\tNo reader for format '" + ext + "' available.");
            }

//            imageReader = (TIFFImageReader) it.next();
            imageReader = (ImageReader) it.next();
            traza.trace("ImageReader " + imageReader.getClass().getName(), Level.INFO);
            traza.trace("ImageReader FormatName " + imageReader.getFormatName(), Level.INFO);

            archivo = new File(filename);
            iis = ImageIO.createImageInputStream(archivo);
            traza.trace("ImageInputStream: " + iis, Level.INFO);

            imageReader.setInput(iis);
            cantPag = imageReader.getNumImages(true);
            traza.trace("Numero de paginas: " + cantPag, Level.INFO);

            for (i = 0; i < cantPag; i++) {
//                traza.trace("pagina numero " + i, Level.INFO);
                bufferImagenesTiff.add(imageReader.read(i));
            }
            iis.close();

            tiempoFinal = Calendar.getInstance();
            t2 = tiempoFinal.getTimeInMillis();
            dif = t2 - t1;
            tiempo.setTimeInMillis(dif);
            traza.trace("Tiempo en abrir la imagen " + sdf.format(tiempo.getTime()), Level.INFO);

//        } catch (IndexOutOfBoundsException ioobe) {
//            traza.trace("fuera del indice, abriendo imagen ", Level.ERROR, ioobe);
//        } catch (OutOfMemoryError e) {
//            traza.trace("error de memoria ", Level.ERROR, e);
//        } catch (IOException e) {
//            traza.trace("error al abrir la imagen", Level.ERROR, e);
//            traza.trace("9\b" + getClass().getName() + ".open:\n\t" + filename, Level.ERROR, e);
//            JOptionPane.showMessageDialog(new JFrame(), "Problemas al abrir el documento\n" + e.getMessage(), "Alerta...", JOptionPane.ERROR_MESSAGE);
//        }
        return bufferImagenesTiff;
    }

    private IIOImage getIIOImage(ImageWriter writer, ImageWriteParam iwp, BufferedImage image) {
        ImageTypeSpecifier it = ImageTypeSpecifier.createFromRenderedImage(image);

        /*
         try{
         uk.co.mmscomputing.imageio.bmp.BMPMetadata md=(uk.co.mmscomputing.imageio.bmp.BMPMetadata)image.getProperty("iiometadata");
         if(md!=null){
         md.setXPixelsPerMeter(11812);    // force 300 dpi for bmp images
         md.setYPixelsPerMeter(11812);    // works only with mmsc.bmp package
         }
         }catch(Exception e){}
         */
        IIOMetadata md;
        Object obj = image.getProperty("iiometadata");               // if image is a TwainBufferedImage get metadata
        if ((obj != null) && (obj instanceof IIOMetadata)) {
            md = (IIOMetadata) obj;
        } else {
            md = writer.getDefaultImageMetadata(it, iwp);
        }
        return new IIOImage(image, null, md);
    }

    /**
     * Guarda un archivo escaneado
     *
     * @param imageBuffe
     * @return Verdadero si lo guardo, falso en caso contrario
     */
    public boolean save(List<BufferedImage> imageBuffe) {

        boolean scaneado = false;
        int cantImage = imageBuffe.size();
        long startTime = (new java.util.Date()).getTime();

        traza.trace("se escanearon " + cantImage + " documentos", Level.INFO);

//        Iterator writers=ImageIO.getImageWritersByFormatName("tiff");
//        ImageWriter writer=(ImageWriter) writers.next();
        ImageWriter writer = (ImageWriter) ImageIO.getImageWritersByFormatName("tiff").next();
        ImageWriteParam iwp = writer.getDefaultWriteParam();
        IIOParamController controller = iwp.getController();

        if (controller != null) {
            controller.activate(iwp);
        }

        long time = System.currentTimeMillis();

        if (fileImageTif.exists()) {
            fileImageTif.delete();
        }

        ImageOutputStream ios;
        try {
            ios = ImageIO.createImageOutputStream(fileImageTif);
            writer.setOutput(ios);

            if (writer.canWriteSequence()) {  //i.e tiff,sff(fax)
                writer.prepareWriteSequence(null);
                for (int i = 0; i < cantImage; i++) {
                    writer.writeToSequence(getIIOImage(writer, iwp, imageBuffe.get(i)), iwp);
                }
                writer.endWriteSequence();
            } else {
                for (int i = 1; i < cantImage; i++) {
                    if (writer.canInsertImage(i)) {
                        writer.write(null, getIIOImage(writer, iwp, imageBuffe.get(i)), iwp);
                    } else {
                        throw new IOException("Image Writer cannot append image [" + i + "] (" + fileImageTif.getName() + ")");
                    }
                }
            }
            ios.close();
            scaneado = true;
            time = System.currentTimeMillis() - time;
            //System.out.println("Saved : "+filename);
            //System.out.println("3\bTime used to save images : "+time);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(new JFrame(), "Problemas al salvar las imagenes escaneadas\n" + e.getMessage(), "Alerta...", JOptionPane.ERROR_MESSAGE);
            traza.trace("Error al salvar las imagenes escaneadas: " + fileImageTif.getName(), Level.ERROR, e);
        }
        long endTime = (new java.util.Date()).getTime();
        double diffTime2 = (endTime - startTime) * 0.001;
        if (diffTime2 > 60000) {
            diffTime2 = diffTime2 / 60;
            traza.trace("tiempo en abrir el archivo " + diffTime2 + " min", Level.INFO);
        }
        traza.trace("tiempo en abrir el archivo " + (diffTime2 * 0.001) / 60 + " min", Level.INFO);
        return scaneado;
    }

    /**
     * Guarda un archivo escaneado
     *
     * @param imageBuffer Lista de bufer de imagen (BufferedImage) del archivo
     * tif
     * @return Verdadero si lo guardo, falso en caso contrario
     */
    public boolean save2(List<BufferedImage> imageBuffer) {
        traza.trace("salvando la imagen", Level.INFO);
        TIFFImageWriteParam tiffiwp;

        boolean scaneado = false;
        ImageOutputStream imgOupStr;
        ImageWriteParam iwp;
        IIOParamController controller;
        int size;

        if (!imageBuffer.isEmpty()) {
            try {
                ImageWriter writer = (ImageWriter) ImageIO.getImageWritersByMIMEType("image/tiff").next();
                //Iterator writers=ImageIO.getImageWritersByFormatName("tiff");
                //ImageWriter writer=(ImageWriter)writers.next();
                tiffiwp = (TIFFImageWriteParam) writer.getDefaultWriteParam();

                if (tiffiwp.canWriteCompressed()) {
                    //tiffiwp.setPhotometricInterpretation("YCbCr");
                    tiffiwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    tiffiwp.setCompressionType("CCITT T.6");
                    tiffiwp.setCompressionQuality(0.7f);
                }
                if (tiffiwp.canWriteTiles()) {
                    tiffiwp.setTilingMode(ImageWriteParam.MODE_DISABLED);
                }

//                tiffiwp.setPhotometricInterpretation("YCbCr");
//                tiffiwp.setCompressionType("jpeg");
//                tiffiwp.setCompressionQuality(0.5f);
                controller = tiffiwp.getController();
                //            if(controller!=null){
                //                controller.activate(tiffiwp);
                //            }

                //            iwp = writer.getDefaultWriteParam();
                //            controller = iwp.getController();
                //            if(controller!=null){
                //                controller.activate(iwp);
                //            }
                long time = System.currentTimeMillis();
                traza.trace("tiempo antes de salvar la imagen " + time, Level.INFO);

                imgOupStr = ImageIO.createImageOutputStream(fileImageTif);
                writer.setOutput(imgOupStr);

                size = imageBuffer.size();
                traza.trace("se escanearon " + size + " documentos", Level.INFO);

                if (writer.canWriteSequence()) {                               //i.e tiff,sff(fax)
                    writer.prepareWriteSequence(null);

                    for (int i = 0; i < size; i++) {
                        //writer.writeToSequence(getIIOImage(writer,iwp,imageBuffer.get(i)),iwp);
                        writer.writeToSequence(getIIOImage(writer, tiffiwp, imageBuffer.get(i)), tiffiwp);
                    }

                    writer.endWriteSequence();
                } else {

                    for (int i = 1; i < size; i++) {

                        if (writer.canInsertImage(i)) {
                            //writer.write(null,getIIOImage(writer,iwp,imageBuffer.get(i)),iwp);
                            writer.write(null, getIIOImage(writer, tiffiwp, imageBuffer.get(i)), tiffiwp);
                        } else {
                            throw new IOException("Image Writer cannot append image [" + i + "] (" + fileImageTif.getName() + ")");
                        }
                    }
                }
                long tim = System.currentTimeMillis() - time;
                traza.trace("tiempo despues de salvar la imagen " + tim, Level.INFO);
                time = time - tim;

                scaneado = true;
                imgOupStr.close();
                traza.trace("tiempo usado para salvar la imagen es de: " + time + " milisegundos", Level.INFO);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(new JFrame(), "Problemas con el Escaner\n" + e.getMessage(), "Alerta...", JOptionPane.ERROR_MESSAGE);
                traza.trace("Error al salvar el archivo escaneado: " + fileImageTif.getName(), Level.ERROR, e);

            }
        }
        return scaneado;
    }

    public void guardarArchivoTIFFtoPDF() {
        JFileChooser save = new JFileChooser();
        int resp;
        File filePDF = new File("documento.pdf");

        try {
            System.gc();

            save.addChoosableFileFilter(new FileNameExtensionFilter("archivos PDF", "pdf", "*.pdf"));
            save.setSelectedFile(filePDF);

            resp = save.showSaveDialog(null);

            if (resp == JFileChooser.APPROVE_OPTION) {

                File fileSave = save.getSelectedFile();

                int ext = fileSave.getName().indexOf(".");
                if (ext == -1) {
                    //Properties pro = System.getProperties();
                    //String sep = System.getProperty("file.separator");
                    //int path = fileSave.getPath().indexOf(sep);
                    String nombre = fileSave.getName();
                    int name = fileSave.getPath().indexOf(nombre);
                    String ruta = fileSave.getPath().substring(0, name);

                    //nombre = fileSave.getName();
                    nombre = ruta + nombre + ".pdf";
                    fileSave = new File(nombre);
                }

                Document document = new Document();

                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileSave));
                writer.setStrictImageSequence(true);
                document.open();

                InputStream is = new FileInputStream(getArchivoTif());

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) > 0) {
                    byteArrayOutputStream.write(bytes, 0, len);
                }

                byte[] byteArray = byteArrayOutputStream.toByteArray(); //get bytes array
                RandomAccessFileOrArray ra = new RandomAccessFileOrArray(byteArray);
                int pages = TiffImage.getNumberOfPages(ra); // get page count
                float pageWidth = document.getPageSize().getWidth();

                for (int i = 1; i <= pages; i++) {
                    Image image = TiffImage.getTiffImage(new RandomAccessFileOrArray(byteArray), i); //new
                    float imageWidth = image.getWidth();
                    if (pageWidth < imageWidth) {
                        image.scalePercent((pageWidth - 50) / imageWidth * 100);
                    }
                    image.setAlignment(Image.MIDDLE);
                    document.add(image);
                    document.newPage();
                }
                document.close();

                if (fileSave.exists()) {
                    JOptionPane.showMessageDialog(new JFrame(), "Archivo guardado con exito en la ruta\n" + fileSave, "Información", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "Problema al guardar el archivo", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }

        } catch (DocumentException ex) {
            traza.trace("problemas al guradar el archivo", Level.ERROR, ex);
        } catch (IOException ex) {
            traza.trace("error al escribir el archivo", Level.ERROR, ex);
        }
    }

    public void guardarArchivoJPGtoPDF(File fileJPG) {
        JFileChooser save = new JFileChooser();
        int resp;
        File filePDF = new File("documento.pdf");

        try {
            save.addChoosableFileFilter(new FileNameExtensionFilter("archivos PDF", "pdf", "*.pdf"));
            save.setSelectedFile(filePDF);

            resp = save.showSaveDialog(null);

            if (resp == JFileChooser.APPROVE_OPTION) {

                File fileSave = save.getSelectedFile();

                int ext = fileSave.getName().indexOf(".");
                if (ext == -1) {
                    //Properties pro = System.getProperties();
                    //String sep = System.getProperty("file.separator");
                    //int path = fileSave.getPath().indexOf(sep);
                    String nombre = fileSave.getName();
                    int name = fileSave.getPath().indexOf(nombre);
                    String ruta = fileSave.getPath().substring(0, name);

                    //nombre = fileSave.getName();
                    nombre = ruta + nombre + ".pdf";
                    fileSave = new File(nombre);
                }

                Document document = new Document();

                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileSave));
                writer.setStrictImageSequence(true);
                document.open();

                InputStream is = new FileInputStream(getTempPath() + fileJPG.toString());

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) > 0) {
                    byteArrayOutputStream.write(bytes, 0, len);
                }

                byte[] byteArray = byteArrayOutputStream.toByteArray(); //get bytes array
                RandomAccessFileOrArray ra = new RandomAccessFileOrArray(byteArray);
                //int pages = TiffImage.getNumberOfPages(ra); // get page count
                float pageWidth = document.getPageSize().getWidth();

                Image image = Image.getInstance(getTempPath() + fileJPG.toString());
                float imageWidth = image.getWidth();
                if (pageWidth < imageWidth) {
                    image.scalePercent((pageWidth - 50) / imageWidth * 100);
                }
                image.setAlignment(Image.MIDDLE);
                document.add(image);
                document.newPage();

                document.close();

//                Document convertJpgToPdf = new Document();
//                PdfWriter.getInstance(convertJpgToPdf, new FileOutputStream(fileSave));
//                convertJpgToPdf.open();
//                Image convertJpg = Image.getInstance(getTempPath() + fileJPG.toString());
//                convertJpgToPdf.add(convertJpg);
//                convertJpgToPdf.close();
                if (fileSave.exists()) {
                    JOptionPane.showMessageDialog(new JFrame(), "Archivo guardado con exito en la ruta\n" + fileSave, "Información", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "Problema al guardar el archivo", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }

            }

        } catch (DocumentException ex) {
            traza.trace("problemas al guradar el archivo", Level.ERROR, ex);
        } catch (MalformedURLException ex) {
            traza.trace("mal URL", Level.ERROR, ex);
        } catch (IOException ex) {
            traza.trace("error al escribir el archivo", Level.ERROR, ex);
        }

    }

    public void guardarArchivoPDF(File file) {
        JFileChooser save = new JFileChooser();
        int resp;
        File filePDF = new File("documento.pdf");

        save.addChoosableFileFilter(new FileNameExtensionFilter("archivos PDF", "pdf", "*.pdf"));
        save.setSelectedFile(filePDF);

        resp = save.showSaveDialog(null);

        if (resp == JFileChooser.APPROVE_OPTION) {

            File fileSave = save.getSelectedFile();

            int ext = fileSave.getName().indexOf(".");
            if (ext == -1) {
                //Properties pro = System.getProperties();
                //String sep = System.getProperty("file.separator");
                //int path = fileSave.getPath().indexOf(sep);
                String nombre = fileSave.getName();
                int name = fileSave.getPath().indexOf(nombre);
                String ruta = fileSave.getPath().substring(0, name);

                //nombre = fileSave.getName();
                nombre = ruta + nombre + ".pdf";
                fileSave = new File(nombre);
            }

            copiarArchivo(file, fileSave);

        }

    }

    public void copiarArchivo(File origen, File destino) {
        //String fileName = origen.getName();
        byte[] buf = new byte[1024];
        //byte[] buf = new byte[10485760];
        int i = 0;
        int x;
        int fileSize = (int) origen.length();

        traza.trace("archivo origen " + origen, Level.INFO);
        traza.trace("archivo destino " + destino, Level.INFO);

        try {
            InputStream leyendo = new FileInputStream(origen);
            OutputStream escribiendo = new FileOutputStream(destino);

            int vueltas = fileSize / 10485760 + 1;

            x = 100 / vueltas;
            if (vueltas == 1) {
                i = 100;
            }
            int len;
            while ((len = leyendo.read(buf)) > 0) {
                traza.trace("vuelta numero " + i, Level.INFO);
                i += x;
                escribiendo.write(buf, 0, len);
            }

            leyendo.close();
            escribiendo.flush();
            escribiendo.close();

        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(new JFrame(), "Archivo: \n" + ex + "\n no encontrado para copiar", "Error", 0);
            this.traza.trace("archivo no encontrado para copiar", Level.ERROR, ex);
        } catch (IOException ex) {
            this.traza.trace("error en el proceso de copiado", Level.ERROR, ex);
        } catch (Exception e) {
            this.traza.trace("error general en el proceso de copiado", Level.ERROR, e);
        }

    }

//    public void clean() {
//        Runtime.getRuntime().runFinalization();
//        Runtime.getRuntime().gc();
//    }
    /**
     * Codifica el archivo
     */
    public void codificar() {
        new CodDecodArchivos().codificar(archivoTif, archivoCodificado);
//        clean();
    }

    /**
     * Codifica el archivo
     *
     * @param file El archivo a codificar
     */
    public void codificar(File file) {
        new CodDecodArchivos().codificar(file.getAbsolutePath(), archivoCodificado);
//        clean();
    }

    /**
     * Decodifica el archivo
     */
    public void decodificar() {
        new CodDecodArchivos().decodificar(archivoCodificado, archivoTif);
//        clean();
    }

    public void decodificar(String nombre) {
        new CodDecodArchivos().decodificar(archivoCodificado, tempPath + nombre);
//        clean();
    }

    /**
     * Se obtiene el archivo codificado
     *
     * @return El archivo codificado
     */
    public String getArchivoCodificado() {
        traza.trace("ruta archivo codificado " + archivoCodificado, Level.INFO);
        return archivoCodificado;
    }

    /**
     * Se obtiene el archivo tiff
     *
     * @return El archivo tif
     */
    public String getArchivoTif() {
        traza.trace("ruta archivo tif " + archivoTif, Level.INFO);
        return archivoTif;
    }

    public String getTempPath() {
        traza.trace("ruta temporal " + tempPath, Level.INFO);
        return tempPath;
    }

    /**
     * Se obtiene el directorio temporal
     *
     * @return El directorio temporal
     */
    public File getDirTemporal() {
        return dirTemporal;
    }
}
