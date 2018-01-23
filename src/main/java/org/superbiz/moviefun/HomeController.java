package org.superbiz.moviefun;

import jdk.nashorn.internal.runtime.AllocationStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.superbiz.moviefun.albums.Album;
import org.superbiz.moviefun.albums.AlbumFixtures;
import org.superbiz.moviefun.albums.AlbumsBean;
import org.superbiz.moviefun.movies.Movie;
import org.superbiz.moviefun.movies.MovieFixtures;
import org.superbiz.moviefun.movies.MoviesBean;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private final MoviesBean moviesBean;
    private final AlbumsBean albumsBean;
    private final MovieFixtures movieFixtures;
    private final AlbumFixtures albumFixtures;
    private final PlatformTransactionManager moviesPlatformTransactionManager;
    private final PlatformTransactionManager albumsPlatformTransactionManager;

    public HomeController(MoviesBean moviesBean, AlbumsBean albumsBean, MovieFixtures movieFixtures, AlbumFixtures albumFixtures, PlatformTransactionManager moviesPlatformTransactionManager, PlatformTransactionManager albumsPlatformTransactionManager) {
        this.moviesBean = moviesBean;
        this.albumsBean = albumsBean;
        this.movieFixtures = movieFixtures;
        this.albumFixtures = albumFixtures;
        this.moviesPlatformTransactionManager = moviesPlatformTransactionManager;
        this.albumsPlatformTransactionManager = albumsPlatformTransactionManager;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/setup")
    public String setup(Map<String, Object> model) {



        model.put("movies", createMovies());
        model.put("albums", createAlbums());

        return "setup";
    }

    public List<Movie> createMovies() {

        TransactionStatus transactionStatus = moviesPlatformTransactionManager.getTransaction(null);

        for (Movie movie : movieFixtures.load()) {
            moviesBean.addMovie(movie);
        }

        moviesPlatformTransactionManager.commit(transactionStatus);

        return moviesBean.getMovies();
    }

    public List<Album> createAlbums() {

        TransactionStatus transactionStatus = albumsPlatformTransactionManager.getTransaction(null);

        for (Album album : albumFixtures.load()) {
            albumsBean.addAlbum(album);
        }

        albumsPlatformTransactionManager.commit(transactionStatus);

        return albumsBean.getAlbums();
    }

}
