package com.wedevol.fcmtest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Token service to handle the registration into the DB
 */

public class TokenService extends AsyncTask<String, Void, Void> {

    private static final String TAG = "TokenService";
    public static final String BACKEND_SERVER_IP = "10.0.2.2";
    public static final String BACKEND_URL_BASE = "http://" + BACKEND_SERVER_IP;

    private Context context;
    private IRequestListener listener;

    public TokenService(Context context, IRequestListener listener){
        this.context = context;
        this.listener = listener;
    }

    protected Void doInBackground(String... tokens) {
        registerTokenInDB(tokens[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.onComplete();
    }

    public void registerTokenInDB(final String token) {
        // The call should have a back off strategy
        Log.d(TAG, "Register token service");

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = BACKEND_URL_BASE + "/PHP/fcmtest/register.php";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Token registered successfully in the DB");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error trying to register the token in the DB");
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("token",token);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}
