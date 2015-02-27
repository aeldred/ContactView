package edu.umn.contactview;

import android.app.Activity;
import android.content.Context;
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
import java.util.ListIterator;

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
        //(aContext.getClass().cast(activity).updateAdapter(contacts);

        if (!isPopulated) {
            if (aContext instanceof MainActivity) {
                activity = (MainActivity)aContext;
            }
            ourInstance = new ContactManager();
            isPopulated = Boolean.TRUE;
        } else {
            if (aContext instanceof MainActivity) {
                activity = (MainActivity)aContext;
                activity.updateAdapter(contacts);
            }
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

                //Log.w("GetContactTask", "Retrieved contacts\n" + result.toString());
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

    public Contact GetContact(String mId)
    {
        try {
            return getContactById(mId);
        }
        catch (Exception ex) {
            Log.w("GetContact","No Contact with id=[" + mId + "]");
            return null;
        }
    }

    public void UpdateContact(String mId, Contact mContact)
    {
        // Update the JSON data


    }

    public Contact getContactById(String id) {
        Iterator<Contact> itr = contacts.iterator();

        while (itr.hasNext()) {
            Contact curr = itr.next();
            if (curr.equals(id)) {
                return curr;
            }
        }
        return null;
    }

    public void DeleteContact(String sid){
        Contact mContact = new Contact();
        contacts.remove(sid);
    }
}
