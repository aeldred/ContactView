package edu.umn.contactview;

import android.app.ListActivity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //attach our adapter to the activity
        //if asking for "Context", asking for "Activity" which is a sub-class of context
        setListAdapter(new ContactAdapter(this, R.layout.contact_item, Contact.getAll()));
    }

    @Override
    //listView - never need to look at this
    //View - generally don't need this
    // most interested in position or id
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Contact contact = (Contact)getListAdapter().getItem(position);
        //makeText only makes the text, need to add show() to actually display the text
        Toast.makeText(this, "Clicked " + contact.getName() + " (" + id + ") ", Toast.LENGTH_LONG).show();
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //easier to make adapter an inner class - common to put this in activity class
    class ContactAdapter extends ArrayAdapter<Contact> {

        public ContactAdapter(Context context, int resource, Contact[] objects) {
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

}
