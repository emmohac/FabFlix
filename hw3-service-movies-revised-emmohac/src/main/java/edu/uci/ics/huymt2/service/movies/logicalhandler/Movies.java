package edu.uci.ics.huymt2.service.movies.logicalhandler;

import edu.uci.ics.huymt2.service.movies.MovieService;
import edu.uci.ics.huymt2.service.movies.core.*;
import edu.uci.ics.huymt2.service.movies.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.movies.models.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class Movies {
    // This method helps retrieve all the movie from the table given the inputs from the user
    public static MovieSearchResponseModel retrieveMovieFromDB(MovieSearchRequestModel requestModel){
        ServiceLogger.LOGGER.info("Retrieving movie from DB...");
        // The array list holds all the movie from the request. The movie will be store in an array as a more simple
        // data structure
        ArrayList<Movie> movies = HelpMe.retrieveMovieFromRequestQuery(requestModel);
        int len = movies.size();
        if (movies == null || len == 0)
            return new MovieSearchResponseModel(ResultCode.MOVIE_NOT_FOUND);

        ServiceLogger.LOGGER.info("Number of movie: "+len);
        MovieModel[] toReturn = new MovieModel[len];

        for (int i = 0; i < len; ++i) {
            ServiceLogger.LOGGER.info("Making movieModel...");
            toReturn[i] = MovieModel.buildModelFromObject(movies.get(i));
        }
        MovieSearchResponseModel rm = new MovieSearchResponseModel(ResultCode.MOVIE_FOUND);
        rm.setMovies(toReturn);
        return rm;
    }

    // This method remove a movie from the table. However, the movie hidden field is set to true to normal user, so
    // user who is not an admin, mod will not see the movie. The actual movie is not removed from the table.
    public static MovieResponseModel removeMovieFromDB(String movieId){
        ServiceLogger.LOGGER.info("Received request remove movieId: "+movieId);

        try{
            String queryFind = "SELECT id FROM movies WHERE id = ?;";
            PreparedStatement psFind = MovieService.getCon().prepareStatement(queryFind);
            psFind.setString(1, movieId);
            ResultSet rsFind = psFind.executeQuery();
            // Checking if the movie exists in the table
            if (!rsFind.next())
                return new MovieResponseModel(ResultCode.MOVIE_NOT_REMOVED);

            String queryCheck = "SELECT id FROM movies WHERE hidden = 1 AND id = ?;";
            PreparedStatement psCheck = MovieService.getCon().prepareStatement(queryCheck);
            psCheck.setString(1, movieId);
            ResultSet rsCheck = psCheck.executeQuery();

            // Checking if the movie has already been removed.
            if (rsCheck.next())
                return new MovieResponseModel(ResultCode.MOVIE_ALREADY_REMOVED);

            ServiceLogger.LOGGER.info("Movies:: movie has not been removed. Trying to remove...");
            // The movie is not remove. Setting the hidden field of the movie to true so that it becomes invisible to
            // all normal user
            String query = "UPDATE movies SET hidden = ? WHERE id = ?;";
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setInt(1, 1);
            ps.setString(2, movieId);
            ServiceLogger.LOGGER.info("Trying query " +ps.toString());
            ps.executeUpdate();
            return new MovieResponseModel(ResultCode.SUCCESSFULLY_REMOVED_MOVIE);

        }catch (SQLException e){
            ServiceLogger.LOGGER.info("Movie:: failure to remove movie.");
            e.printStackTrace();
        }
        return new MovieResponseModel(ResultCode.MOVIE_NOT_REMOVED);
    }

    // This method retrieve a movie given its ID
    public static MovieIDSearchResponseModel retrieveMovieID(String movieid, Boolean isHidden){
        ServiceLogger.LOGGER.info("Received request retrieve movieID: "+movieid);

        // Retrieving the genres of that movie based on its movie ID
        ArrayList<Genre> genres = HelpMe.retrieveGenreOfMovieID(movieid);

        int len = genres.size();
        GenreModel[] genreArray = new GenreModel[len];

        // Putting the genre from the array list into an array
        for (int i = 0; i < len; ++i)
            genreArray[i] = GenreModel.buildModelFromObject(genres.get(i));

        // Retrieving the stars of that movie based on its movie ID
        ArrayList<Star> stars = HelpMe.retrieveStarOfMovieID(movieid);

        len = stars.size();
        StarModel[] starArray = new StarModel[len];

        // Putting the stars from the array lsit into an array
        for (int i = 0; i < len; ++i)
            starArray[i] = StarModel.buildModelFromObject(stars.get(i));

        try{
            // Retrieving the movie
            String query = "SELECT * FROM movies, ratings WHERE ratings.movieId = movies.id AND movies.id = ?";
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieid);
            ServiceLogger.LOGGER.info("Movies:: Trying query "+ps.toString());
            ResultSet rs = ps.executeQuery();
            // Check if there exists a movie with the given movie ID
            if (rs.next()){
                // Checking if the movie has been removed or not
                // *** NOTICE: the movie is also not visible to admin or mod. A different implementation might improve
                // *** this
                if (isHidden && rs.getInt("hidden") == 1)
                    return new MovieIDSearchResponseModel(ResultCode.MOVIE_ALREADY_REMOVED);

                // Making the movie model with the appropriate fields
                MovieIDModel movieIDModel = new MovieIDModel(rs.getString("id"), rs.getString("title"), genreArray, starArray);
                movieIDModel.setDirector(rs.getString("director"));
                movieIDModel.setBackdrop_path(rs.getString("backdrop_path"));
                movieIDModel.setBudget(rs.getInt("budget"));
                movieIDModel.setYear(rs.getInt("year"));
                movieIDModel.setPoster_path(rs.getString("poster_path"));
                movieIDModel.setOverview(rs.getString("overview"));
                movieIDModel.setNumVotes(rs.getInt("numVotes"));
                movieIDModel.setRating(rs.getFloat("rating"));
                movieIDModel.setRevenue(rs.getInt("revenue"));
                MovieIDSearchResponseModel rm = new MovieIDSearchResponseModel(ResultCode.MOVIE_FOUND);
                rm.setMovie(movieIDModel);
                return rm;
            }
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("Movies:: failure to retrieve movie by movieID");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return new MovieIDSearchResponseModel(ResultCode.MOVIE_NOT_FOUND);
    }

    // This method adds a new movie into the table. Each new movie will have the ID starting with cs
    public static MovieAddResponseModel addMovieToDBFrom(MovieAddRequestModel requestModel){
        String title = requestModel.getTitle();
        String director = requestModel.getDirector();
        Integer year = requestModel.getYear();
        String backdrop_path = requestModel.getBackdrop_path();
        Integer budget = requestModel.getBudget();
        String overview = requestModel.getOverview();
        String poster_path = requestModel.getPoster_path();
        Integer revenue = requestModel.getRevenue();
        GenreRequestModel[] genres = requestModel.getGenres();

        ServiceLogger.LOGGER.info("Movies:: Received request to insert movie title: "+title);
        try{
            String queryCheck = "SELECT movies.title, movies.year, movies.director FROM movies WHERE title = ? AND movies.year = ? AND director = ?;";
            PreparedStatement psCheck = MovieService.getCon().prepareStatement(queryCheck);
            psCheck.setString(1, title);
            psCheck.setInt(2, year);
            psCheck.setString(3, director);
            ResultSet rsCheck = psCheck.executeQuery();

            // Checking if the movie already existed in the table
            if (rsCheck.next())
                return new MovieAddResponseModel(ResultCode.MOVIE_ALREADY_EXISTED);

            ServiceLogger.LOGGER.info("Movies:: movie title "+title+" is not in DB. Inserting...");

            // Generating the movie. ResultCode.movieID starts with 0.
            String movieID = HelpMe.generateID("cs",++ResultCode.movieID);
            ServiceLogger.LOGGER.info("Movies:: Inserting new movieID: "+movieID);
            // Constructing the insertion query
            String query = "INSERT INTO movies(id, title, year, director, backdrop_path, budget, overview, poster_path, revenue)" +
                    " VALUES(?, ?, ? ,? ,? ,? ,? ,? ,?);";

            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieID);
            ps.setString(2, title);
            ps.setInt(3, year);
            ps.setString(4, director);

            // Checking for condition.
            if (backdrop_path != null)
                ps.setString(5, backdrop_path);
            else
                ps.setNull(5, Types.VARCHAR);

            if (budget != null)
                ps.setInt(6, budget);
            else
                ps.setInt(6, 0);

            if (overview != null)
                ps.setString(7, overview);
            else
                ps.setNull(7, Types.VARCHAR);

            if (poster_path != null)
                ps.setString(8, poster_path);
            else
                ps.setNull(8, Types.VARCHAR);

            if (revenue != null)
                ps.setInt(9, revenue);
            else
                ps.setInt(9,0);

            ServiceLogger.LOGGER.info("Trying query: "+ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Movies:: Inserted new movie into DB with title: "+title);

            // Inserting new rating for the movie. The rating should start with 0 and also 0 for number of votes
            String queryRating = "INSERT INTO ratings(movieId, rating, numVotes) VALUES(?,?,?);";
            PreparedStatement psRating = MovieService.getCon().prepareStatement(queryRating);
            psRating.setString(1, movieID);
            psRating.setFloat(2, 0.0f);
            psRating.setInt(3, 0);
            psRating.execute();

            int len = genres.length;
            for (int i = 0; i < len; ++i){
            String genreName = genres[i].getName();
            genreName = genreName.substring(0,1).toUpperCase() + genreName.substring(1).toLowerCase();

            String checkGenre = "SELECT name FROM genres WHERE name LIKE ?;";
            PreparedStatement psLookup = MovieService.getCon().prepareStatement(checkGenre);
            psLookup.setString(1, genreName);
            ServiceLogger.LOGGER.info("Trying query: "+psLookup.toString());
            ServiceLogger.LOGGER.info("Looking up new genre name in DB...");
            ResultSet rsLookup = psLookup.executeQuery();
                // Checking of the new genre of the new movie is already existed in the table
                if (!rsLookup.next()) {
                    ServiceLogger.LOGGER.info("Adding genre into DB " + genreName);
                    String genreQuery = "INSERT INTO genres(name) VALUE (?);";
                    PreparedStatement psGenre = MovieService.getCon().prepareStatement(genreQuery);
                    psGenre.setString(1, genreName);
                    psGenre.execute();
                    ServiceLogger.LOGGER.info("Successfully inserted new genre: " + genreName);
                }
            }

            // Putting new genreID into array
            int[] genreID = new int[len];
            for (int i = 0; i < len; ++i) {
                String findGenre = "SELECT genres.id FROM genres WHERE genres.name LIKE ?;";
                PreparedStatement psFindGenre = MovieService.getCon().prepareStatement(findGenre);
                psFindGenre.setString(1, genres[i].getName());
                ResultSet rsGenre = psFindGenre.executeQuery();
                ServiceLogger.LOGGER.info("Movies:: finding genreID for "+genres[i].getName());
                // Checking if the genre ID is in the table
                if (rsGenre.next()){
                    ServiceLogger.LOGGER.info("Movies:: found genreID for genreName above.");
                    // Inserting the new genre into the table
                    String insertGenre = "INSERT INTO genres_in_movies(genreId, movieId) VALUES(?,?);";
                    PreparedStatement psInsert = MovieService.getCon().prepareStatement(insertGenre);
                    psInsert.setInt(1, rsGenre.getInt("id"));
                    psInsert.setString(2, movieID);
                    ServiceLogger.LOGGER.info("Trying query : "+psInsert.toString());
                    psInsert.execute();
                    ServiceLogger.LOGGER.info("Movies:: successfully added genreId and movieId into genres_in_movies...");
                    // Putting genre to array
                    genreID[i] = rsGenre.getInt("id");
                }
            }
            MovieAddResponseModel rm = new MovieAddResponseModel(ResultCode.SUCCESSFULLY_ADDED_MOVIE);
            rm.setMovieid(movieID);
            rm.setGenreid(genreID);
            return rm;
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("Movies:: failure to add movie to DB");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return new MovieAddResponseModel(ResultCode.MOVIE_ALREADY_EXISTED);
    }
}
