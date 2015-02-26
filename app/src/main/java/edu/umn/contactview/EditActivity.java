package edu.umn.contactview;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class EditActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ContactManager contactMgr = ContactManager.getInstance(this);

        if (!(getIntent().getExtras().getString("_id") == null)) {
            String contactId = getIntent().getExtras().getString("_id");

            // Check if there is a problem or creating a new contact
            if (contactId.equals("-1")) {
                //Do nothing and the defaults will be displayed
            } else {
                Contact mContact = contactMgr.GetContact(contactId);

                ((TextView) findViewById(R.id.editName)).setText(mContact.getName());
                ((TextView) findViewById(R.id.editEmail)).setText(mContact.getEmail());
                ((TextView) findViewById(R.id.editPhone)).setText(mContact.getPhone());
                ((TextView) findViewById(R.id.editTitle)).setText(mContact.getTitle());
                ((TextView) findViewById(R.id.editTwitter)).setText(mContact.getTwitterId());
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
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


    // We want to create an object of type contact and update it with the
    // data from the text fields and pass it to the ContactManager
    private void SaveChanges(String contactId)
    {
        ContactManager contactMgr = ContactManager.getInstance(this);
        Contact mContact = new Contact();

        contactMgr.UpdateContact(contactId, mContact);
    }

    // Abort!!! don't save changes, don't pass anything to ContactManager
    private void DiscardChanges()
    {

    }
}
