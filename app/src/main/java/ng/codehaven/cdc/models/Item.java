package ng.codehaven.cdc.models;

import android.os.Parcel;
import android.os.Parcelable;


public class Item implements Parcelable {
    private String cet, description, objectId;
    private int levy, vat, duty;

    public Item(String cet, String description, String objectId, int levy, int vat, int duty) {
        this.cet = cet;
        this.description = description;
        this.objectId = objectId;
        this.levy = levy;
        this.vat = vat;
        this.duty = duty;
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

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
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

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
