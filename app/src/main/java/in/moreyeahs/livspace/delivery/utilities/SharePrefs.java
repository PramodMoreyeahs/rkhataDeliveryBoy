package in.moreyeahs.livspace.delivery.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by User on 13-11-2018.
 */

public class SharePrefs {
    public static final String TOKEN_NAME = "token_name";
    public static final String TOKEN_PASSWORD = "token_pass";
    public static String LANGUAGE = "LANGUAGE";
    public static String SHARED_PREF_NAME = "fcmsharedprefname";
    public static String KEY_ACCESS_TOKEN = "token";
    public static String PEOPLE_ID = "people_id";
    public static String DELIVERY_ISSUANCE_ID = "deliveryIssuanceId";
    public static String LOGGED = "logged";
    public static String EMAILID = "emailid";
    public static String SK_CODE = "sk_code";
    public static String ASSIGNMENT_ID = "assignment_id";
    public static String WAREHOUSE_ID = "warehouse_id";
    public static String COMPANY_ID = "company_id";
    public static String PEAOPLE_FIRST_NAME = "people_first_name";
    public static String PEAOPLE_LAST_NAME = "people_last_name";
    public static String PEAOPLE_EMAIL = "people_email";
    public static String PEAOPLE_IMAGE = "people_image";
    public static String MOBILE = "mobile";
    public static String TOKEN = "token";
    public static String VEHICLE_NUMBER = "vehicle_number";
    public static String VEHICLE_NAME = "vehicle_name";
    public static String PREFERENCE = "skDelivery";
    public static String CASHMANAGMENT_PREFERENCE = "cash_managmentpref";
    public static String MY_TASK_DATA = "My_task_data";
    public static String OTP_NUMBER = "Otp_Numbers";
    public static String CASH_AMOUNT_JSON = "Cash_amount_json";
    public static String EDIT_AMOUNT_JSON = "Edit_amount_json";
    public static String CASH_AMOUNT = "Cash_amount";
    public static String CURRENCY_MODEL = "CURRENCY_MODEL ";
    public static String CHEQUE_AMOUNT = "Cheque_amount";
    public static String ONLINE_AMOUNT = "Online_amount";
    public static String ASSIGN_ID = "Assign_ID";
    public static String ASSIGN_MODEL = "Assign_model";
    public static String HISTORY_MODEL = "History_model";
    public static String UPDATE_DATA = "Update_data";
    public static String FLAG = "flag";
    // HDFC credentials
    public static final String GATWAY_URL = "gatway_url";
    public static final String HAS_RETURN_ORDER = "cancel_url";


    private Context ctx;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferencesComplex;
    private static SharePrefs instance;


    public SharePrefs(Context context) {
        ctx = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferencesComplex = PreferenceManager.getDefaultSharedPreferences(context);
    }

   /* public static SharePrefs getInstanceComplex(Context ctx) {
        if (instanceComplex == null) {
            instanceComplex = new SharePrefs(ctx);
        }
        return instanceComplex;
    }*/

    public static SharePrefs getInstance(Context ctx) {
        if (instance == null) {
            instance = new SharePrefs(ctx);
        }
        return instance;
    }

    public void putString(String key, String val) {
        sharedPreferences.edit().putString(key, val).apply();
    }

    public void putComplexData(String key, String val) {
        sharedPreferencesComplex.edit().putString(key, val).apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void putInt(String key, Integer val) {
        sharedPreferences.edit().putInt(key, val).apply();
    }

    public void putLong(String key, Long val) {
        sharedPreferences.edit().putLong(key, val).apply();
    }

    public long getLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }


    public void putBoolean(String key, Boolean val) {
        sharedPreferences.edit().putBoolean(key, val).apply();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }


    public void clearSharePrefs() {
        sharedPreferences.edit().clear().commit();
    }


    public static String getStringSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        return settings.getString(name, "");
    }

    // for username string preferences
    public static void setStringSharedPreference(Context context, String name, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, value);
        editor.apply();
    }

    public static void setCashmanagmentSharedPreference(Context context, String name, String value) {
        SharedPreferences settings = context.getSharedPreferences(CASHMANAGMENT_PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, value);
        editor.apply();
    }

    public static String getCashmanagmentSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(CASHMANAGMENT_PREFERENCE, 0);
        return settings.getString(name, "");
    }

    public boolean storeToken(String Token) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, Token);
        editor.apply();
        return true;
    }

    public String getToken() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }
}
