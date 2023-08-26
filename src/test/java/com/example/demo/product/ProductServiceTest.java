package com.example.demo.product;

import com.example.demo.product.application.command.ProductRequestCommand;
import com.example.demo.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;


    private Map<Integer, Product> productMap = new HashMap<>();

    @DisplayName("상품을 등록하는 테스트 입니다.")
    @Test
    public void 상품등록테스트() {
        // arrange
        ProductRequestCommand.command productRequestCommand = new ProductRequestCommand.command("JPA 다루기", 50000);

        // act
        ProductService productService = new ProductService(productRepository);
        Product product = productService.addProduct(productRequestCommand);

        // assert
        Product searchProduct = productRepository.findById(product.getId()).get();
        assertThat(searchProduct.getName()).isEqualTo(product.getName());
    }

    @DisplayName("상품을 등록 실패 테스트")
    @Test
    public void 상품등록실패테스트() {
        // arrange and assert
        assertThatThrownBy(() -> {
            new ProductRequestCommand.command("", 5000);
        }).isInstanceOf(AssertionError.class);
    }

    @DisplayName("상품을 조회하는 테스트")
    @Test
    public void 상품조회테스트() {
        // arrange
        ProductRequestCommand.command productRequestCommand = new ProductRequestCommand.command("코틀린 프레임워크", 40000);
        ProductService productService = new ProductService(productRepository);
        Product product = productService.addProduct(productRequestCommand);

        // act
        Product searchProduct = productService.getProduct(product.getId());

        // assert
        assertThat(searchProduct).isNotNull();
        assertThat(searchProduct.getName()).isEqualTo(productRequestCommand.name());

        // act
        ProductRequestCommand.command productRequestCommand2 = new ProductRequestCommand.command("코틀린 정복하기", 40000);
        productService.addProduct(productRequestCommand2);
        ProductRequestCommand.command productRequestCommand3 = new ProductRequestCommand.command("코틀린 함수형 프로그래밍", 40000);
        productService.addProduct(productRequestCommand3);

        Slice<Product> slicedProductList1 = productService.getProductListByName("코틀린", PageRequest.of(0, 1));
        Slice<Product> slicedProductList2 = productService.getProductListByName("코틀린", PageRequest.of(1, 1));
        Slice<Product> slicedProductList3 = productService.getProductListByName("코틀린", PageRequest.of(2, 1));

        // assert
        assertThat(slicedProductList1).isNotNull();
        assertThat(slicedProductList1.hasNext()).isTrue();

        assertThat(slicedProductList2).isNotNull();
        assertThat(slicedProductList2.hasNext()).isTrue();

        assertThat(slicedProductList3).isNotNull();
        assertThat(slicedProductList3.hasNext()).isFalse();

    }
}
