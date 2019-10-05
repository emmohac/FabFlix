package edu.uci.ics.huymt2.service.movies.logicalhandler;

import edu.uci.ics.huymt2.service.movies.MovieService;
import edu.uci.ics.huymt2.service.movies.core.ResultCode;
import edu.uci.ics.huymt2.service.movies.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.movies.models.RatingRequestModel;
import edu.uci.ics.huymt2.service.movies.models.RatingResponseModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Ratings {
    // Updating the rating of a movie given the movie ID
    public static RatingResponseModel updateRatingFrom(RatingRequestModel requestModel){
        String id = requestModel.getId();
        Float rating = requestModel.getRating();

        ServiceLogger.LOGGER.info("Received request to update rating for movieID: "+id+ " with rating: "+rating);

        // Verify rating before process any further
        if (rating < 0 || rating > 10)
            return new RatingResponseModel(ResultCode.RATING_NOT_UPDATED);

        try{
            String queryMovie = "SELECT id FROM movies WHERE id = ?;";
            PreparedStatement psMovie = MovieService.getCon().prepareStatement(queryMovie);
            psMovie.setString(1, id);
            ResultSet rsMovie = psMovie.executeQuery();

            if (!rsMovie.next())
                return new RatingResponseModel(ResultCode.MOVIE_NOT_FOUND);

            // Calculate rating while putting the result into the table
            String queryUpdate = "UPDATE ratings SET rating = (numVotes * rating + ?) / (numVotes + 1), numVotes = numVotes + 1 WHERE movieId = ?;";
            PreparedStatement psUpdate = MovieService.getCon().prepareStatement(queryUpdate);
            psUpdate.setFloat(1, rating);
            psUpdate.setString(2, id);
            psUpdate.executeUpdate();
            ServiceLogger.LOGGER.info("Ratings:: updated rating successfully");
            return new RatingResponseModel(ResultCode.SUCCESSFULLY_UPDATED_RATING);
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("Ratings:: failure to update rating.");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return new RatingResponseModel(ResultCode.RATING_NOT_UPDATED);
    }
}
