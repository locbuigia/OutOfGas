package group5.tcss450.uw.edu.outofgas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView nameTv = (TextView) findViewById(R.id.nameTextView);
        String name = getIntent().getSerializableExtra("name").toString();
        nameTv.setText(name);

        TextView vicinityTv = (TextView) findViewById(R.id.vicinityTextView);
        String vicinity = getIntent().getSerializableExtra("vicinity").toString();
        vicinityTv.setText(vicinity);

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
        }
        return true;
    }

}
