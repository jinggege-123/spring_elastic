package com.liangjing.service;

import com.liangjing.pojo.Product;
import org.springframework.data.domain.Page;

public interface ProductService {

    Product getById(Integer id);

    Integer delete(Integer id);

    Integer save(Product product);

    Integer update(Product product);

    Page<Product> getPage(Integer num, Integer size);
}
