package in.moreyeahs.livspace.delivery.api;

import com.google.gson.Gson;
import in.moreyeahs.livspace.delivery.BuildConfig;
import in.moreyeahs.livspace.delivery.utilities.Aes256;
import in.moreyeahs.livspace.delivery.utilities.Utils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User on 03-11-2018.
 */
public class RestClient {
    private static Retrofit retrofit;
    private static final RestClient ourInstance = new RestClient();

    public static RestClient getInstance() {
        return ourInstance;
    }

    private RestClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    if (response.code() == 200) {
                        if (!request.url().toString().contains("/token") && !request.url().toString().contains("/appVersion") &&
                                !request.url().toString().contains("/UpdateAssignment") &&
                                !request.url().toString().contains("/MobileDelivery/CurrencyUploadChequeImageForMobile") &&
                                !request.url().toString().contains("/DBoyAssignmentDeposit/UploadDboySignature") &&
                                !request.url().toString().contains("/api/AssignmentCopyUpload") &&
                                !request.url().toString().contains("/api/itemimageupload/UploadKKReturnReplaceImages")) {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("response", new JSONObject(response.body().string()));
                                String data = jsonObject.getJSONObject("response").getString("Data");
                                String destr = Aes256.decrypt(data, new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(new Date()) + "1201");
//                                String destr = Aes256.decrypt(data, "12345678");
                                if (BuildConfig.DEBUG)
                                    printMsg(destr);
                                MediaType contentType = response.body().contentType();
                                ResponseBody responseBody = ResponseBody.create(contentType, destr);
                                return response.newBuilder().body(responseBody).build();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return response;
                })
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .header("username", Utils.getCustMobile())
                            .addHeader("authorization", "Bearer " + Utils.getToken())
                            .build();
                    return chain.proceed(request);
                })
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.apiEndpoint)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    private void printMsg(String msg) {
        int chunkCount = msg.length() / 4050;     // integer division
        for (int i = 0; i <= chunkCount; i++) {
            int max = 4050 * (i + 1);
            if (max >= msg.length()) {
                System.out.println(msg.substring(4050 * i));
            } else {
                System.out.println(msg.substring(4050 * i, max));
            }
        }
    }

    public APIServices getService() {
        return retrofit.create(APIServices.class);
    }
}