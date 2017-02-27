package group5.tcss450.uw.edu.outofgas;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView t;
    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/" +
            "~phuctran/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        t = (TextView) findViewById(R.id.commentTV);
        t.setMovementMethod(new ScrollingMovementMethod());

        Button b = (Button) findViewById(R.id.commentButton);
        b.setOnClickListener(this);


        AsyncTask<String, Void, String> task;
        String address = getIntent().getSerializableExtra("address").toString();
        address.replace(" ", "+");

        task = new loadCommentTask();
        task.execute(PARTIAL_URL, address);


       // getIntent().getSerializableExtra("username").toString());
    }

    @Override
    public void onClick(View v) {
        EditText commentBox = (EditText) findViewById(R.id.commentBox);

        if (!commentBox.getText().toString().equalsIgnoreCase("")){

            saveCommentTask task = new saveCommentTask();
            String address = getIntent().getSerializableExtra("address").toString();
            address.replace(' ', '+');
            String comment = commentBox.getText().toString();
            comment = comment.replace(' ', '+');
            commentBox.setText(" ");
            task.execute(PARTIAL_URL, address, comment);
        } else {
            commentBox.setError("please type in your comment");
        }

    }

    /*
     * Web service task that calls the loginapp php script.
     * This checks if the user exists in the system and if they entered
     * the correct password.
     */

    private class loadCommentTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "loadComment.php";
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 2) {
                throw new IllegalArgumentException("Two String arguments required.");
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            String arg1 = "?inputAddress=" + strings[1];

            try {
                URL urlObject = new URL(url + SERVICE + arg1);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s;
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplication(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            } else if (!result.isEmpty() && !result.startsWith("Unable to")) {
                result = result.replace('~', '\n');
                t.setText(getIntent().getSerializableExtra("address").toString() + "\n "
                        + "-------------------------------------------" +
                          "-------------------------------------------" +
                        "\n" + result);
            }
        }
    }

    private class saveCommentTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "saveComment.php";
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 3) {
                throw new IllegalArgumentException("Six String arguments required.");
            }
            
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            String arg0 = "?inputAddress=" + strings[1];
            String arg1 = "&comment="+
                    getIntent().getSerializableExtra("username").toString() +
                     "+("+ SystemClock.currentThreadTimeMillis() + "):+" + strings[2];
            try {
                URL urlObject = new URL(url + SERVICE + arg0 + arg1);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.

            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplication(), "unable", Toast.LENGTH_LONG)
                        .show();
                return;
            } else if (!result.isEmpty() && !result.equals("overwrite")) {
                Toast.makeText(getApplication(), result , Toast.LENGTH_LONG).show();
                loadCommentTask task = new loadCommentTask();
                task.execute(PARTIAL_URL, getIntent().getSerializableExtra("address").toString());
            } else {
                t.setText(result);
                Toast.makeText(getApplication(), result, Toast.LENGTH_LONG).show();
            }
        }
    }
}