package com.nab.icommerce.products.repositories;

import com.nab.icommerce.products.constants.ProductTableMetadata;
import com.nab.icommerce.products.entities.Product;
import com.nab.icommerce.products.models.ComparisonOperands;
import com.nab.icommerce.products.models.ConditionDefinition;
import com.nab.icommerce.products.services.ProductFilterByQuery;
import com.nab.icommerce.products.services.ProductFilterByScan;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@SpringBootTest
class ProductItemRepositoryImplTest {

    @Autowired
    public ProductItemRepositoryImpl itemRepositoryImp;

    @Test
    @Disabled
    void findAllByCategory() {
        Map<String, ConditionDefinition> filter = new HashMap<>();
        filter.put(ProductTableMetadata.FIELD_CATEGORY, new ConditionDefinition(ComparisonOperands.EQUALS, "food" ));
        List<Product> products = this.itemRepositoryImp.findAll(filter, new ProductFilterByQuery());
        products.forEach(p -> System.out.println(p.getName()));
    }

    @Test
    @Disabled
    void findAllByPrice() {
        Map<String, ConditionDefinition> filter = new HashMap<>();
        filter.put(ProductTableMetadata.FIELD_PRICE, new ConditionDefinition(ComparisonOperands.GREATER_THAN, "150" ));
        List<Product> products = this.itemRepositoryImp.findAll(filter, new ProductFilterByScan());
        products.forEach(p -> System.out.println(p.getName()));
    }
}