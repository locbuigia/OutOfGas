package group5.tcss450.uw.edu.outofgas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Map;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    private final int[] radiusArray = {4, 6, 8, 10, 12, 14, 16};
    int radiusMultiplier = 1000;

    TextView radius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
        for (int i = 0; i < group.getChildCount(); i++) {
            group.getChildAt(i).setOnClickListener(this);
        }

        group.check(MapsActivity.checkedRadioBtnId);

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBarRadius);
        radius = (TextView) findViewById(R.id.radiusTextView);
        seekBar.setProgress(MapsActivity.radiusProgress);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                radius.setText("Current Radius in Kilometers: " + radiusArray[i]);
                MapsActivity.radiusProgress = i;
                MapsActivity.mRadius = radiusArray[i] * radiusMultiplier;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.normalBtn:
                MapsActivity.mMap.setMapType(MapsActivity.mMap.MAP_TYPE_NORMAL);
                MapsActivity.checkedRadioBtnId = R.id.normalBtn;
                break;
            case R.id.satelliteBtn:
                MapsActivity.mMap.setMapType(MapsActivity.mMap.MAP_TYPE_SATELLITE);
                MapsActivity.checkedRadioBtnId = R.id.satelliteBtn;
                break;
            case R.id.hybridBtn:
                MapsActivity.mMap.setMapType(MapsActivity.mMap.MAP_TYPE_HYBRID);
                MapsActivity.checkedRadioBtnId = R.id.hybridBtn;
                break;
        }
    }
}
