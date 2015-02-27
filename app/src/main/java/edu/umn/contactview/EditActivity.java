package edu.umn.contactview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class EditActivity extends Activity {

    String contactId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ContactManager contactMgr = ContactManager.getInstance(this);

        try {
            contactId = getIntent().getExtras().getString("_id");
            Contact mContact = contactMgr.GetContact(contactId);
            ((TextView) findViewById(R.id.editName)).setText(mContact.getName());
            ((TextView) findViewById(R.id.editEmail)).setText(mContact.getEmail());
            ((TextView) findViewById(R.id.editPhone)).setText(mContact.getPhone());
            ((TextView) findViewById(R.id.editTitle)).setText(mContact.getTitle());
            ((TextView) findViewById(R.id.editTwitter)).setText(mContact.getTwitterId());
        } catch (Exception e) {
            contactId = "-1";
            //this is an add if the contact is null
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

        switch(id) {
            case R.id.action_settings:
                break;
            case R.id.action_save:
                SaveChanges();
                ShowToast("Changes Saved");
                finish();
                break;
            case R.id.action_delete:
                DeleteContact();
                ShowToast("Contact Deleted");
                NavUtils.navigateUpFromSameTask(this);
                break;
            default:
                break;
        }


        finish();

        return super.onOptionsItemSelected(item);
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
        //ShowToast("Edit onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //ShowToast("Edit onResume");
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
    private void SaveChanges()
    {
        ContactManager contactMgr = ContactManager.getInstance(this);
        Contact mContact = new Contact();
        mContact.setName(((TextView) findViewById(R.id.editName)).getText().toString());
        mContact.setTitle(((TextView) findViewById(R.id.editTitle)).getText().toString());
        mContact.setEmail(((TextView) findViewById(R.id.editEmail)).getText().toString());
        mContact.setPhone(((TextView) findViewById(R.id.editPhone)).getText().toString());
        mContact.setTwitterId(((TextView) findViewById(R.id.editTwitter)).getText().toString());

        if(!contactId.equals("-1")) {
            mContact.set_id(contactId);
            contactMgr.UpdateContact(contactId, mContact);
        } else {
            contactMgr.AddContact(mContact);

        }
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
}
