package ng.codehaven.cdc.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Thompson on 13/04/2015.
 */
public class Favorite extends RealmObject {

    @PrimaryKey
    private String objectId;

    private String cet, description;
    private int levy, vat, duty;

    private Date createdAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCet() {
        return cet;
    }

    public void setCet(String cet) {
        this.cet = cet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLevy() {
        return levy;
    }

    public void setLevy(int levy) {
        this.levy = levy;
    }

    public int getVat() {
        return vat;
    }

    public void setVat(int vat) {
        this.vat = vat;
    }

    public int getDuty() {
        return duty;
    }

    public void setDuty(int duty) {
        this.duty = duty;
    }
}
