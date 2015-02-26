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

    public Contact GetContact(String mId)
    {
        Contact mContact = new Contact();
        mContact.setName("Yo mamma!");
        mContact.setPhone("555-555-5555");
        mContact.setEmail("yomamma@g.com");
        mContact.setTitle("the boss");
        mContact.setTwitterId("@yomamma");
        mContact.set_id(mId);

        return mContact;
    }

    public void UpdateContact(String mId, Contact mContact)
    {
        // Update the JSON data
    }
}
