package group5.tcss450.uw.edu.outofgas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;

public class EntriesFragment extends Fragment {
    /**
     * The text view for all saved gas station.
     */
    public static TextView textView;

    public EntriesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_entries, container, false);
        textView = (TextView) view.findViewById(R.id.entries);
        textView.setMovementMethod(new ScrollingMovementMethod());
        try {
            File file = new File(getActivity().getFilesDir(), "SavedStations.txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            int ch = 0;
            char c;
            String string = "";
            while ( (ch = fileInputStream.read()) != -1) {
                c = (char) ch;
                string = string + c;
            }
            textView.setText(string);
            fileInputStream.close();
        } catch (Exception e) {
            Toast.makeText(this.getContext(), "Failed To Open File!", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

}
