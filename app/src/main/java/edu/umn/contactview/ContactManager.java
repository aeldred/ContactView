package edu.umn.contactview;

import android.content.Context;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ryan on 2/21/15.
 *
 * This where we will interface with the database
 */
public class ContactManager {
    private static ContactManager ourInstance;
    private static MainActivity activity;
    private static Boolean isPopulated = Boolean.FALSE;

    private static List<Contact> contacts;

    public static ContactManager getInstance(Context aContext) {
        if (aContext instanceof MainActivity) {
            activity = (MainActivity)aContext;
        }
        if (!isPopulated) {
            ourInstance = new ContactManager();
            isPopulated = Boolean.TRUE;
        } else {
            activity.updateAdapter(contacts);
        }

        return ourInstance;
    }

    private ContactManager() {
        new GetContactTask().execute();
    }

    public static void clearContactManager() {
        isPopulated = Boolean.FALSE;
    }
    public static List<Contact> getContacts() {
        return contacts;
    }

    //This provides the basic functionality for retrieving all current contacts
    //from web service.
    private class GetContactTask extends AsyncTask<Void, Void, ServiceResult> {
        private String URL_BASE = activity.getString(R.string.URL_BASE);
        private String API_KEY = activity.getString(R.string.API_KEY);
        private ServiceResult result;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ServiceResult doInBackground(Void... params) {
            try {
                AndroidHttpClient client = AndroidHttpClient.newInstance("Android", null);
                HttpUriRequest request = new HttpGet(URL_BASE + "contacts" +
                        "?key=" + API_KEY);
                HttpResponse response = client.execute(request);
                Gson gson = new Gson();
                result = gson.fromJson(
                        new InputStreamReader(response.getEntity().getContent()),
                        ServiceResult.class);
                client.close();
                return result;
            }
            catch (Exception ex) {
                Log.w("GetContactTask", "Error getting contact", ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ServiceResult result) {
            super.onPostExecute(result);

            //String test = result.toString();
            //Log.w("onPostExecute","GSON Result: " + test);

            contacts = result.getContacts();
            activity.updateAdapter(contacts);

        }
    }

    public String GenerateId(){
        if(contacts.size()==0){
            return "1";
        }else{
            long max=0;
            Iterator it = contacts.iterator();
            while (it.hasNext()){
                Contact tempContact=(Contact)it.next();
                long tempId= ContactManager.converter(tempContact.get_id());
                if(tempId > max){
                    max = tempId;
                }
            }
            String idStr=String.valueOf(max+1);
            return idStr;
        }
    }
    public static Long converter(String hexStr){
        // get the value of last 16digit to convert
        hexStr= hexStr.substring(hexStr.length()-16, hexStr.length());
        Long value= Long.valueOf(hexStr, 16);
        return value;
    }

    public Contact GetContact(String mId)
    {
        Iterator it = ContactManager.getContacts().iterator();
        while (it.hasNext()){
            Contact tempContact=(Contact)it.next();
            if( tempContact.get_id().equals(mId) ){
                return tempContact;
            }
        }

        Contact newContact= new Contact();
        newContact.set_id(this.GenerateId());
        newContact.setName("");
        newContact.setPhone("");
        newContact.setEmail("");
        newContact.setTitle("");
        newContact.setTwitterId("");
        return newContact;
        /*
        mContact.setName("Yo mamma!");
        mContact.setPhone("555-555-5555");
        mContact.setEmail("yomamma@g.com");
        mContact.setTitle("the boss");
        mContact.setTwitterId("@yomamma");
        mContact.set_id(mId);

        return mContact;*/
    }

    public void localUpdateContact(String mId, Contact mContact)
    {
        //use hashMap to make it faster...
        Iterator it = ContactManager.getContacts().iterator();
        while (it.hasNext()){
            Contact tempContact=(Contact)it.next();
            if( tempContact.get_id().equals(mId) ){
                tempContact.setName(mContact.getName());
                tempContact.setPhone(mContact.getPhone());
                tempContact.setTitle(mContact.getTitle());
                tempContact.setTwitterId(mContact.getTwitterId());
                return;
            }
        }
        ContactManager.getContacts().add(mContact);
        return;
    }
    public void localDeleteContact(String mId)
    {
        //use hashMap to make it faster...
        Iterator it = ContactManager.getContacts().iterator();
        while (it.hasNext()){
            Contact tempContact=(Contact)it.next();
            if( tempContact.get_id().equals(mId) ){
                it.remove();
                return;
            }
        }
    }
    public void UpdateContact(String mId, Contact mContact)
    {
        // Update the JSON data
    }
}
