package com.liangjing.pojo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

/**
 * @author hewei
 * @date 2023/4/7 10:12
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "product")
public class Product {

    @Id
    @Field(type = FieldType.Integer)
    private Integer id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String price;

    @Field(type = FieldType.Integer)
    private Integer num;

    @Field(type = FieldType.Text)
    private String desc;


}
