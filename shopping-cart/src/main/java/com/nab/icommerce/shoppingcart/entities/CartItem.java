package com.nab.icommerce.shoppingcart.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;

@DynamoDBTable(tableName = "iCommerce-Shopping-Cart")
public class CartItem {
    @Getter
    @Setter
    @Id
    @DynamoDBGeneratedUuid(DynamoDBAutoGenerateStrategy.CREATE)
    private String uuid;

    @Getter
    @Setter
    @DynamoDBHashKey (attributeName = "UserId")
    private String userId;

    @Getter
    @Setter
    @DynamoDBAttribute (attributeName = "productUuid")
    private String productUuid;

    @Getter
    @Setter
    @DynamoDBRangeKey(attributeName = "AddedDate")
    private long addedDate = DateTime.now().getMillis();

    @Getter
    @Setter
    @DynamoDBAttribute (attributeName = "Quantity")
    private int quantity = 1;
}
