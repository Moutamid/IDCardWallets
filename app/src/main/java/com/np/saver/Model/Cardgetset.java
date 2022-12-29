package com.np.saver.Model;

public class Cardgetset {
    String card = "";
    String data;
    String description;
    int id = 0;
    byte[] image_back = null;
    byte[] image_front = null;
    String name = "";

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String str) {
        this.data = str;
    }

    public String getCard() {
        return this.card;
    }

    public void setCard(String str) {
        this.card = str;
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

    public byte[] getImage_front() {
        return this.image_front;
    }

    public void setImage_front(byte[] bArr) {
        this.image_front = bArr;
    }

    public byte[] getImage_back() {
        return this.image_back;
    }

    public void setImage_back(byte[] bArr) {
        this.image_back = bArr;
    }
}
