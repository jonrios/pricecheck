package com.jrios.pricecheck.model;

/**
 * Created by rios on 30/01/2015.
 */
public class ProductDTO {

    private String productname;

    public ProductDTO(String productname) {
        this.productname = productname;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }
}
