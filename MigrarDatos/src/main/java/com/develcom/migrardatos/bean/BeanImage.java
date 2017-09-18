/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.migrardatos.bean;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.ServletContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Soaint210TQF
 */
@Component
@ManagedBean
@ViewScoped
public class BeanImage {

    private static final Logger LOG = LoggerFactory.getLogger(BeanImage.class);

    private DefaultStreamedContent content;

    public StreamedContent getContent() {

        try {
            
            String image = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/resources/image/ajax-loader.gif");
            
            LOG.info("ruta imagen "+image);

            InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/image/ajax-loader.gif");

            if (content == null) {
                /* use your database call here */
                BufferedInputStream in = new BufferedInputStream(is);
//            BufferedInputStream in = new BufferedInputStream(BeanImage.class.getClassLoader().getResourceAsStream("test/test.png"));
                ByteArrayOutputStream out = new ByteArrayOutputStream();

                int val = -1;
                /* this is a simple test method to double check values from the stream */
                try {
                    while ((val = in.read()) != -1) {
                        out.write(val);
                    }
                } catch (IOException e) {
                    LOG.error("error ", e);
                }

                byte[] bytes = out.toByteArray();
                LOG.info("Bytes -> " + bytes.length);
                content = new DefaultStreamedContent(new ByteArrayInputStream(bytes), "image/png", "test.png");
            }
        } catch (Exception e) {
            LOG.error("error al cargar la imagen", e);
        }

        return content;
    }
}
