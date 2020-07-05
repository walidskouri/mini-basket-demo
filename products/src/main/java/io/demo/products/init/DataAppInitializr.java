package io.demo.products.init;

import io.demo.products.models.Product;
import io.demo.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Date;
import java.util.stream.BaseStream;

import static io.demo.products.utils.MoneyUtils.convertToMoney;

@Component
@Profile(value = "!test")
@RequiredArgsConstructor
@Slf4j
public class DataAppInitializr {

    private final ResourceLoader resourceLoader;
    private final ProductRepository repo;

    @EventListener(ApplicationReadyEvent.class)
    public void run() throws IOException {
        Date start = new Date();
        repo.deleteAll()
                .thenMany((csvFileToFlux(Path.of(resourceLoader.getResource("classpath:data.csv").getURI()))
                        .map(this::lineToProduct))
                        .flatMap(repo::save))
                .subscribe(
                        saved -> log.info("Inserted Product {} into database ", saved),
                        throwable -> log.error("Error inserting Document into database {}", throwable.getMessage()),
                        () -> log.info("Database Initialisation done in {}ms", Duration.between(start.toInstant(), new Date().toInstant()).toMillis())
                );
    }

    private Product lineToProduct(String line) {
        String[] split = line.split(";");
        String price = split[2];
        return new Product(null, split[1], split[0], convertToMoney(price));
    }

    private static Flux<String> csvFileToFlux(Path path) {
        return Flux.using(() -> Files.lines(path),
                Flux::fromStream,
                BaseStream::close
        );
    }

}
