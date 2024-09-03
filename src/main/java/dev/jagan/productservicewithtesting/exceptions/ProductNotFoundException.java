package dev.jagan.productservicewithtesting.exceptions;

public class ProductNotFoundException extends Exception{
    public ProductNotFoundException(String message) {
        super(message);
    }
}
