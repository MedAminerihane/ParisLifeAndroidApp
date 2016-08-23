package com.Amine.Project.parislife.entity;

public class Category {
    String categoryName = "";
    private boolean checked = false;

    public Category(String categoryName, boolean checked) {

        this.categoryName = categoryName;
        this.checked = checked;

    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
