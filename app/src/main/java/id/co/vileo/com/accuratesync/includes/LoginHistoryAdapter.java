package id.co.vileo.com.accuratesync.includes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.co.vileo.com.accuratesync.R;

/**
 * Created by BDO-IT on 15/06/2016.
 */
public class LoginHistoryAdapter extends ArrayAdapter<LoginHistory> {
    TextView timeLogin,timeLogout;

    public LoginHistoryAdapter(Context context) {
        super(context, R.layout.login_history_rows);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.login_history_rows, parent, false);
        }

        timeLogin = (TextView) convertView.findViewById(R.id.time_login);
        timeLogout = (TextView) convertView.findViewById(R.id.time_logout);

        LoginHistory log = getItem(position);
        timeLogin.setText("Login :" + log.getTimeLogin());
        timeLogout.setText("Logout :" + log.getTimeLogout());
        return convertView;
    }

    public void swapRecords(List<LoginHistory> objects) {
        clear();
        for(LoginHistory object : objects) {
            add(object);
        }
        notifyDataSetChanged();
    }
}
