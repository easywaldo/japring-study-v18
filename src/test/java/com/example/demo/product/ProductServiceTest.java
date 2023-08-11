package com.example.demo.product;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class ProductServiceTest {

    private Map<Integer, Product> productMap = new HashMap<>();

    @DisplayName("상품을 등록하는 테스트 입니다.")
    @Test
    public void 상품등록테스트() {
        // arrange
        ProductRequestCommand productRequestCommand = new ProductRequestCommand("product001", "test");

        // act
        ProductService productService = new ProductService();
        productService.addProduct(productRequestCommand);

        // assert
        assertThat(productMap.size()).isEqualTo(1);
    }

    @DisplayName("상품을 등록 실패 테스트")
    @Test
    public void 상품등록실패테스트() {
        // arrange and assert
        assertThatThrownBy(() -> {
            new ProductRequestCommand("produt002", "");
        }).isInstanceOf(AssertionError.class).hasMessageContaining("Expecting not blank");
    }

    private record Product(String id, String name) {
        private Product{
            Assertions.assertFalse(id.isBlank());
        }
    }

    public record ProductRequestCommand(String id, String name) {
        public ProductRequestCommand{
            assertThat(id).isNotBlank();
            assertThat(name).isNotBlank();
        }
    }

    private class ProductService {
        void addProduct(ProductRequestCommand command) {
            Product product = new Product(command.id, command.name);
            Integer seq = productMap.size();
            productMap.put(++seq, product);
        }
    }
}
