package dev.jagan.productservicewithtesting.controllers;

import dev.jagan.productservicewithtesting.commons.AuthenticationCommons;
import dev.jagan.productservicewithtesting.dtos.CreateProductRequestDto;
import dev.jagan.productservicewithtesting.dtos.Role;
import dev.jagan.productservicewithtesting.dtos.UserDto;
import dev.jagan.productservicewithtesting.exceptions.ProductNotFoundException;
import dev.jagan.productservicewithtesting.models.Product;
import dev.jagan.productservicewithtesting.services.ProductService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {
    //    private Map<Integer, Integer> mp = new TreeMap<>();
//    List<Integer> li = new ArrayList<>();

    private ProductService productService;
    private RestTemplate restTemplate;
    private AuthenticationCommons authenticationCommons;

//    private ProductService productService2 = new FakeStoreProductService();


    public ProductController(@Qualifier("fakeStoreProductService") ProductService productService,
                             RestTemplate restTemplate,
                             AuthenticationCommons authenticationCommons
    ) {
        this.productService = productService;
        this.restTemplate = restTemplate;
        this.authenticationCommons = authenticationCommons;
    }

// private ProductService productService;

    // POST /products
    // RequestBody
    // {
    //     title:
    //     description:
    //     price:
    //     category: "mobile"
    // }
    @PostMapping("/products")
    public Product createProduct(@RequestBody CreateProductRequestDto request) {
        return productService.createProduct(
                request.getTitle(),
                request.getDescription(),
                request.getCategory(),
                request.getPrice(),
                request.getImage()
        );
    }

    // GET /products/1
    // GET /products/201

    @GetMapping("/products/{id}")
    public Product getProductDetails(@PathVariable("id") Long productId) throws ProductNotFoundException {
        Product response =  productService.getSingleProduct(productId);
        return response;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() throws ProductNotFoundException {

        // I want to call user service
        // Client side load balancing
        UserDto userDto = restTemplate.getForObject("http://userService/users/1" ,
                UserDto.class);
        // we have the user from the user service


        List<Product> products = productService.getProducts();

//        List<Product> productListSample = new ArrayList<>();
//        Product extraProduct = new Product();
//        extraProduct.setPrice(250);
//        extraProduct.setTitle("pencil");
//        extraProduct.setDescription("Utility");
//
//        productListSample.add(extraProduct);

//        throw new ProductNotFoundException("Bla bla bla");

        ResponseEntity<List<Product>> response = new ResponseEntity<>(products, HttpStatus.OK);
        return response;
    }

    // For Authentication Product
    @GetMapping("/products/{token}")
    public ResponseEntity<List<Product>> getAllProducts(@PathVariable("token") @NonNull String token){
        // validate the token first
        UserDto userDto = authenticationCommons.validateToken(token);

        if (userDto == null){
            // the token is valid, it's a forbidden request
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        boolean isAdmin = false;
        for(Role role : userDto.getRoles()){
            if (role.getName().equals("Admin")){
                    isAdmin = true;
            }
        }

        if (!isAdmin){
            // auth failed, not allowed
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        //only admins are allowed to go to this / products/
        // handle Auth part here?

        List<Product> products = productService.getProducts();
        return new ResponseEntity<>(products, HttpStatus.ACCEPTED);

    }

    public void updateProduct() {

    }

//    @ExceptionHandler(ProductNotFoundException.class)
//    public ResponseEntity<ErrorDto> handleProductNotFoundExeption(ProductNotFoundException exception) {
//
//        ErrorDto errorDto = new ErrorDto();
//        errorDto.setMessage(exception.getMessage());
//
//        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
////        return null;
//    }

    // Limited to only the exceptions thrown from this controller
    // Controller Advices: Global

    // if this controller ever ends up throwing ProductNotFoundException.class
    // for any reason, don't throw that exception as is.
    // Instead call this method and return what this method is returning
}

// allProducts - X
// all APIs should be structured around resources
// GET  /products
