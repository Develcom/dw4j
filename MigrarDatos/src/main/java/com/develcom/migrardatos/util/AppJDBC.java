/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.migrardatos.util;

import java.sql.Types;
import java.util.Date;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

/**
 *
 * @author Soaint210TQF
 */
@Component
public class AppJDBC {

    private JdbcTemplate jdbcTemplate;

    public AppJDBC(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public SqlRowSet buscar(String sql) {

//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        SqlRowSet srs;

        srs = jdbcTemplate.queryForRowSet(sql);

        return srs;
    }

    public int agregarRegistro(String insertSql, Object[] params) {

        int row;
        
        // define query arguments
//        Object[] params = new Object[]{name, surname, title, new Date()};

        // define SQL types of the arguments
//        int[] types = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP};

        // execute insert query to insert the data
        // return number of row / rows processed by the executed query
//        int row = jdbcTemplate.update(insertSql, params, types);
        row = jdbcTemplate.update(insertSql, params);

        System.out.println(row + " row inserted.");
        
        return row;

    }
}
