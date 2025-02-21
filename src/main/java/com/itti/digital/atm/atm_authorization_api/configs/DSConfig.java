package com.itti.digital.atm.atm_authorization_api.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DSConfig {
    @Bean(name="sqlserverDS")
    @Primary
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name="oracleDS")
    @ConfigurationProperties(prefix="spring.second-datasource")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }
/*
    @Bean(name="tm1")
    DataSourceTransactionManager tm1(@Qualifier("sqlserverDS") DataSource datasource) {
        DataSourceTransactionManager txm  = new DataSourceTransactionManager(datasource);
        return txm;
    }

    @Bean(name="tm2")
    DataSourceTransactionManager tm2(@Qualifier ("oracleDS") DataSource datasource) {
        DataSourceTransactionManager txm  = new DataSourceTransactionManager(datasource);
        return txm;
    }

    @Bean(name = "chainedTransactionManager")
    public ChainedTransactionManager getChainedTransactionManager(@Qualifier ("tm1") DataSourceTransactionManager tm1, @Qualifier ("tm2") DataSourceTransactionManager tm2){
        return new ChainedTransactionManager(tm1, tm2);
    }
*/
    @Bean
    public JdbcTemplate sqlServerJdbcTemplate(@Qualifier("sqlserverDS") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public JdbcTemplate oracleJdbcTemplate(@Qualifier("oracleDS") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
