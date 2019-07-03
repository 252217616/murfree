package com.qianbao.print;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
})
@ComponentScan(basePackages={"com.qianbao"})
@EnableAsync
public class PrintTradeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrintTradeServiceApplication.class, args);
    }

}
