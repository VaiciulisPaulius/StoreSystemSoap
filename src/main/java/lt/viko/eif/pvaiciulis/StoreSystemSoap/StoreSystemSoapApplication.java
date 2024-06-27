package lt.viko.eif.pvaiciulis.StoreSystemSoap;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StoreSystemSoapApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreSystemSoapApplication.class, args);
    }
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
