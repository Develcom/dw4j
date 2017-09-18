package com.develcom.migrardatos.config;

import com.develcom.migrardatos.bean.BusquedaBean;
import com.develcom.migrardatos.pojo.Combo;
import com.develcom.migrardatos.pojo.DatoAdicionalTipoDocumento;
import com.develcom.migrardatos.pojo.DatosInfoDocumento;
import com.develcom.migrardatos.pojo.Expediente;
import com.develcom.migrardatos.pojo.Indice;
import com.develcom.migrardatos.pojo.InfoDocumento;
import com.develcom.migrardatos.util.AppJDBC;
import com.develcom.migrardatos.util.Propiedades;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.develcom.migrardatos")
public class AppConfig {

    @Autowired
    private Propiedades propiedades;

    @Bean
    public BusquedaBean busquedaBean() {
        return new BusquedaBean();
    }

    @Bean
    public Expediente expediente() {
        return new Expediente();
    }

    @Bean
    public Indice indice() {
        return new Indice();
    }

    @Bean
    public DatoAdicionalTipoDocumento datoAdicionalTipoDocumento() {
        return new DatoAdicionalTipoDocumento();
    }

    @Bean
    public Combo combo() {
        return new Combo();
    }

    @Bean
    public InfoDocumento infoDocumento() {
        return new InfoDocumento();
    }

    @Bean
    public DatosInfoDocumento datosInfoDocumento() {
        return new DatosInfoDocumento();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        Properties prop = propiedades.configuracionBaseDatos();
        String url = prop.getProperty("urlbasedato") + prop.getProperty("srvbasedato") + ":"
                + prop.getProperty("portbasedato") + "/" + prop.getProperty("basedato");

        dataSource.setDriverClassName(prop.getProperty("driverpostgres"));
        dataSource.setUrl(url);
        dataSource.setUsername(prop.getProperty("userbasedato"));
        dataSource.setPassword(prop.getProperty("passbasedato"));

        return dataSource;
    }

//    @Bean
//    public SimpleDriverDataSource dataSource() {
//        
//        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
//        Properties prop = propiedades.configuracionBaseDatos();
//
//        dataSource.setDriver(new org.postgresql.Driver());
//        dataSource.setUrl("jdbc:postgresql://" + prop.getProperty("srvbasedato") + ":" + prop.getProperty("portbasedato")
//                + "/" + prop.getProperty("basedato"));
//        dataSource.setUsername(prop.getProperty("userbasedato"));
//        dataSource.setPassword("passbasedato");
//
//        return dataSource;
//    }
    @Bean
    public AppJDBC appJDBC() {
        return new AppJDBC(dataSource());
    }

}
