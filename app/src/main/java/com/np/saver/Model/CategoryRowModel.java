package com.np.saver.Model;

public class CategoryRowModel {
    private int id;
    private int imageType;
    private boolean isSelected = false;
    private String name;

    public CategoryRowModel(int i, int i2, String str) {
        this.id = i;
        this.imageType = i2;
        this.name = str;
    }

    public CategoryRowModel(int i) {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public int getImageType() {
        return this.imageType;
    }

    public void setImageType(int i) {
        this.imageType = i;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }
}
