package io.demo.products;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j

public class ProductsApplication {



    public static void main(String[] args) {
        SpringApplication.run(ProductsApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner demoData(ProductRepository repo) throws IOException {
//        Flux<String> stringFlux = fromPath(Path.of(resourceLoader.getResource("classpath:data.csv").getURI()));
//        return args -> repo.deleteAll()
//                .thenMany(stringFlux.map(line -> convertToProduct(line)).flatMap(p -> repo.save(p))).blockFirst();
//    }



}
