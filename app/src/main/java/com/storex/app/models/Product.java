package com.storex.app.models;

public class Product {

    private int id;
    private String name;
    private String description;
    private double price;
    private String category; // "shop" or "games"
    private String emoji;
    private int imageRes; // R.drawable.xxx - صورة المنتج الحقيقية

    public Product(int id, String name, String description, double price, String category, String emoji, int imageRes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.emoji = emoji;
        this.imageRes = imageRes;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public String getEmoji() { return emoji; }
    public int getImageRes() { return imageRes; }
}
