package models;

import java.io.Serializable;
import java.util.Objects;

public class Goods implements Serializable {
    private String name;
    private String description;
    private String producer;
    private int count = 0;
    private double price;

    public Goods(String name, String description, String producer, double price) {
        this.name = name;
        this.description = description;
        this.producer = producer;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public int getCount() {
        return count;
    }

    public void increaseCount(int count){
        if (count < 1)
            throw new RuntimeException("Не може прийти менше 1 товару");
        this.count += count;
    }

    public void decreaseCount(int count){
        if (count < 1)
            throw new RuntimeException("Не може списати менше 1 товару");
        if (this.count - count < 0)
            throw new RuntimeException("Не може списати більше " + this.count + " товарів");
        this.count -= count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return name;
    }
}
