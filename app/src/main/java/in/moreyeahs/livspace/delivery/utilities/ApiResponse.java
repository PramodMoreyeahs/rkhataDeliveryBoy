package in.moreyeahs.livspace.delivery.utilities;

import com.google.gson.JsonElement;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;


/**
 * Created by ${Saquib} on 03-05-2018.
 */

public class ApiResponse {
    public final Status status;
    /*@Nullable
    public final JsonElement data;*/
    @Nullable
    public final JsonElement data;
    @Nullable
    public final Throwable error;

    /**
     * @param status
     * @param data
     * @param error
     */
    private ApiResponse(Status status, @Nullable JsonElement data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static ApiResponse loading() {
        return new ApiResponse(Status.LOADING, null, null);
    }

    public static ApiResponse success(@NonNull JsonElement data) {
        return new ApiResponse(Status.SUCCESS, data, null);
    }

    public static ApiResponse error(@NonNull Throwable error) {
        return new ApiResponse(Status.ERROR, null, error);
    }

}
