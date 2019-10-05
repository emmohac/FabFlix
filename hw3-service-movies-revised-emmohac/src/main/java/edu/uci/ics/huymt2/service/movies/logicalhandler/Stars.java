package edu.uci.ics.huymt2.service.movies.logicalhandler;

import edu.uci.ics.huymt2.service.movies.MovieService;
import edu.uci.ics.huymt2.service.movies.core.HelpMe;
import edu.uci.ics.huymt2.service.movies.core.ResultCode;
import edu.uci.ics.huymt2.service.movies.core.Star;
import edu.uci.ics.huymt2.service.movies.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.movies.models.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Stars {
    // Retrieve a specific movie star given the movie star ID
    public static StarIDSearchResponseModel retrieveStarID(String starid){
        ServiceLogger.LOGGER.info("Received request to retrieve starID: "+starid);
        try{
            String query = "SELECT * FROM stars WHERE id = ?;";
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, starid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                StarModel sm = new StarModel(rs.getString("id"), rs.getString("name"), rs.getInt("birthYear"));
                StarIDSearchResponseModel rm = new StarIDSearchResponseModel(ResultCode.STAR_FOUND);
                rm.setStars(sm);
                ServiceLogger.LOGGER.info("Successfully retrieved starID");
                return rm;
            }
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("Stars:: failure to retrieve starID above.");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return new StarIDSearchResponseModel(ResultCode.STAR_NOT_FOUND);
    }

    // Add a star movie in the table and also the movie that the star plays in
    public static StarMovieResponseModel addStarInMovieFrom(StarMovieRequestModel requestModel){
        String starID = requestModel.getStarid();
        String movieID = requestModel.getMovieid();

        try{
            String movieQuery = "SELECT id FROM movies WHERE id = ?;";
            PreparedStatement psMovie = MovieService.getCon().prepareStatement(movieQuery);
            psMovie.setString(1, movieID);
            ResultSet rsMovie = psMovie.executeQuery();

            // Checking if the movie actually exists in the table
            if (!rsMovie.next())
                return new StarMovieResponseModel(ResultCode.MOVIE_NOT_FOUND);

            String simQuery = "SELECT * FROM stars_in_movies WHERE starId = ? AND movieId = ?;";
            PreparedStatement psSIM = MovieService.getCon().prepareStatement(simQuery);
            psSIM.setString(1, starID);
            psSIM.setString(2, movieID);
            ResultSet rsSIM = psSIM.executeQuery();

            // Checking if the movie star already exists in the table
            if (rsSIM.next())
                return new StarMovieResponseModel(ResultCode.STAR_MOVIE_ALREADY_EXISTED);

            String insertSIM = "INSERT INTO stars_in_movies(starId, movieId) VALUES(?,?);";
            PreparedStatement psInsert = MovieService.getCon().prepareStatement(insertSIM);
            psInsert.setString(1, starID);
            psInsert.setString(2, movieID);
            psInsert.execute();
            return new StarMovieResponseModel(ResultCode.SUCCESSFULLY_ADDED_STAR_MOVIE);
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("Stars:: failure to add star in movie");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return new StarMovieResponseModel(ResultCode.STAR_MOVIE_NOT_ADDED);
    }

    // Searching for a movie start
    public static StarSearchResponseModel searchStarFrom(StarSearchRequestModel requestModel){
        ServiceLogger.LOGGER.info("Retrieving star from DB...");

        ArrayList<Star> stars = HelpMe.retrieveStarFromRequestQuery(requestModel);

        if (stars == null)
            return new StarSearchResponseModel(ResultCode.STAR_NOT_FOUND);
        ServiceLogger.LOGGER.info("Star is not null. Stars length "+stars.size());
        int len = stars.size();
        StarModel[] array = new StarModel[len];
        for (int i = 0; i < len; ++i)
            array[i] = StarModel.buildModelFromObject(stars.get(i));
        StarSearchResponseModel rm = new StarSearchResponseModel(ResultCode.STAR_FOUND);
        rm.setStars(array);
        return rm;
    }

    // Adding a movie star
    public static StarAddResponseModel addStarFrom(StarAddRequestModel requestModel){
        String name = requestModel.getName();
        Integer birthYear = requestModel.getBirthYear();

        ServiceLogger.LOGGER.info("Stars:: Received request to add star into DB: "+name);

        try{
            String queryCheck = "SELECT stars.id FROM stars WHERE stars.name LIKE ? AND stars.birthYear = ?;";
            PreparedStatement psCheck = MovieService.getCon().prepareStatement(queryCheck);
            psCheck.setString(1, "%"+name+"%");
            psCheck.setInt(2, birthYear);
            ResultSet rsCheck = psCheck.executeQuery();

            // Check if the movie star is already exists in the table
            if (rsCheck.next())
                return new StarAddResponseModel(ResultCode.STAR_ALREADY_EXISTED);

            ServiceLogger.LOGGER.info("Star: "+name+" is not existed in DB. Inserting...");

            // Generating ID for movie star. All new star ID will start with ss
            String starID = HelpMe.generateID("ss", ++ResultCode.starID);

            String queryInsert = "INSERT INTO stars(id, name, birthYear) VALUES(?,?,?);";
            PreparedStatement psInsert = MovieService.getCon().prepareStatement(queryInsert);
            psInsert.setString(1, starID);
            psInsert.setString(2, name);
            psInsert.setInt(3, (birthYear > 2019 ? null : birthYear));
            psInsert.execute();
            ServiceLogger.LOGGER.info("Stars:: inserted star into DB");
            return new StarAddResponseModel(ResultCode.SUCCESSFULLY_ADDED_STAR);
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("Stars:: failure to add star.");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return new StarAddResponseModel(ResultCode.STAR_NOT_ADDED);
    }
}
