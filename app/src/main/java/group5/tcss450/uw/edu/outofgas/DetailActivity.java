package group5.tcss450.uw.edu.outofgas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
}
