package group5.tcss450.uw.edu.outofgas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {

    private TextView mNameTv;

    private TextView mVicinityTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.save) {
            File file = new File(getFilesDir(), "SavedStations.txt");
            try {
                FileWriter writer = new FileWriter(file);
                writer.append("Gas Station Name: ");
                writer.append(mNameTv.getText().toString());
                writer.append(", Gas Station Address: ");
                writer.append(mVicinityTv.getText().toString());
                writer.append('\n');
                writer.close();
                Toast.makeText(getApplication(), "Save Successful!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplication(), "Failed to Save!", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

}
