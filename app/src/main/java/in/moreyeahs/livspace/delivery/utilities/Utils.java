package in.moreyeahs.livspace.delivery.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import in.moreyeahs.livspace.delivery.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static Context context;
    public static Dialog customDialog;
    private static final String MOBILE_NO_PATTERN = "^[+]?[0-9]{10,13}$";
    public static String myFormat = "yyyy-MM-dd'T'HH:mm:ss";
    public static LatLng temp = null;
    public static GPSTracker gpsTracker;
    public static String lat, lag;

    public Utils(Context _mContext) {
        context = _mContext;
    }

    public static void setToast(Context _mContext, String str) {
        Toast toast = Toast.makeText(_mContext, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(MOBILE_NO_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo networkInfo : info) {
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String getCustMobile() {
        return SharePrefs.getInstance(context).getString(SharePrefs.MOBILE);
    }

    public static String getToken() {
        return SharePrefs.getInstance(context).getString(SharePrefs.TOKEN);
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showProgressDialog(Activity activity) {
        try {
            if (customDialog != null)
                customDialog.dismiss();
            if (customDialog == null) {
                customDialog = new Dialog(activity, R.style.CustomDialog);
            }
            LayoutInflater inflater = LayoutInflater.from(activity);
            if (inflater != null) {
                View mView = inflater.inflate(R.layout.progress_dialog, null);
                customDialog.setContentView(mView);
                if (!customDialog.isShowing() && customDialog != null && !activity.isFinishing()) {
                    customDialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideProgressDialog(Activity activity) {
        if (customDialog != null) {
            //if (customDialog != null) {
            customDialog.dismiss();
        }
    }

    public static void hideProgressDialog() {
        if (customDialog != null) {
            customDialog.dismiss();
        }
    }

    public static boolean isJSONValid(String json) {
        try {
            new JSONObject(json);
        } catch (Exception ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(json);
            } catch (Exception ex1) {
                return false;
            }
        }
        return true;
    }

    public static String getDate(String dateTime) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd/MM/yyyy";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.ENGLISH);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dateTime);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getSimpleDateFormat(String ServerDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (!ServerDate.equalsIgnoreCase("") && !ServerDate.equalsIgnoreCase(null)) {
            DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);//These format come to server
            originalFormat.setTimeZone(TimeZone.getDefault());

            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);  //change to new format here  //dd-MM-yyyy HH:mm:ss

            Date date = null;
            String formattedDate = "";
            try {
                date = originalFormat.parse(ServerDate);
                formattedDate = targetFormat.format(date);  //result
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return formattedDate;
        } else {
            return "null";
        }
    }

    public static String getDateFormat(String ServerDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (!ServerDate.equalsIgnoreCase(null) && !ServerDate.equalsIgnoreCase("")) {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);//These format come to server
            originalFormat.setTimeZone(TimeZone.getDefault());

            DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);  //change to new format here  //dd-MM-yyyy HH:mm:ss

            Date date = null;
            String formattedDate = "";
            try {
                date = originalFormat.parse(ServerDate);

                formattedDate = targetFormat.format(date);  //result
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return formattedDate;
        } else {
            return "null";
        }
    }

    public static String getDayMonthFormat(String ServerDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (ServerDate != null && !ServerDate.equalsIgnoreCase("")) {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);//These format come to server
            originalFormat.setTimeZone(TimeZone.getDefault());

            DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);  //change to new format here  //dd-MM-yyyy HH:mm:ss

            Date date = null;
            String formattedDate = "";
            try {
                date = originalFormat.parse(ServerDate);

                formattedDate = targetFormat.format(date);  //result
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return formattedDate;
        } else {
            return "";
        }
    }

    public static LatLng getTemp(Context context) {
        try {
            gpsTracker = new GPSTracker(context);
            if (gpsTracker != null)
                temp = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static String getLat(Context context) {
        try {
            gpsTracker = new GPSTracker(context);
            if (gpsTracker != null)
                lat = String.valueOf(gpsTracker.getLatitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lat;
    }

    public static String getLog(Context context) {
        try {
            gpsTracker = new GPSTracker(context);
            if (gpsTracker != null)
                lag = String.valueOf(gpsTracker.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lag;
    }

    public static String convertToDaysHoursMinutes(long minutes) {
        int day = (int) TimeUnit.MINUTES.toDays(minutes);
        long hours = TimeUnit.MINUTES.toHours(minutes) - (day * 24);
        long minute = TimeUnit.MINUTES.toMinutes(minutes) - (TimeUnit.MINUTES.toHours(minutes) * 60);
        String result = "";
        if (day != 0) {
            result += day;
            if (day == 1) {
                result += " day ";
            } else {
                result += " days ";
            }
            return result;
        }

        if (hours != 0) {
            result += hours;

            if (hours == 1) {
                result += " hr ";
            } else {
                result += " hrs ";
            }
        }

        if (minute != 0) {
            result += minute;

            if (minute == 1) {
                result += " min";
            } else {
                result += " mins";
            }
        }
        return result;
    }

    public static void leftTransaction(Activity activity) {
        activity.overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
    }

    public static void rightTransaction(Activity activity) {
        activity.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}