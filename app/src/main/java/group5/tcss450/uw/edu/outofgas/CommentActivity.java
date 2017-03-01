package group5.tcss450.uw.edu.outofgas;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        TextView b = (TextView) findViewById(R.id.commentSendText);
        b.setOnClickListener(this);

        setTitle(getIntent().getSerializableExtra("address").toString());

        AsyncTask<String, Void, String> task;
        String address = getIntent().getSerializableExtra("address").toString();
        URLString(address);

        task = new loadCommentTask();
        task.execute(PARTIAL_URL, address);
    }

    @Override
    public void onClick(View v) {
        EditText commentBox = (EditText) findViewById(R.id.commentBox);

        if (!commentBox.getText().toString().equalsIgnoreCase("")){

            saveCommentTask task = new saveCommentTask();
            String address = getIntent().getSerializableExtra("address").toString();
            String comment = commentBox.getText().toString();
            if (commentBox.getText().toString().length() < 0) {
                commentBox.setError("Your comment must contain at least 4 characters");
            } else {
                URLString(address);
                String commentToSubmit = URLStringChange(comment);
                commentBox.setText("");
                task.execute(PARTIAL_URL, address, commentToSubmit);
            }
        } else {
            commentBox.setError("You cannot submit an empty comment");
        }
    }

    /*
     * Web service task that calls the loadComment php script.
     * Load all previous comments.
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
                t.setText(result);
            }
        }
    }

    /*́́
    * Web service task that calls the saveComment php script.
    * Save the comment that the user sent.
    */
    private class saveCommentTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "saveComment.php";
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 3) {
                throw new IllegalArgumentException("Six String arguments required.");
            }
            String date = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            String arg0 = "?inputAddress=" + strings[1];
            String arg1 = "&comment="+
                    getIntent().getSerializableExtra("username").toString() +
                     "+("+ date + "):+" + strings[2];
            try {
                URL urlObject = new URL(url + SERVICE + arg0 + arg1);
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

    private String URLString (String str){
        str = str.replace(' ', '+')
        .replace("\'", "\\'");
        return str;
    }

    private String URLStringChange (String str){
        String returnStr;
        returnStr = str.replaceAll("'", "\"");
        return returnStr;
    }
}
