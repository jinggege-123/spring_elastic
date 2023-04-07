package com.liangjing.service.impl;

import com.liangjing.dao.ProductDao;
import com.liangjing.pojo.Product;
import com.liangjing.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author hewei
 * @date 2023/4/7 10:35
 */
@Service
public class ProductServiceImpl implements ProductService {


    @Autowired
    private ProductDao productDao;

    @Override
    public Product getById(Integer id) {
        Optional<Product> byId = productDao.findById(id);
        return byId.orElse(null);

    }

    @Override
    public Integer delete(Integer id) {
        productDao.deleteById(id);
        return id;
    }

    @Override
    public Integer save(Product product) {
        Product save = productDao.save(product);
        return save.getId();
    }

    @Override
    public Integer update(Product product) {
        productDao.save(product);
        return product.getId();
    }

    @Override
    public Page<Product> getPage(Integer num, Integer size) {
        PageRequest of = PageRequest.of(0, 10);
        Page<Product> all = productDao.findAll(of);
        return all;
    }
}
