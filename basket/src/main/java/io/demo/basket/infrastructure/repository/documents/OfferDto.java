package io.demo.basket.infrastructure.repository.documents;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


import javax.validation.constraints.Positive;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Document
public class OfferDto {

    @Positive
    @Field
    private Integer quantity = null;

    @Field
    private String productCode = null;

    @Field
    private boolean available;

    @Field
    private MoneyDto unitPrice;

    @Field
    private String name;



}
