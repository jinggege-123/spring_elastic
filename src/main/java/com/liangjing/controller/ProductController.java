package com.liangjing.controller;

import com.liangjing.dao.ProductDao;
import com.liangjing.pojo.Product;
import com.liangjing.service.ProductService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hewei
 * @date 2023/4/7 10:34
 */

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public List<Product> getList(){
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("name", "脑"))
                .should(QueryBuilders.matchQuery("desc", "脑"));
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withHighlightFields(
                        new HighlightBuilder.Field("name")
                        , new HighlightBuilder.Field("desc"))
                .withHighlightBuilder(
                        new HighlightBuilder()
                                .preTags("<span style='color:red'>")
                                .postTags("</span>")).build();
        SearchHits<Product> search = restTemplate.search(build, Product.class);
        System.out.println(search.toString());
        List<SearchHit<Product>> searchHits = search.getSearchHits();
        ArrayList<Product> products = new ArrayList<>();
        for (SearchHit<Product> searchHit : searchHits) {
            //高亮的内容
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
            //将高亮的内容填充到content中
            searchHit.getContent().setName(highlightFields.get("name") == null ? searchHit.getContent().getName() : highlightFields.get("name").get(0));
            searchHit.getContent().setDesc(highlightFields.get("desc") == null ? searchHit.getContent().getDesc() : highlightFields.get("desc").get(0));
            //放到实体类中
            products.add(searchHit.getContent());
        }

        return products;
    }
}
