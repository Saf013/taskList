package task.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.dialect.PostgreSQL10Dialect;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Configuration
@PropertySource("classpath:/config/database.properties")
@ComponentScan({"task.persistence","task.service", "task.controller", "task.util", "task.filter"})
@EnableJpaRepositories(entityManagerFactoryRef = "emf", basePackages = {"task.persistence.jpa.repository"})
@EnableTransactionManagement
public class RootConfig {

    @Bean
    public DataSource dataSource(@Value("${database.password}") String password,
                                 @Value("${database.username}") String userName,
                                 @Value("${database.url}") String ulr,
                                 @Value("${database.name}") String dataBaseName,
                                 @Value("${database.minimumIdle}") String minIdle,
                                 @Value("${database.maximumPoolSize}") String maxPoolSize
                                 ) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setPassword(password);
        hikariConfig.setUsername(userName);
        hikariConfig.setJdbcUrl(ulr);
        hikariConfig.addDataSourceProperty("databaseName",dataBaseName);
        hikariConfig.setMinimumIdle(Integer.parseInt(Objects.requireNonNull(minIdle)));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(Objects.requireNonNull(maxPoolSize)));
        hikariConfig.setDataSourceClassName(PGSimpleDataSource.class.getName());
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean emf(
            DataSource dataSource,
            @Value("${hibernate.hbm2ddl}") String hbm2ddl,
            @Value("${hibernate.show.sql}") String showSql,
            @Value("${hibernate.query_timeout}") int timeout
    ) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("task.persistence.entity");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Properties properties = new Properties();
        properties.put("hibernate.dialect", PostgreSQL10Dialect.class.getName());
        properties.put("hibernate.hbm2ddl.auto", hbm2ddl);
        properties.put("hibernate.show_sql", showSql);
        properties.put("javax.persistence.query.timeout", timeout);
        emf.setJpaProperties(properties);
        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory, DataSource dataSource) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
