package ng.codehaven.cdc.models;

public class ListMenuItem {
    String title;
    int icon;
    boolean isSelected;

    public ListMenuItem(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
