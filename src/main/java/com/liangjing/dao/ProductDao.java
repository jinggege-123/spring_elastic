package com.liangjing.dao;


import com.liangjing.pojo.Product;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao extends ElasticsearchRepository<Product,Integer> {

    List<Product> findByDesc(String desc);

    @Highlight(fields = {
            @HighlightField(name = "name")
    })
    List<Product> findByName(String name);


}
