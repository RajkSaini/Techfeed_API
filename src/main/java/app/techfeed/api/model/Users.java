package app.techfeed.api.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Users")
public class Users {
    private String id;
    private String emailId;

    private String[] categories;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }
}
