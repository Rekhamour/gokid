package com.gokids.yoda_tech.gokids.ecommerce.model;

/**
 * Created by Lenovo on 10/14/2017.
 */
public class EcommercProductBean {
 private String ProductCategoryID;
    private String  ProductCategory;
    private String  ImageURL;
    private String  ProductClassID;
    private String  ProductClass;


    public String getProductClass() {
        return ProductClass;
    }

    public void setProductClass(String productClass) {
        ProductClass = productClass;
    }

    public String getProductCategoryID() {
        return ProductCategoryID;
    }

    public void setProductCategoryID(String productCategoryID) {
        ProductCategoryID = productCategoryID;
    }

    public String getProductCategory() {
        return ProductCategory;
    }

    public void setProductCategory(String productCategory) {
        ProductCategory = productCategory;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getProductClassID() {
        return ProductClassID;
    }

    public void setProductClassID(String productClassID) {
        ProductClassID = productClassID;
    }
}
