package in.moreyeahs.livspace.delivery.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

    public static  String DATE_FORMAT_TIME = "yyyy-MM-dd'T'HH:mm:ss";
    public static  String DATE_FORMAT_DATE = "E, MMM dd, yyyy";


    public static String getDateFormat(String ServerDate) {
        // 2018-12-24T15:48:15.707+05:30
        if (!ServerDate.equalsIgnoreCase("") && !ServerDate.equalsIgnoreCase(null)) {

            DateFormat originalFormat = new SimpleDateFormat(DATE_FORMAT_TIME);//These format come to server
            originalFormat.setTimeZone(TimeZone.getDefault());
            //"dd/MM/yyyy" 02/02/2019
            // E, dd MMM yyyy  HH:mm:ss z  Tue, 02 Jan 2018 18:07:59 IST
            DateFormat targetFormat = new SimpleDateFormat(DATE_FORMAT_DATE);  //change to new format here  //dd-MM-yyyy HH:mm:ss
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

    public static long getEpochTime(String ServerDate) {
        if (!ServerDate.equalsIgnoreCase("") && !ServerDate.equalsIgnoreCase(null)) {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date epochDate = null;
            try {
                epochDate = formatter.parse(ServerDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return epochDate.getTime();
        } else {
            return 0;
        }
    }


}
