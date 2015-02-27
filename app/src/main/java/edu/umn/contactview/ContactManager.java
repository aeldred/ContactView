package edu.umn.contactview;

import android.app.Activity;
import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

/**
 * Created by ryan on 2/21/15.
 *
 * This where we will interface with the database
 */
public class ContactManager {
    private static ContactManager ourInstance;
    private static MainActivity activity;
    public static enum DataStatus { IS_SYNCED, IS_CHANGED, IS_EMPTY }
    private static ServiceResult result;
    private static DataStatus instanceStatus = DataStatus.IS_EMPTY;

    private static List<Contact> contacts;

    public static ContactManager getInstance(Context aContext) {
        //(aContext.getClass().cast(activity).updateAdapter(contacts);

        switch (instanceStatus) {
            case IS_EMPTY:
                if (aContext instanceof MainActivity) {
                    activity = (MainActivity) aContext;
                }
                ourInstance = new ContactManager();
                instanceStatus = DataStatus.IS_SYNCED;
                break;
            case IS_SYNCED:
                if (aContext instanceof MainActivity) {
                    activity = (MainActivity)aContext;
                    activity.updateAdapter(contacts);
                }
                instanceStatus = DataStatus.IS_SYNCED;
            case IS_CHANGED:
                try {
                    if (activity instanceof MainActivity) {
                        activity.updateAdapter(contacts);
                        instanceStatus = DataStatus.IS_SYNCED;
                    }
                } catch (Exception e) {

                }
        }

        return ourInstance;
    }

    private ContactManager() {
        new GetContactTask().execute();
    }

    public static void clearContactManager() {
        instanceStatus = DataStatus.IS_EMPTY;
    }
    public static List<Contact> getContacts() {
        return contacts;
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

    //This provides the basic functionality for retrieving all current contacts
    //from web service.
    private class GetContactTask extends AsyncTask<Void, Void, ServiceResult> {
        private String URL_BASE = activity.getString(R.string.URL_BASE);
        private String API_KEY = activity.getString(R.string.API_KEY);

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

            try {
                contacts = result.getContacts();
                activity.updateAdapter(contacts);
            } catch (Exception e) {
                Log.w("onPostExecute","Unable to retrieve contacts from result [" + result.toString() + "]");
            }

        }
    }

    // this task will persist our current instance
    private class PersistDataTask extends AsyncTask<String, Void, ServiceResult> {
        private String URL_BASE = activity.getString(R.string.URL_BASE);
        private String API_KEY = activity.getString(R.string.API_KEY);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ServiceResult doInBackground(String... params) {
            String data[] = params[0].split("\\|");

            HttpPost post = null;
            HttpPut put = null;
            HttpDelete delete = null;
            HttpGet get = null;

            try {
                //result.setContacts(contacts);
                AndroidHttpClient client = AndroidHttpClient.newInstance("Android", null);
                //String objJson = new Gson().toJson(result.getContacts());
                //objJson = objJson.replaceFirst("contacts","key\":\"grumpy\",\"contacts");
                //objJson = "key=grumpy&" + objJson;
//                Log.w("PersistDataTask","TESTING DATA: " + data[0] + "\nURL: " + data[1]);
                //ArrayList<NameValuePair> nameValPair = new ArrayList();
                //nameValPair.add(new BasicNameValuePair("JSON",objJson));

                int index = data[1].indexOf("?");
                String base_url = URL_BASE + "contacts" + data[1].substring(0,index);
                String encoded = URLEncoder.encode(data[1].substring(index+1),"UTF-8");
                String url = base_url + "?" + encoded;

                //String encoded = URLEncoder.encode(data[1], "UTF-8");
                switch (data[0]) {
                    case "PST":
                        post = new HttpPost(url);
                        break;
                    case "PUT":
                        put = new HttpPut(url);
                        break;
                    case "DEL":
                        delete = new HttpDelete(url);
                        break;
                    default:
                        get = new HttpGet(url);
                        break;
                }

                HttpResponse resp = null;
                try{
                    if (put != null) { resp = client.execute(put); }
                    else if (post != null) {resp = client.execute(post); }
                    else if (delete != null) {resp = client.execute(delete); }
                    else { resp = client.execute(get); }

                   result = new Gson().fromJson(
                            new InputStreamReader(resp.getEntity().getContent()),
                            ServiceResult.class);


                    Log.w("PersistDataTask", ""+resp.getStatusLine().getStatusCode());
                } catch (Exception e){
                    e.printStackTrace();
                }

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

            instanceStatus = DataStatus.IS_CHANGED;

            try {
                if (result.getMessage().equals("Successfully created contact")) {
                    Contact newContact = result.getContact();
                    contacts.add(newContact);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            getInstance(activity);

        }

        private String convertStreamToString(InputStream is) throws IOException {
            if (is != null) {
                Writer writer = new StringWriter();

                char[] buffer = new char[1024];
                try {
                    Reader reader = new BufferedReader(
                            new InputStreamReader(is, "UTF-8"));
                    int n;
                    while ((n = reader.read(buffer)) != -1) {
                        writer.write(buffer, 0, n);
                    }
                } finally {
                    is.close();
                }
                return writer.toString();
            } else {
                return "";
            }
        }
    }

    public Exception persistData(String data) {
        try {
            new PersistDataTask().execute(data);
        } catch (Exception e) {
            return e;
        }

        return null;
    }


    // Persistance methods
    public void UpdateContact(String mId, Contact mContact)
    {
        try {
            // update instance data
            int pos = getContactPositionById(mId);
            contacts.set(pos, mContact);

            String data = "PUT|/" + mContact.get_id() + "?key=grumpy&" +
                    "name=" + mContact.getName() +
                    "&title=" + mContact.getTitle() +
                    "&email=" + mContact.getEmail() +
                    "&twitterId=" + mContact.getTwitterId() +
                    "&phone=" + mContact.getPhone();
            // Update the JSON data
            ContactManager.getInstance(activity).persistData(data);
        } catch (ContactNotFoundException e) {
            Log.w("UpdateContact", e + "\nUpdate NOT successful");
        }


    }

    public void AddContact(Contact mContact) {
            String data = "PST|" + "?key=grumpy&" +
                //"_id=" + mContact.get_id() +
                "name=" + mContact.getName() +
                "&title=" + mContact.getTitle() +
                "&email=" + mContact.getEmail() +
                "&twitterId=" + mContact.getTwitterId() +
                "&phone=" + mContact.getPhone();
        // Update the JSON data
        ContactManager.getInstance(activity).persistData(data);

        // create a unique id for the new contact
        //UUID local_uniqueId = UUID.randomUUID();
        //mContact.set_id(local_uniqueId.toString().replaceAll("[\\- ]", ""));

        //contacts.add(mContact);

    }

    public void DeleteContact(String mId){
        
        try {
            // delete from instance data
            int pos = getContactPositionById(mId);
            contacts.remove(pos);

            String data = "DEL|/" + mId + "?key=grumpy&";

            // delete from json and persist data
            ContactManager.getInstance(activity).persistData(data);
        } catch (ContactNotFoundException e) {
            Log.w("DeleteContact",e + "\nDelete NOT successful");
        }
    }

    public void GetSavedContact(String mId) {
        String data = "GET|/" + mId + "?key=grumpy&";

        // get a specific contact from persisted data
        ContactManager.getInstance(activity).persistData(data);
    }

    // List management methods
    public Contact getContactById(String id) throws ContactNotFoundException {
        Iterator<Contact> itr = contacts.iterator();

        while (itr.hasNext()) {
            Contact curr = itr.next();
            if (curr.equals(id)) {
                return curr;
            }
        }
        throw new ContactNotFoundException("Exception: No contact found with id [" + id + "]");
    }

    public int getContactPositionById(String id) throws ContactNotFoundException {
        Iterator<Contact> itr = contacts.iterator();

        int pos = 0;
        while (itr.hasNext()) {
            Contact curr = itr.next();
            if (curr.equals(id)) {
                return pos;
            }
            pos++;
        }
        throw new ContactNotFoundException("Exception: No contact found with id [" + id + "]");
    }

    private class ContactNotFoundException extends Exception {
        public ContactNotFoundException(String msg) {
            super(msg);
        }
    }
}
