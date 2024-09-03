package dev.jagan.productservicewithtesting.services;

import dev.jagan.productservicewithtesting.dtos.FakeStoreProductDto;
import dev.jagan.productservicewithtesting.exceptions.ProductNotFoundException;
import dev.jagan.productservicewithtesting.models.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service("fakeStoreProductService")
public class FakeStoreProductService implements ProductService{

    // RestTemplate
    // - allows to send HTTP requests to external URLs
    //    and work with responses

    private RestTemplate restTemplate;

    public FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getSingleProduct(Long productId) throws ProductNotFoundException {

        ResponseEntity<FakeStoreProductDto> fakeStoreProductResponse = restTemplate.getForEntity(
                "https://fakestoreapi.com/products/" + productId,
                FakeStoreProductDto.class
        );

//
//        if (fakeStoreProductResponse.getStatusCode() != HttpStatusCode.valueOf(200)) {
//
//        }

//        fakeStoreProductResponse.getHeaders().

        FakeStoreProductDto fakeStoreProduct = fakeStoreProductResponse.getBody();

        if (fakeStoreProduct == null) {
            throw new ProductNotFoundException("Product with id: " + productId + " doesn't exist. Retry some other product.");
        }

        return fakeStoreProduct.toProduct();
    }

    @Override
    public List<Product> getProducts() {
        FakeStoreProductDto[] fakeStoreProducts =
                restTemplate.getForObject(
                        "https://fakestoreapi.com/products",
                        FakeStoreProductDto[].class
                );

        List<Product> products = new ArrayList<>();

        for (FakeStoreProductDto fakeStoreProduct: fakeStoreProducts) {
            products.add(fakeStoreProduct.toProduct());
        }

        return products;
    }

    @Override
    public Product createProduct(String title,
                                 String description,
                                 String category,
                                 double price,
                                 String image) {
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setTitle(title);
        fakeStoreProductDto.setCategory(category);
        fakeStoreProductDto.setPrice(price);
        fakeStoreProductDto.setImage(image);
        fakeStoreProductDto.setDescription(description);

        FakeStoreProductDto response = restTemplate.postForObject(
                "https://fakestoreapi.com/products", // url
                fakeStoreProductDto, // request body
                FakeStoreProductDto.class // data type of response
        );

        if (response == null) return new Product();

        return response.toProduct();
    }
}
