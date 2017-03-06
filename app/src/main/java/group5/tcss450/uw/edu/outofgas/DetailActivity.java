package group5.tcss450.uw.edu.outofgas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {

    private TextView mNameTv;

    private TextView mVicinityTv;

    /*
     * SharePref for saving user's login
     */
    private SharedPreferences mPrefs;

    private MenuItem mSave;

    private MenuItem mShow;

    private MenuItem mDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mPrefs = getSharedPreferences(getString(R.string.SHARED_PREFS), Context.MODE_PRIVATE);

        mNameTv = (TextView) findViewById(R.id.nameTextView);
        String name = getIntent().getSerializableExtra("name").toString();
        mNameTv.setText(name);

        mVicinityTv = (TextView) findViewById(R.id.vicinityTextView);
        String vicinity = getIntent().getSerializableExtra("vicinity").toString();
        mVicinityTv.setText(vicinity);

        TextView priceTv = (TextView) findViewById(R.id.priceTextView);
        String price = getIntent().getSerializableExtra("price").toString();
        priceTv.setText(price);

        TextView ratingTv = (TextView) findViewById(R.id.ratingTextView);
        String rating = getIntent().getSerializableExtra("rating").toString();
        ratingTv.setText(rating);

        TextView distanceTv = (TextView) findViewById(R.id.distanceTextView);
        String distance = getIntent().getSerializableExtra("distance").toString();
        double distanceInNum = Double.parseDouble(distance);
        distanceTv.setText(new DecimalFormat("##.##").format(distanceInNum));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mSave = menu.findItem(R.id.save);
        mShow = menu.findItem(R.id.showEntries);
        mDelete = menu.findItem(R.id.delete);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.comment) {
            Intent intent = new Intent(getApplication(), CommentActivity.class);
            intent.putExtra("address", getIntent().getSerializableExtra("vicinity"));
            if (!VerifyFragment.myVerifyUsername.equals("")) {
                LoginActivity.user = VerifyFragment.myVerifyUsername;
                intent.putExtra("username", LoginActivity.user);
            } else if (!LoginActivity.user.equals("")){
                intent.putExtra("username", LoginActivity.user);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.signOut) {
            LoginActivity.user = "";
            VerifyFragment.myVerifyUsername = "";
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            mPrefs.edit().putString(getString(R.string.username),"0").apply();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.save) {
            try {
                OutputStreamWriter writer = new OutputStreamWriter(openFileOutput("SavedStations.txt", Context.MODE_APPEND));
                writer.append("Gas Station Name: ");
                writer.append(mNameTv.getText().toString());
                writer.append(System.lineSeparator());
                writer.append("Gas Station Address: ");
                writer.append(mVicinityTv.getText().toString());
                writer.append(System.lineSeparator());
                writer.append(System.lineSeparator());
                writer.close();
                Toast.makeText(getApplication(), "Save Successful!", Toast.LENGTH_SHORT).show();
                mSave.setEnabled(false);
            } catch (Exception e) {
                Toast.makeText(getApplication(), "Failed to Save!", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.delete) {
            File file = new File(getFilesDir(), "SavedStations.txt");
            try {
                FileWriter writer = new FileWriter(file);
                writer.write("");
                writer.close();
                EntriesFragment.textView.setText("");
                Toast.makeText(getApplication(), "Delete Successful!", Toast.LENGTH_SHORT).show();
                mDelete.setEnabled(false);
            } catch (Exception e) {
                Toast.makeText(getApplication(), "Failed to Delete!", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.showEntries) {
            EntriesFragment entriesFragment = new EntriesFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                    R.anim.enter_from_left, R.anim.exit_to_right);
            transaction.replace(R.id.activity_detail, entriesFragment);
            transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit();
            mShow.setEnabled(false);
            mSave.setEnabled(false);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mSave.setEnabled(true);
        mShow.setEnabled(true);
        mDelete.setEnabled(true);
    }
}
