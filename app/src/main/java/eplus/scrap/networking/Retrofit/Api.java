package eplus.scrap.networking.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by admin on 3/22/2018.
 */

public interface Api {
    public static String BASE_URL = "https://is-ticket1.mysterycircus.jp/api/";

    //             https://is-ticket1.mysterycircus.jp/api/product
    //             https://is-ticket1.mysterycircus.jp/api/product?item_per_page=20&page=1&show_type=coming_soon&lang_code=ja

    @GET("/product")
    @Headers("secret_key: pLgcv/DJCQgqdKpfxivx9jvZRF9MtIYw9c7PQB670yo=")
    Call<Data> getRetrofitData(@Query("item_per_page") int item,@Query("page") String page,@Query("show_type") String showType,@Query("lang_code") String lang );
}
