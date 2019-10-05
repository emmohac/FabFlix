package edu.uci.ics.huymt2.service.movies.logicalhandler;

import edu.uci.ics.huymt2.service.movies.MovieService;
import edu.uci.ics.huymt2.service.movies.core.Genre;
import edu.uci.ics.huymt2.service.movies.core.HelpMe;
import edu.uci.ics.huymt2.service.movies.core.Movie;
import edu.uci.ics.huymt2.service.movies.core.ResultCode;
import edu.uci.ics.huymt2.service.movies.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.movies.models.GenreModel;
import edu.uci.ics.huymt2.service.movies.models.GenreRequestModel;
import edu.uci.ics.huymt2.service.movies.models.MovieResponseModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Genres {
    // Retrieving the genres from table using the ArrayList from HelpMe and make it become and array so that
    // Jackson can parse it into JSON easily
    public static MovieResponseModel retrieveAllGenreFromDB(){
        ArrayList<Genre> genres = HelpMe.retrieveGenreFromDB();
        int len = genres.size();
        GenreModel[] array = new GenreModel[len];
        for (int i = 0; i < len; ++i)
            array[i] = GenreModel.buildModelFromObject(genres.get(i));
        MovieResponseModel rm = new MovieResponseModel(ResultCode.SUCCESSFULLY_RETRIEVED_GENRE);
        rm.setGenres(array);
        ServiceLogger.LOGGER.info("Successfully retrieve all genre.");
        return rm;
    }

    // Adding a genre into the table
    public static MovieResponseModel addGenreToDB(GenreRequestModel requestModel){
        String geneName = requestModel.getName();

        try{
            String query = "SELECT name FROM genres WHERE name LIKE ?;";
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, geneName);
            ResultSet rs = ps.executeQuery();

            // Checking if the given genre is already existed in the table
            if (rs.next())
                return new MovieResponseModel(ResultCode.GENRE_NOT_ADDED);

            ServiceLogger.LOGGER.info("Genres:: DB does not have this genre. Inserting new genre.");
            String queryInsert = "INSERT INTO genres(name) VALUES(?);";
            PreparedStatement psInsert = MovieService.getCon().prepareStatement(queryInsert);
            psInsert.setString(1, geneName);
            psInsert.execute();
            ServiceLogger.LOGGER.info("Successfully add a genre.");
            return new MovieResponseModel(ResultCode.SUCCESSFULLY_ADDED_GENRE);
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("GenrePage:: failure to insert new genre");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return new MovieResponseModel(ResultCode.GENRE_NOT_ADDED);
    }

    // Retrieving genres of a given movie ID using the ArrayList from HelpMe to make it become and array so that
    // Jackson can parse it as JSON easily
    public static MovieResponseModel retrieveGenreOfMovie(String movieid){
        ArrayList<Genre> genres = HelpMe.retrieveGenreOfMovieID(movieid);
        if (genres == null)
            return new MovieResponseModel(ResultCode.MOVIE_NOT_FOUND);

        int len = genres.size();
        GenreModel[] array = new GenreModel[len];
        for (int i = 0; i < len; ++i)
            array[i] = GenreModel.buildModelFromObject(genres.get(i));
        MovieResponseModel rm = new MovieResponseModel(ResultCode.SUCCESSFULLY_RETRIEVED_GENRE);
        rm.setGenres(array);
        return rm;
    }
}
