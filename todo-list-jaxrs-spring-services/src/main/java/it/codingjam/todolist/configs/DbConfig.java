package it.codingjam.todolist.configs;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.config.Task;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManagerFactory;
import javax.transaction.TransactionManager;

/**
 * Database configuration providers
 */
@Configuration
@EnableTransactionManagement
public class DbConfig {

    @Value("${db.driver}")
    private String driverClass;

    @Value("${db.url}")
    private String url;

    @Value("${db.username}")
    private String userName;

    @Value("${db.password}")
    private String password;

    @Value("${db.initialSize}")
    private int initialSizePool;

    @Value("${db.maxActive}")
    private int maxActiveConnections;

    @Value("${db.persistence.unit}")
    private String persistenceUnitName;

    @Value("${db.show.sql}")
    private boolean showSql;

    @Value("${db.generate.ddl}")
    private boolean generateDdl;

    @Value("${db.dialect}")
    private String platform;


    @Bean(destroyMethod = "close")
    public DataSource dataSource() {

        DataSource ds = new DataSource();
        ds.setDriverClassName(this.driverClass);
        ds.setUrl(this.url);
        ds.setUsername(this.userName);
        ds.setPassword(this.password);
        ds.setInitialSize(this.initialSizePool);
        ds.setMaxActive(this.maxActiveConnections);
        ds.setTestOnBorrow(true);
        ds.setValidationQuery("SELECT 1");
        ds.setTestWhileIdle(true);
        ds.setTimeBetweenEvictionRunsMillis(500000);

        return ds;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPersistenceUnitName(this.persistenceUnitName);
        emf.setJpaVendorAdapter(getAdapter());
        emf.setPackagesToScan(String.valueOf(Task.class.getPackage()));

        return emf;
    }

    @Bean
    public TransactionTemplate transactionTemplate(EntityManagerFactory emf) {
        TransactionTemplate tt = new TransactionTemplate();
        tt.setTransactionManager(transactionManager(emf));

        return tt;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

    private JpaVendorAdapter getAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(this.showSql);
        adapter.setGenerateDdl(this.generateDdl);
        adapter.setDatabasePlatform(this.platform);

        return adapter;
    }
}
