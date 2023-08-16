package com.example.demo.product.application.command;

public class ProductRequestCommand {
    public record command(String name, int price) {
        public command{
            assert !name.isBlank();
            assert price > 0;
        }
    }
}

