package id.co.vileo.com.accuratesync.includes;

/**
 * Created by suhe on 6/13/2016.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.HashMap;

public class SessionManager {
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "HelpdeskBDO";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NIK = "nik";
    public static final String KEY_NAME = "name";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(Boolean isLogin) {
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.commit();
    }

    public boolean getLogin() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public boolean isLogin(){
        if(!this.getLogin()){
            return  false;
        }
        return true;
    }

    public void setSession(String label,Object value){
        editor.putString(label,value.toString());
        editor.commit();
    }

    public String getSession(String label) {
        return pref.getString(label,null);
    }
}
