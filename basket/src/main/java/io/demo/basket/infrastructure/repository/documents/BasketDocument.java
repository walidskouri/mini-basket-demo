package io.demo.basket.infrastructure.repository.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "basket")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BasketDocument {

    @Id
    @Field
    @NotNull
    private String customerLogin;

    @Field
    private MoneyDto totalOffersMonetaryAmount;

    @Field
    private Integer offersCount;

    @Field
    private OffsetDateTime creationDate;

    @Field
    private OffsetDateTime lastModified;

    @Version
    @Field
    private long version;

    @Field
    private List<OfferDto> offers = new ArrayList<>();

}
