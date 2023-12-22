package com.example.MangaWebsite.Model;

public class UserInput {


    private Long id; // Giả sử ID người dùng là kiểu Long, điều chỉnh theo cần thiết
    private Double amount; // Giả sử số tiền là kiểu Double, điều chỉnh theo cần thiết

    // Constructors, getters, và setters

    public UserInput() {
        // Constructor mặc định
    }

    public UserInput(Long id, Double amount) {
        this.id = id;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
