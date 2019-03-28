package es.upm.etsisi.cine;

import android.app.Activity;
import android.app.SearchManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import es.upm.etsisi.cine.adapter.MoviesAdapter;
import es.upm.etsisi.cine.retrofit.SevicioTMDB;
import es.upm.etsisi.cine.retrofit.model.Pelicula;
import es.upm.etsisi.cine.retrofit.model.Respuesta;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class BuscarPeliculas extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private List<Pelicula> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    private int pagina;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_peliculas);

        recyclerView = (RecyclerView) findViewById(R.id.lista);

        mAdapter = new MoviesAdapter(movieList);
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.simpleSwipeRefreshLayout);
        /*swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // cancel the Visual indication of a refresh
                buscarPeliculas(query);

            }
        });*/
        swipeRefreshLayout.setOnRefreshListener(() ->
                buscarPeliculas(query)
        );
    }

    private SwipeRefreshLayout swipeRefreshLayout;

    Observable observable;

    private void buscarPeliculas(String query) {

        SevicioTMDB.getServicio()
                .searchPelicula("313ff7692155916f8308b10fbbd435c7",query, pagina)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMapIterable( x -> { pagina++;
                                         return x.getPeliculas();})
                .subscribe( pelicula -> {
                    Log.e("Pagina actual", ""+pelicula.getTitle());
                    movieList.add(0, pelicula);
                    mAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    Log.e("PAGINA", "" + pagina);
                }, error -> {
                    Log.e("Error", "error");
                    swipeRefreshLayout.setRefreshing(false);
                });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        // Retrieve the SearchView and plug it into SearchManager
        //final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        final SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        pagina = 1;
        movieList.clear();
        this.query = query;
        buscarPeliculas(this.query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        pagina = 1;
        movieList.clear();
        this.query = query;
        buscarPeliculas(this.query);
        return false;
    }
}

