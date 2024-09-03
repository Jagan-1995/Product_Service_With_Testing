package dev.jagan.productservicewithtesting.services;

import dev.jagan.productservicewithtesting.exceptions.ProductNotFoundException;
import dev.jagan.productservicewithtesting.models.Product;

import java.util.List;

public interface ProductService {

    Product getSingleProduct(Long productId) throws ProductNotFoundException;

    List<Product> getProducts();

    Product createProduct(String title,
                          String description,
                          String category,
                          double price,
                          String image);
}
