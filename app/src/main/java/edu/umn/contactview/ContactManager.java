package edu.umn.contactview;

/**
 * Created by ryan on 2/21/15.
 *
 * This where we will interface with the database
 */
public class ContactManager {
    private static ContactManager ourInstance = new ContactManager();

    public static ContactManager getInstance() {
        return ourInstance;
    }

    private ContactManager() {
    }
}
