package io.demo.basket.infrastructure.repository;


import io.demo.basket.domain.model.basket.Basket;
import io.demo.basket.domain.spi.BasketPersistencePort;
import io.demo.basket.infrastructure.mapper.dto.DocumentToServiceMapper;
import io.demo.basket.infrastructure.mapper.dto.ServiceToDocumentMapper;
import io.demo.basket.infrastructure.repository.documents.BasketDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasketPersistenceAdapter implements BasketPersistencePort {

    private final BasketRepository basketRepository;
    private final DocumentToServiceMapper documentToServiceMapper;
    private final ServiceToDocumentMapper serviceToDocumentMapper;


    @Override
    public Optional<Basket> getBasket(String userLogin) {
        Optional<BasketDocument> optionalBasketDocument = basketRepository.findById(userLogin);
        if (optionalBasketDocument.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(documentToServiceMapper.documentToServiceBasket(optionalBasketDocument.get()));
    }

    @Override
    public Basket saveBasket(Basket basket, String userLogin) {
        Optional<BasketDocument> optionalBasketDocument = basketRepository.findById(userLogin);
        BasketDocument basketDocument = serviceToDocumentMapper.toBasketDocument(basket);
        optionalBasketDocument.ifPresent(document -> basketDocument.setVersion(document.getVersion()));
        return documentToServiceMapper.documentToServiceBasket(basketRepository.save(basketDocument));
    }
}
