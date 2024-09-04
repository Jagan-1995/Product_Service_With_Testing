package dev.jagan.productservicewithtesting.controllers;

import dev.jagan.productservicewithtesting.exceptions.ProductNotFoundException;
import dev.jagan.productservicewithtesting.models.Product;
import dev.jagan.productservicewithtesting.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductControllerTest {

    @Autowired
    private ProductController productController;

    @MockBean
    @Qualifier("fakeStoreProductService")
    private ProductService productService;

    @Test
    void createProduct() {
        productService.getProducts();
    }

    @Test
    void getProductDetailsNegative() throws ProductNotFoundException {
        when(productService.getSingleProduct(1000L))
                .thenThrow(ProductNotFoundException.class);

        assertThrows(
                ProductNotFoundException.class,
                () -> productController.getProductDetails(1000L)
        );
    }

    @Test
    void getProductDetailsPositive() throws ProductNotFoundException {
        Product product = new Product();
        product.setTitle("mouse");
        product.setDescription("accessories");
        product.setPrice(909);

        when(productService.getSingleProduct(anyLong())).thenReturn(product);

//        when(productService.getSingleProduct(100L)).thenReturn(new Product());

        Product actualResponse = productController.getProductDetails(100L);

        assertEquals(product,
                actualResponse,
                "Product details from service doesn't matches with what we are getting from controller");
    }

    @Test
    void getAllProducts() throws ProductNotFoundException {
        // Call the mocked product service to get list of products
        Product p1 = new Product();
        p1.setTitle("Iphone");
        p1.setDescription("des1");
        p1.setPrice(500);

        Product p2 = new Product();
        p2.setTitle("Jungle Book");
        p2.setDescription("book");
        p2.setPrice(300);

        Product p3 = new Product();
        p3.setTitle("iPad");
        p3.setDescription("onePlus");
        p3.setPrice(600);

        List<Product> productList = new ArrayList<>();
        productList.add(p1);
        productList.add(p2);
        productList.add(p3);


        when(
                productService.getProducts()
        ).thenReturn(productList);

        // Actual Response
        ResponseEntity<List<Product>> responseEntity = productController.getAllProducts();
        List<Product> response = responseEntity.getBody();

        assertEquals(productList.size(), response.size());
        assertEquals(productList, response);
    }

    @Test
    void updateProduct() {
    }
}