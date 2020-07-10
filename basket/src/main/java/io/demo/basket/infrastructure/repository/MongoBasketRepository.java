package io.demo.basket.infrastructure.repository;

import io.demo.basket.infrastructure.repository.documents.BasketDocument;
import io.demo.basket.infrastructure.util.logging.CallType;
import io.demo.basket.infrastructure.util.logging.TraceMethodCall;
import io.demo.basket.infrastructure.util.logging.TracingConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MongoBasketRepository implements BasketRepository {

    private final BasketRepositoryDelegate basketRepositoryDelegate;

    @Override
    @TraceMethodCall(params = "{" + TracingConstant.USER_LOGIN + ": #userLogin}", type = CallType.DATABASE)
    public Optional<BasketDocument> findById(String userLogin) {
        return basketRepositoryDelegate.findById(userLogin);
    }

    @Override
    @TraceMethodCall(params = "{" + TracingConstant.USER_LOGIN + ": #basketDocument.customerLogin}", type = CallType.DATABASE)
    public BasketDocument save(BasketDocument basketDocument) {
        return basketRepositoryDelegate.save(basketDocument);
    }
}
