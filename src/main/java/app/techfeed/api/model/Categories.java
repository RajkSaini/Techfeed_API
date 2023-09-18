package app.techfeed.api.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Categories")
public class Categories {

    private String categoryId;

    private String name;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
