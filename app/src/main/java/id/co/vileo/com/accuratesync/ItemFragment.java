package id.co.vileo.com.accuratesync;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
import id.co.vileo.com.accuratesync.includes.Item;
import id.co.vileo.com.accuratesync.includes.ItemAdapter;
import id.co.vileo.com.accuratesync.includes.SessionManager;

import static id.co.vileo.com.accuratesync.R.*;

/**
 * Created by suhe on 6/13/2016.
 */
public class ItemFragment extends Fragment {
    private ItemAdapter itemAdapter;
    ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SessionManager session;
    private View footer;
    private int pageCount = 0;
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(layout.items_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        session = new SessionManager(getContext());
        itemAdapter = new ItemAdapter(getActivity());
        listView = (ListView) getView().findViewById(R.id.listView);
        listView.setAdapter(itemAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int threshold = 1;
                int count = listView.getCount();
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (listView.getLastVisiblePosition() >= count - threshold && pageCount < 2) {
                        Log.i("Load More", "loading more data");
                        fetch();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }



        });

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

        fetch();
    }

    private void fetch() {
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Initialize");
        dialog.setMessage("Loading more...");
        dialog.setIndeterminate(false);
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.ITEM_URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            ArrayList<Item> records = new ArrayList<Item>();
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonApp = obj.getJSONArray("app");
                            for(int i =0; i < jsonApp.length(); i++) {
                                JSONObject jsonData = jsonApp.getJSONObject(i);
                                String itemNo = jsonData.getString("ITEMNO");
                                String itemName = jsonData.getString("ITEMDESCRIPTION");
                                String categoryName = jsonData.getString("CATEGORYNAME");
                                String quantity = jsonData.getString("QUANTITY");
                                String messageWarning = jsonData.getString("MESSAGEWARNING");

                                Item record = new Item(itemNo, itemName,categoryName,quantity,messageWarning);
                                records.add(record);
                            }

                            itemAdapter.swapRecords(records);
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
                params.put("role_id",session.getSession("role_id"));
                return params;
            }
        };

        dialog.dismiss();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

}
