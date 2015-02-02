package com.jrios.pricecheck.model;

import java.util.List;

/**
 * Created by rios on 01/02/2015.
 */
public interface ProductDAO {
    List<ProductDTO> getProducts();
    ProductDTO getProduct(int id);
    ProductDTO getProduct(String upc);
    List<ProductDTO> getLastCheckedProducts();
    List<ProductDTO> getLastCheckedProducts(int amount);

    void addProduct(ProductDTO product);
    void removeProduct(int id);
    void removeLastCheckedProduct(int id);
}
