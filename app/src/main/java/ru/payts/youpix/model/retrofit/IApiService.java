package ru.payts.youpix.model.retrofit;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.payts.youpix.model.entity.PhotoSet;

public interface IApiService {
    @GET("api")
    Observable<PhotoSet> getPhotoSetBySearch(@Query("key") String key,  //Your API key:
                                             @Query("q") String query,  //A URL encoded search term. If omitted, all images are returned. This value may not exceed 100 characters.
                                             @Query("editors_choice") boolean editors_choice, //Select images that have received an Editor's Choice award. Accepted values: "true", "false"Default: "false"
                                             @Query("page") int page,    //Returned search results are paginated. Use this parameter to select the page number.
                                             @Query("per_page") int per_page,
                                             @Query("colors") String colors); //Filter images by color properties. A comma separated list of values may be used to select multiple properties.

    @GET("api")
    Observable<PhotoSet> getPhotoByID(@Query("key") String key,  //Your API key:
                                      @Query("id") String picID
    ); //Filter images by color properties. A comma separated list of values may be used to select multiple properties.

}
