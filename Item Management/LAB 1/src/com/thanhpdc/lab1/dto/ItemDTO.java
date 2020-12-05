
package com.thanhpdc.lab1.dto;

public class ItemDTO {
    private String itemCode;
    private String itemName;
    private String itemSupCode;
    private String unit;
    private float price;
    private boolean supplying;
    private String itemSupName;
    
    public ItemDTO() {
    }

    public ItemDTO(String itemCode, String itemName, String itemSupCode, String unit, float price, boolean supplying, String itemSupName) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemSupCode = itemSupCode;
        this.unit = unit;
        this.price = price;
        this.supplying = supplying;
        this.itemSupName = itemSupName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemSupCode() {
        return itemSupCode;
    }

    public void setItemSupCode(String itemSupCode) {
        this.itemSupCode = itemSupCode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isSupplying() {
        return supplying;
    }

    public void setSupplying(boolean supplying) {
        this.supplying = supplying;
    }

    public String getItemSupName() {
        return itemSupName;
    }

    public void setItemSupName(String itemSupName) {
        this.itemSupName = itemSupName;
    }

    
}
