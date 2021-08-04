package com.nab.icommerce.products;

import com.nab.icommerce.products.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, String> {
}
