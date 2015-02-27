package edu.umn.contactview;

/**
 * Created by aeldred on 2/13/15.
 */
public class Contact {
    private String name;
    private String email;
    private String title;
    private String phone;
    private String twitterId;
    private String _id;
    private String groupId;

    //static = method doesn't operate on individual instance of class

/*    public Contact(String name, String title, String phone) {
        this.name = name;
        this.title = title;
        this.phone = phone;
    }
*/

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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Boolean equals (String id) {
        if (this._id.equals(id)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
