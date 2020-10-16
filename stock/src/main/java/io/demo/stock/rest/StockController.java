package io.demo.stock.rest;

import io.demo.stock.model.ProductStockInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping(value = "/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Slf4j
public class StockController {

    @GetMapping(value = "/stock/{product_code}")
    public ResponseEntity<ProductStockInfo> getStock(@PathVariable(name = "product_code") String productCode) {
        int sleepRandomMS = new Random().nextInt(400);
        // try {
        //     log.info("Sleeping for {}  ms", sleepRandomMS);
        //     Thread.sleep(sleepRandomMS);
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }

        // if (sleepRandomMS % 2 == 0) {
        //     throw new ResourceAccessException("Random number " + sleepRandomMS + " is even !!");
        // }

        return ResponseEntity.ok().body(ProductStockInfo.builder().isProductAvailable(true).productCode(productCode).quantityAvailable(sleepRandomMS).build());
    }

}
