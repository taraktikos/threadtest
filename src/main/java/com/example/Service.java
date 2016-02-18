package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@Component
public class Service {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    PlatformTransactionManager transactionManager;

    @Async
    public void analyze() {
        System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " thread started");
        TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());

		jdbcTemplate.update("CREATE temp table thread (name VARCHAR(255));");
		jdbcTemplate.update("INSERT into thread (name) VALUES (?);", Thread.currentThread().getName());
        System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " trans created");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select name from thread");
        System.out.println(Thread.currentThread().getName() + " select count: " + maps.size());
        System.out.println(Thread.currentThread().getName() + " select name: " + maps.get(0).get("name"));

        transactionManager.commit(transaction);

        System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " thread end");
    }
}
