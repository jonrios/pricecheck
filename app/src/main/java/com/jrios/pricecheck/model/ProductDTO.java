package com.jrios.pricecheck.model;

/**
 * Created by rios on 30/01/2015.
 */
public class ProductDTO {
    private int id;
    private String productName;
    private String upc;
    private int productSize;
    private int productSizeUnit;

    public static final int UNIT_PIECE = 0;
    public static final int UNIT_KG = 1;
    public static final int UNIT_G = 2;



    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductSize() {
        return productSize;
    }

    public void setProductSize(int productSize) {
        this.productSize = productSize;
    }

    public int getProductSizeUnit() {
        return productSizeUnit;
    }

    public void setProductSizeUnit(int productSizeUnit) {
        this.productSizeUnit = productSizeUnit;
    }

    public String getProductSizeText(){
        String rv = productSize+" ";

        switch(productSizeUnit){
            case UNIT_PIECE:
                if(productSize > 1)
                    rv += "piece";
                else
                    rv += "pieces";
                break;
            case UNIT_KG:
                rv += "kg";
                break;
            case UNIT_G:
                rv += "g";

        }

        return rv;
    }
}
