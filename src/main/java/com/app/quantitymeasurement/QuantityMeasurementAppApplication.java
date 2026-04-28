package com.app.quantitymeasurement;
 
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
 
@SpringBootApplication  // = @Configuration + @EnableAutoConfiguration + @ComponentScan
@OpenAPIDefinition(info = @Info(
    title = "Quantity Measurement API",
    version = "1.0",
    description = "REST API for quantity measurement operations (UC17)"
))
public class QuantityMeasurementAppApplication {
 
    public static void main(String[] args) {
        SpringApplication.run(QuantityMeasurementAppApplication.class, args);
        // This boots the embedded Tomcat on port 8080
        // Spring scans all @Component, @Service, @Repository, @Controller beans
        // JPA auto-creates tables from @Entity classes
    }
}
