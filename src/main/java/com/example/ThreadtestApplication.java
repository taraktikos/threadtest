package com.example;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.sql.DataSource;
import java.util.concurrent.CyclicBarrier;

@EnableAsync
@SpringBootApplication
public class ThreadtestApplication implements CommandLineRunner {

    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.url}")
    private String dataSourceUrl;
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Bean
    public DataSource redshiftDataSource() {
//		HikariDataSource dataSource = (HikariDataSource) DataSourceBuilder.create().build();
//        dataSource.setJdbcUrl(dataSourceUrl);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(dataSourceUrl);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Autowired
    Service service;

//    final CyclicBarrier gate = new CyclicBarrier(8);

    @Override
    public void run(String... strings) throws Exception {
        for (int i = 0; i < 5; i++) {
            service.analyze();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ThreadtestApplication.class, args);
    }

}
