/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.cliente;

//import com.develcom.bind.Bufer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
/**
 *
 * @author Soaint210TQF
 */
public class Cliente {

    public void buscarArchivo() throws FileNotFoundException, IOException {

        CodDecodArchivos cda = new CodDecodArchivos();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
//        Bufer bufer = new Bufer();
        byte[] buffer = null;
        ResponseEntity<byte[]> response;

        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
        headers.setAccept(Arrays.asList(MediaType.ALL));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        response = restTemplate.exchange("http://localhost:8080/ServicioDW4J/expediente/buscarFisicoDocumento/21593",
                HttpMethod.GET, entity, byte[].class);

        if (response.getStatusCode().equals(HttpStatus.OK)) {
            buffer = response.getBody();
            FileOutputStream output = new FileOutputStream(new File("c:/documentos/archivo.cod"));
            IOUtils.write(response.getBody(), output);
            cda.decodificar("c:/documentos/archivo.cod", "c:/documentos/archivo.pdf");
        }

    }

    public static void main(String[] args) throws IOException {

        Cliente cba = new Cliente();
        cba.buscarArchivo();
    }
    
}
