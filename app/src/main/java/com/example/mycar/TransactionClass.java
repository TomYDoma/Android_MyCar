package com.example.mycar;

public class TransactionClass {
    private int amount;
    private String date;
    private String message;
    private  String comment;
    private boolean positive;

    public TransactionClass(int amount, String message, String dt, String cm, boolean positive) {
        this.amount = amount;
        this.date = dt;
        this.message = message;
        this.comment = cm;
        this.positive = positive;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    public String getDate() {
        return date;
    }

    public void setDate() {
        this.date = date;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String message) {
        this.comment = comment;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isPositive() {
        return positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
    }
}
