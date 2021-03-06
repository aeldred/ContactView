package edu.umn.contactview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class DetailsActivity extends Activity {

    String contactId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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

        if(id==R.id.action_delete)
        {
            DeleteContact();
            ShowToast("Contact Deleted");
            finish();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            // Create a new intent that points to the Details activity
            // then start the new activity
            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra("_id", contactId);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // We want to create an object of type contact and update it with the
    // data from the text fields and pass it to the ContactManager
    private void DeleteContact()
    {
        if(contactId != null) {
            ContactManager contactMgr = ContactManager.getInstance(this);

            contactMgr.DeleteContact(contactId);
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
        //ShowToast("Details onStart");
        super.onStart();
        ContactManager contactMgr = ContactManager.getInstance(this);

        try {
            contactId = getIntent().getExtras().getString("_id");
            Contact mContact = contactMgr.GetContact(contactId);

            ((TextView) findViewById(R.id.detailsName)).setText(mContact.getName());
            ((TextView) findViewById(R.id.detailsEmail)).setText(mContact.getEmail());
            ((TextView) findViewById(R.id.detailsPhone)).setText(mContact.getPhone());
            ((TextView) findViewById(R.id.detailsTitle)).setText(mContact.getTitle());
            ((TextView) findViewById(R.id.detailsTwitter)).setText(mContact.getTwitterId());
        } catch (Exception e) {
            //if no id is returned, then this is an add
        }
    }

    @Override
    protected void onResume() {
        //ShowToast("Details onResume");
        super.onResume();
    }

    // The next two are called when we switch away from this activity
    @Override
    protected void onPause() {
        //ShowToast("Details onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        //ShowToast("Details onStop");
        super.onStop();
    }
}
