package com.np.saver.Model;

public class UserCat {
    int id = 0;
    byte[] image_icon = null;
    String name = "";

    public UserCat(int id, byte[] image_icon, String name) {
        this.id = id;
        this.image_icon = image_icon;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public byte[] getImage_icon() {
        return image_icon;
    }

    public void setImage_icon(byte[] image_icon) {
        this.image_icon = image_icon;
    }
}
