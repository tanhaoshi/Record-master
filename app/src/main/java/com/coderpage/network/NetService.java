package com.coderpage.network;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 */

public interface NetService {

    @GET
    Observable<ResponseBody> getIndexList(
        @Url String url
    );
}
