package pe.edu.upeu.msmatriculas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient

public class MsMatriculasApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsMatriculasApplication.class, args);
    }

}
