package com.nab.icommerce.products.repositories;

import com.nab.icommerce.products.entities.Product;
import com.nab.icommerce.products.models.ConditionDefinition;
import com.nab.icommerce.products.services.ProductFilterStrategy;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;

public interface ProductItemRepository extends CrudRepository<Product, String> {
    List<Product> findAll(Map<String, ConditionDefinition> filterCondition, ProductFilterStrategy filterStrategy);
}
