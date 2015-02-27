package edu.umn.contactview;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.InputStreamReader;
import java.util.List;


public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContactManager.getInstance(this);


        //attach our adapter to the activity
        //if asking for "Context", asking for "Activity" which is a sub-class of context
        //setListAdapter(new ContactAdapter(this, R.layout.contact_item, contactManager.getContacts()));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Contact contact = (Contact)getListAdapter().getItem(position);

        //Toast.makeText(this, "Clicked " + contact.getName(), Toast.LENGTH_LONG).show();

        // Create a new intent that points to the Details activity
        // then start the new activity
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("_id", contact.get_id());
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case R.id.action_add:
                Intent editIntent = new Intent(this,EditActivity.class);
                editIntent.putExtra("_id", "-1");
                startActivity(editIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public void updateAdapter(List<Contact> objects) {
        setListAdapter(new ContactAdapter(MainActivity.this, R.layout.contact_item, objects));
    }

    //easier to make adapter an inner class - common to put this in activity class
    class ContactAdapter extends ArrayAdapter<Contact> {

        public ContactAdapter(Context context, int resource, List<Contact> objects) {
            super(context, resource, objects);
        }

        @Override
        //parent = listView attaching to (needed for inflation of layout)
        //convertView = listView could possibly pass in existing view that listView
        // wants to be used. May pass null if no existing view is created
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            //null means android hasn't found anything to be reused, need to create new
            if (convertView == null) {
                //get layoutinflator - uses id to pull layout out of xml and inflate into object
                //attach the object to the parent
                view = getLayoutInflater().inflate(R.layout.contact_item, parent, false);
            } else {
                view = convertView;
            }

            //populate details from contact
            Contact contact = getItem(position);

            //cast to textview because type returned by findViewById is generic view
            TextView nameView = (TextView)view.findViewById(R.id.item_name);
            TextView titleView = (TextView)view.findViewById(R.id.item_title);
            TextView phoneView = (TextView)view.findViewById(R.id.item_phone);

            nameView.setText(contact.getName());
            titleView.setText(contact.getTitle());
            phoneView.setText(contact.getPhone());

            return view;
        }
    }

    void ShowToast(CharSequence text)
    {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    // The next two are called when we switch back into this activity
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    // The next two are called when we switch away from this activity
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
