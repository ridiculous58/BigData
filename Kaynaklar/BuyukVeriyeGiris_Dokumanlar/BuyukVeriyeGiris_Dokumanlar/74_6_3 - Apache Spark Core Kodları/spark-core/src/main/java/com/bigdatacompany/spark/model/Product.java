package com.bigdatacompany.spark.model;

public class Product {
    private String first_name;
    private String last_name;
    private String email;
    private String country;
    private long price;
    private String product;

    public Product() {
    }

    public Product(String first_name, String last_name, String email, String country, long price, String product) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.country = country;
        this.price = price;
        this.product = product;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
