package edu.umn.contactview;

import java.util.Iterator;
import java.util.List;


/**
 * Created by aeldred on 2/24/15.
 *
 * This defines the json that is returned from the contacts.tinyapollo.com
 */
public class ServiceResult {
    private String status;
    private String message;
    private Group group;
    private List<Contact> contacts;
    private Contact contact;

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }



    @Override
    public String toString() {
        String value = "";
            //comment

        try {
            Iterator i = this.getContacts().iterator();

            while (i.hasNext()) {
                Contact curr = (Contact) i.next();
                String line = "[ID: " + curr.get_id() + "]\n[NAME: " + curr.getName() + "]\n" +
                        "[EMAIL: " + curr.getEmail() + "]\n[TITLE: " + curr.getTitle() + "]\n" +
                        "[PHONE: " + curr.getPhone() + "]\n[GROUP: " + curr.getGroupId() + "]\n" +
                        "[TWITTER: " + curr.getTwitterId() + "]\n\n";
                value = value + line;
            }
        } catch (Exception e) {
                Contact curr = this.getContact();
                String line = "[ID: " + curr.get_id() + "]\n[NAME: " + curr.getName() + "]\n" +
                        "[EMAIL: " + curr.getEmail() + "]\n[TITLE: " + curr.getTitle() + "]\n" +
                        "[PHONE: " + curr.getPhone() + "]\n[GROUP: " + curr.getGroupId() + "]\n" +
                        "[TWITTER: " + curr.getTwitterId() + "]\n\n";
                value = value + line;
        }
        value = value + "[message: " + message + "]";
        value = value + "[status: " + status + "]";
        value = value + "[group: " + group + "]";
        return value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public class Group {
        private String key;
        private String name;
        private String _id;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }
    }

}

