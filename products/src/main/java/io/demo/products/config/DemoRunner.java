package io.demo.products.config;

import io.demo.products.models.Product;
import io.demo.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.BaseStream;

@Component
@Profile(value = "!test")
@RequiredArgsConstructor
public class DemoRunner implements CommandLineRunner {

    private final ResourceLoader resourceLoader;
    private final ProductRepository repo;

    @Override
    public void run(String... args) throws Exception {
        Flux<String> stringFlux = fromPath(Path.of(resourceLoader.getResource("classpath:data.csv").getURI()));
        repo.deleteAll()
                .thenMany(stringFlux.map(line -> convertToProduct(line)).flatMap(p -> repo.save(p)))
                .blockFirst();
    }

    private Product convertToProduct(String line) {
        String[] split = line.split(";");
        return new Product(null, split[1]);

    }

    private static Flux<String> fromPath(Path path) {
        return Flux.using(() -> Files.lines(path),
                Flux::fromStream,
                BaseStream::close
        );
    }

}
