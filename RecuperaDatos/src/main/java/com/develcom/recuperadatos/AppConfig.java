/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.recuperadatos;

import com.develcom.recupera.RecuperaBean;
import com.develcom.recupera.util.AppJDBC;
import com.develcom.recupera.util.Propiedades;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan(basePackages = "com.develcom")
public class AppConfig {

    @Bean
    public Propiedades propiedades() {
        return new Propiedades();
    }

    @Bean
    public RecuperaBean recuperaBean() {
        return new RecuperaBean();
    }

    @Bean
    public AppJDBC appJDBC() {
        return new AppJDBC(dataSource());
    }

    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        Propiedades propiedades = new Propiedades();
        Properties prop = propiedades.buscarProperties();
        
        String url = prop.getProperty("urlbasedato") + prop.getProperty("srvbasedato")+":"
                + prop.getProperty("portbasedato") + "/" + prop.getProperty("basedato");

        dataSource.setDriverClassName(prop.getProperty("driverpostgres"));
        dataSource.setUrl(url);
        dataSource.setUsername(prop.getProperty("userbasedato"));
        dataSource.setPassword(prop.getProperty("passbasedato"));
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        return jdbcTemplate;
    }

}
