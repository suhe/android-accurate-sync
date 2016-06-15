package id.co.vileo.com.accuratesync;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.co.vileo.com.accuratesync.config.AppConfig;
import id.co.vileo.com.accuratesync.includes.LoginHistory;
import id.co.vileo.com.accuratesync.includes.LoginHistoryAdapter;
import id.co.vileo.com.accuratesync.includes.SessionManager;

/**
 * Created by suhe on 6/13/2016.
 */
public class LoginHistoryFragment extends Fragment {
    private LoginHistoryAdapter logAdapter;
    ListView listView;
    private SessionManager session;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_history_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        session = new SessionManager(getContext());
        logAdapter = new LoginHistoryAdapter(getActivity());
        listView = (ListView) getView().findViewById(R.id.listViewLoginHistory);
        listView.setAdapter(logAdapter);
        fetch();

        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                fetch();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetch();
            }
        });
    }

    private void fetch() {
        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.LOG_HISTORY_URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            ArrayList<LoginHistory> records = new ArrayList<LoginHistory>();
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonApp = obj.getJSONArray("log");
                            for(int i =0; i < jsonApp.length(); i++) {
                                JSONObject jsonData = jsonApp.getJSONObject(i);
                                String time_login = jsonData.getString("time_login");
                                String time_logout = jsonData.getString("time_logout");
                                LoginHistory record = new LoginHistory(time_login,time_logout);
                                records.add(record);
                            }
                            logAdapter.swapRecords(records);
                            swipeRefreshLayout.setRefreshing(false);

                        } catch (JSONException e) {
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getActivity(), "Unable to parsing data view at : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "Unable to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("user_id",session.getSession("user_id"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }
}