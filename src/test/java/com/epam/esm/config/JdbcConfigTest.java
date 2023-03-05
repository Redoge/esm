//package com.epam.esm.config;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.sql.DataSource;
//
//import static org.junit.jupiter.api.Assertions.*;
//@ExtendWith(MockitoExtension.class)
//class JdbcConfigTest {
//    private final JdbcConfig jdbcConfig=new JdbcConfig();
//
//    @Test
//    void dataSource() {
//        assertInstanceOf(DataSource.class, jdbcConfig.dataSource());
//    }
//
//    @Test
//    void jdbcTemplate() {
//        assertInstanceOf(JdbcTemplate.class, jdbcConfig.jdbcTemplate());
//    }
//
//    @Test
//    void transactionManager() {
//        assertInstanceOf(PlatformTransactionManager.class, jdbcConfig.transactionManager());
//    }
//}