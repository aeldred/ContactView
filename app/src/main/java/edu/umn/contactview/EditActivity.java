package edu.umn.contactview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class EditActivity extends Activity {

    Contact mContact=null;
    String contactId;
    ContactManager contactMgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        contactMgr = ContactManager.getInstance(this);

        if (!(getIntent().getExtras().getString("_id") == null)) {
            contactId = getIntent().getExtras().getString("_id");

            // Check if there is a problem or creating a new contact

            mContact = contactMgr.GetContact(contactId);
            ((TextView) findViewById(R.id.editName)).setText(mContact.getName());
            ((TextView) findViewById(R.id.editEmail)).setText(mContact.getEmail());
            ((TextView) findViewById(R.id.editPhone)).setText(mContact.getPhone());
            ((TextView) findViewById(R.id.editTitle)).setText(mContact.getTitle());
            ((TextView) findViewById(R.id.editTwitter)).setText(mContact.getTwitterId());
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


        if (id == R.id.action_save) {
            String name= ((TextView)findViewById(R.id.editName)).getText().toString();
            String email = ((TextView)findViewById(R.id.editEmail)).getText().toString();
            String phone=((TextView) findViewById(R.id.editPhone)).getText().toString();
            String title= ((TextView) findViewById(R.id.editTitle)).getText().toString();
            String twitter=((TextView) findViewById(R.id.editTwitter)).getText().toString();
            mContact.setName(name);
            mContact.setEmail(email);
            mContact.setPhone(phone);
            mContact.setTitle(title);
            mContact.setTwitterId(twitter);
            contactMgr.localUpdateContact(contactId, mContact);
            finish();
            startActivity(getIntent());
            return true;
        }

        if (id == R.id.action_delete) {
            contactMgr.localDeleteContact(contactId);
            finish();
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
