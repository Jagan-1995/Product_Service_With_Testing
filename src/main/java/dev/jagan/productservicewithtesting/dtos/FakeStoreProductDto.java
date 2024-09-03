package dev.jagan.productservicewithtesting.dtos;

import dev.jagan.productservicewithtesting.models.Category;
import dev.jagan.productservicewithtesting.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeStoreProductDto {
    private Long id;
    private String title;
    private String image;
    private String description;
    private String category;
    private double price;

    public Product toProduct() {
        Product product = new Product();
        product.setId(id);
        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);
        product.setImageUrl(image);

        Category productCategory = new Category();
        productCategory.setTitle(category);

//        product.setCategory(productCategory);

        return product;
    }
}
