package dev.jagan.productservicewithtesting.services;

import dev.jagan.productservicewithtesting.exceptions.ProductNotFoundException;
import dev.jagan.productservicewithtesting.models.Category;
import dev.jagan.productservicewithtesting.models.Product;
import dev.jagan.productservicewithtesting.repositories.CategoryRepository;
import dev.jagan.productservicewithtesting.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("selfProductService")
public class SelfProductService implements ProductService{

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public SelfProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getSingleProduct(Long productId) throws ProductNotFoundException {
        return productRepository.findByIdIs(productId);
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(String title, String description, String category, double price, String image) {
        Product product = new Product();
        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);
        product.setImageUrl(image);

        Category categoryFromDatabase = categoryRepository.findByTitle(category);


        if (categoryFromDatabase == null) {
            Category newCategory = new Category();
            newCategory.setTitle(category);
//            categoryFromDatabase = categoryRepository.save(newCategory);
            categoryFromDatabase = newCategory;
//            category1 = new Category();
//            category1.setTitle(category);
        }

        // if the category was found from DB -> category1 will be having an ID
        // else: category1 won't have any ID
        product.setCategory(categoryFromDatabase);

        Product savedProduct = productRepository.save(product);

        return savedProduct;
    }
}
