/**
 * Loc Bui, Andrew Dinh, Phuc Tran
 * Feb 9, 2017
 * @version: 0.1
 */
package group5.tcss450.uw.edu.outofgas;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {
    private EditText username, password;

    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/" +
            "~locbui/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.username);
        username.requestFocus();
        password = (EditText) findViewById(R.id.password);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AsyncTask<String, Void, String> task = null;
                String theUsername = username.getText().toString();
                String thePassword = password.getText().toString();

                task = new GetWebServiceTask();
                if (username.getText().toString().trim().equalsIgnoreCase("")) {
                    username.setError("Please enter username");
                } else if (password.getText().toString().trim().equalsIgnoreCase("")) {
                    password.setError("Please enter password");
                } else {
                    task.execute(PARTIAL_URL, theUsername, thePassword);
                }
            }
        });
    }

    private class GetWebServiceTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "loginApp.php";
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 3) {
                throw new IllegalArgumentException("Two String arguments required.");
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            String arg1 = "?user_name=" + strings[1];
            String arg2 = "&pass_word=" + strings[2];
            try {
                URL urlObject = new URL(url + SERVICE + arg1 + arg2);
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
                Toast.makeText(getApplication(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            } else if (!result.isEmpty() && !result.startsWith("Unable to")) {
                Toast.makeText(getApplication(), "Login Success!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplication(), MapsActivity.class);
                startActivity(intent);
            } else if (result.isEmpty() && !result.startsWith("Unable to")) {
                Toast toast = Toast.makeText(getApplication(), "Login Failed! Invalid Username or Password", Toast.LENGTH_LONG);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();
            }
        }
    }

    public void registerMethod(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void forgotPasswordMethod(View v) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}
