package ng.codehaven.cdc.models;

import io.realm.RealmObject;


public class Car extends RealmObject {
    private String objectId;
    private String brandName;
    private String model;
    private String year;
    private int newPrice;
    private int usedPrice;

    public Car () {
    }

    public String getObjectId () {
        return objectId;
    }

    public void setObjectId (String objectId) {
        this.objectId = objectId;
    }

    public String getBrandName () {
        return brandName;
    }

    public void setBrandName (String brandName) {
        this.brandName = brandName;
    }

    public String getModel () {
        return model;
    }

    public void setModel (String model) {
        this.model = model;
    }

    public String getYear () {
        return year;
    }

    public void setYear (String year) {
        this.year = year;
    }

    public int getNewPrice () {
        return newPrice;
    }

    public void setNewPrice (int newPrice) {
        this.newPrice = newPrice;
    }

    public int getUsedPrice () {
        return usedPrice;
    }

    public void setUsedPrice (int usedPrice) {
        this.usedPrice = usedPrice;
    }
}