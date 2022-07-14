package in.moreyeahs.livspace.delivery.utilities;
import com.google.gson.JsonObject;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * Created by ${Saquib} on 03-05-2018.
 */

public class ApiResponseObj {

    public final Status status;

    /*@Nullable
    public final JsonElement data;*/
    @Nullable
    public final JsonObject data;
    @Nullable
    public final Throwable error;

    private ApiResponseObj(Status status, @Nullable JsonObject data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static ApiResponseObj loading() {
        return new ApiResponseObj(Status.LOADING, null, null);
    }

    public static ApiResponseObj success(@NonNull JsonObject data) {
        return new ApiResponseObj(Status.SUCCESS, data, null);
    }

    public static ApiResponseObj error(@NonNull Throwable error) {
        return new ApiResponseObj(Status.ERROR, null, error);
    }

}
