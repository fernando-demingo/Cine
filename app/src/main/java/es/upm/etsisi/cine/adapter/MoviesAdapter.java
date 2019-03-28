package es.upm.etsisi.cine.adapter;
 
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import es.upm.etsisi.cine.R;
import es.upm.etsisi.cine.retrofit.model.Pelicula;

import java.util.List;
 
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {
 
    private List<Pelicula> moviesList;
 
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView imagen;

 
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            imagen = (ImageView) view.findViewById(R.id.imagen);
            //year = (TextView) view.findViewById(R.id.year);
        }
    }
 
 
    public MoviesAdapter(List<Pelicula> moviesList) {
        this.moviesList = moviesList;
    }
 
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.row, parent, false);
        ////
        MyViewHolder vh;
        //if (parent != null)
        //    vh = parent.getRootView();
        //else
            vh = new MyViewHolder(itemView);
        return vh;
    }
 
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Pelicula movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        Picasso.get()
                .load("http://image.tmdb.org/t/p/w342/"+movie.getPosterPath())
                .error(R.drawable.ic_launcher_background)
                .into(holder.imagen);
    }
 
    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}