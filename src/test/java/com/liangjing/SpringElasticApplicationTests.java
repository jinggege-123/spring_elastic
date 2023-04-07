package com.liangjing;

import com.liangjing.dao.ProductDao;
import com.liangjing.pojo.Product;
import com.liangjing.service.ProductService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import sun.management.Agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
class SpringElasticApplicationTests {


    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductService productService;

    @Test
    void contextLoads() {
        Product product = productService.getById(1);
        System.out.println(product);
        List<Product> list = productDao.findByDesc("北京");
        System.out.println(list.toString());
    }

    @Test
    void add() {
        Product product = new Product();
        product.setId(2);
        product.setNum(520);
        product.setName("手机");
        product.setPrice("20.23");
        product.setDesc("新鲜的水果，来自北京电脑");

        productService.save(product);
        System.out.println(product);
    }

    @Test
    void update() {
        Product product = new Product();
        product.setId(1);
        product.setName("电脑");
        product.setNum(520);
        product.setPrice("20.23");
        product.setDesc("新鲜的水果，来自北京手机");
        Integer update = productService.update(product);
        System.out.println(update);

    }

    @Test
    void lsit() {
        Page<Product> page = productService.getPage(0, 10);
        page.get().forEach(s-> System.out.println(s));
        System.out.println(page.getTotalPages());
    }

    @Test
    void byName() {

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("name", "脑"))
                .should(QueryBuilders.matchQuery("edsc", "脑"));
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



//        List<Product> list = productDao.findByName("电");
//        System.out.println(list.toString());
    }

}
