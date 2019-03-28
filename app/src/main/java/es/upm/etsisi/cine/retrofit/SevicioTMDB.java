package es.upm.etsisi.cine.retrofit;

import es.upm.etsisi.cine.retrofit.model.Respuesta;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public class SevicioTMDB {

    public interface IServicioTMDB {
        @GET("search/movie")
        /*Call<Respuesta> searchPelicula(@Query("api_key") String api_key,
                                             @Query("query") String query);
        */
        Observable<Respuesta> searchPelicula(@Query("api_key") String api_key,
                                             @Query("query") String query,
                                             @Query("page") int pagina);
    }

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            //AÑADIR CONVERTER GSON
            .addConverterFactory(GsonConverterFactory.create())
            //AÑADIR ADAPTER RXJAVA
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    private static IServicioTMDB servicio;

    public static IServicioTMDB getServicio() {
        if (servicio==null)
            servicio = retrofit.create(IServicioTMDB.class);
        return servicio;
    }
}
