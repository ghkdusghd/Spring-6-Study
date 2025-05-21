package com.villainscode.service;

import com.villainscode.entity.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    List<Product> productList = new ArrayList<>();

    // 2) 생성자에서 목 데이터 초기화
    public ProductService() {
        productList.add(
                new Product(1L, "Wireless Mouse", new BigDecimal("25.99"),
                        100));
        productList.add(
                new Product(2L, "Mechanical Keyboard", new BigDecimal("89.50"),
                        2));
        productList.add(
                new Product(3L, "27인치 모니터", new BigDecimal("199.99"), 3));
        productList.add(
                new Product(4L, "USB-C 허브", new BigDecimal("34.75"), 4));
        productList.add(new Product(5L, "노트북 스탠드", new BigDecimal("29.00"), 5));
    }

    // 3) findAll() 은 필드 리스트를 반환
    public List<Product> findAll() {
        // 원본 리스트를 직접 반환해도 되고, 불변 복사본을 만들어 반환해도 됩니다.
        return new ArrayList<>(productList);
    }

    @PreAuthorize("hasRole('MANAGER')")
    public void delete(int productId) {
        // id 기준으로 안전하게 제거
        boolean removed = productList.removeIf(
                p -> p.getId().equals(productId));
        if (!removed) {
            throw new NoSuchElementException("해당 ID의 상품이 없습니다: " + productId);
        }
    }
}
