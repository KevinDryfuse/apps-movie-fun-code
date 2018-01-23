package org.superbiz.moviefun;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

import javax.sql.DataSource;

@Configuration
public class DbConfig {

    @Bean
    public DataSource albumsDataSource(
            @Value("${moviefun.datasources.albums.url}") String url,
            @Value("${moviefun.datasources.albums.username}") String username,
            @Value("${moviefun.datasources.albums.password}") String password
    ) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        //return dataSource;
        return hikariDataSource(dataSource);
    }

    @Bean
    public DataSource moviesDataSource(
            @Value("${moviefun.datasources.movies.url}") String url,
            @Value("${moviefun.datasources.movies.username}") String username,
            @Value("${moviefun.datasources.movies.password}") String password
    ) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        //return dataSource;
        return hikariDataSource(dataSource);
    }

    public HikariDataSource hikariDataSource(DataSource dataSource){
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDataSource(dataSource);

        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        return hikariDataSource;
    }


    @Bean
    public HibernateJpaVendorAdapter hibernateJpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();

        hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
        hibernateJpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
        hibernateJpaVendorAdapter.setGenerateDdl(true);

        return hibernateJpaVendorAdapter;
    }

    @Bean
    @Qualifier("movies")
    public LocalContainerEntityManagerFactoryBean moviesLocalContainerEntityManagerFactoryBean(DataSource moviesDataSource, HibernateJpaVendorAdapter hibernateJpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean lCEMFB = new LocalContainerEntityManagerFactoryBean();

        lCEMFB.setDataSource(moviesDataSource);
        lCEMFB.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        lCEMFB.setPackagesToScan(DbConfig.class.getPackage().getName());
        lCEMFB.setPersistenceUnitName("movies");

        return lCEMFB;
    }

    @Bean
    @Qualifier("albums")
    public LocalContainerEntityManagerFactoryBean albumsLocalContainerEntityManagerFactoryBean(DataSource albumsDataSource, HibernateJpaVendorAdapter hibernateJpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean lCEMFB = new LocalContainerEntityManagerFactoryBean();

        lCEMFB.setDataSource(albumsDataSource);
        lCEMFB.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        lCEMFB.setPackagesToScan(DbConfig.class.getPackage().getName());
        lCEMFB.setPersistenceUnitName("albums");

        return lCEMFB;
    }

    @Bean
    public PlatformTransactionManager moviesPlatformTransactionManager(@Qualifier("movies") LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {

        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(localContainerEntityManagerFactoryBean.getObject());
        return  jpaTransactionManager;
    }

    @Bean
    public PlatformTransactionManager albumsPlatformTransactionManager(@Qualifier("albums") LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {

        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(localContainerEntityManagerFactoryBean.getObject());
        return  jpaTransactionManager;
    }

}



