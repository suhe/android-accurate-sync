package id.co.vileo.com.accuratesync.includes;

/**
 * Created by BDO-IT on 15/06/2016.
 */
public class LoginHistory {
    private String timeLogin;
    private String timeLogout;

    public LoginHistory(String timeLogin, String timeLogout) {
        this.timeLogin = timeLogin;
        this.timeLogout= timeLogout;
    }

    public String getTimeLogin() {
        return timeLogin;
    }

    public String getTimeLogout() {
        return timeLogout;
    }
}
