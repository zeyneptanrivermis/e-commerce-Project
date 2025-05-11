package com.example.ecommerce_api.dto.ProductDTO;

public class ReviewNotAllowedException extends RuntimeException {
    public ReviewNotAllowedException() {
        super("Bu ürünü satın almadığınız veya henüz teslim almadığınız için yorum yapamazsınız.");
    }
    public ReviewNotAllowedException(String message) {
        super(message);
    }
}