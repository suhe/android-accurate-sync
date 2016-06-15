package id.co.vileo.com.accuratesync;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.co.vileo.com.accuratesync.config.AppConfig;
import id.co.vileo.com.accuratesync.includes.SessionManager;

public class LoginActivity extends AppCompatActivity  {
    private EditText user_login,user_pass;
    private Button btn_login;
    private SessionManager session;
    private boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        session = new SessionManager(getApplicationContext());
        user_login = (EditText) findViewById(R.id.user_login);
        user_pass = (EditText) findViewById(R.id.user_pass);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLogin();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isLoggedIn = session.getLogin();
        if(isLoggedIn) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void getLogin() {
        final String str_user_login = user_login.getText().toString();
        final String str_user_pass = user_pass.getText().toString();
        if(!validate(str_user_login,str_user_pass)) {
            return;
        }

        //set button to enabled false
        btn_login.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Processing to login ...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            boolean success = obj.getBoolean("success");
                            if(!success) {
                                onLoginFailed();
                                progressDialog.dismiss();
                            } else {
                                JSONObject user = obj.getJSONObject("user");
                                session.setLogin(true);
                                session.setSession("user_id",user.getString("user_id"));
                                session.setSession("user_name",user.getString("user_name"));
                                session.setSession("role_id",user.getString("role_id"));
                                new Handler().postDelayed (
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                onLoginSuccess();
                                                progressDialog.dismiss();
                                            }
                                        } ,5000
                                );

                            }

                        } catch (JSONException e) {
                            onLoginFailed();
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onLoginFailed();
                        progressDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("user_login", str_user_login.toString());
                params.put("user_pass",str_user_pass.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public boolean validate(String str_user_login,String str_user_pass) {
        boolean valid = true;
        if(str_user_login.isEmpty()) {
            user_login.setError("Please fill user login filed !");
            valid = false;
        } else {
            user_login.setError(null);
        }

        if(str_user_pass.isEmpty()) {
            user_pass.setError("Please fill password field !");
            valid = false;
        } else {
            user_pass.setError(null);
        }

        return valid;
    }

    public void onLoginFailed() {
        Toast.makeText(LoginActivity.this, "Wrong username or password please try again !", Toast.LENGTH_LONG).show();
        btn_login.setEnabled(true);
    }

    public void onLoginSuccess() {
        btn_login.setEnabled(true);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}