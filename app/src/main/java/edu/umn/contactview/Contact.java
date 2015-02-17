package edu.umn.contactview;

/**
 * Created by aeldred on 2/13/15.
 */
public class Contact {

    //static = method doesn't operate on individual instance of class
    public static Contact[] getAll() {
        //for demonstrative purposes only - do not use in homework
        //data needs to be persisted in homework assignment
        return new Contact[] {
                new Contact("Malcolm Reynolds","Captain","555-2345"),
                new Contact("Jayne Cobb", "Muscle","555-4567"),
                new Contact("Jim Rowe","Executive","555-2345"),
                new Contact("Kyle Mirth","Sales","555-8765")
        };
    }

    public Contact(String name, String title, String phone) {
        this.name = name;
        this.title = title;
        this.phone = phone;
    }
    private String name;
    private String email;
    private String title;
    private String phone;
    private String twitterId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }
}
