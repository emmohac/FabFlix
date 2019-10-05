package edu.uci.ics.huymt2.service.movies.core;

import edu.uci.ics.huymt2.service.movies.MovieService;
import edu.uci.ics.huymt2.service.movies.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.movies.models.MovieSearchRequestModel;
import edu.uci.ics.huymt2.service.movies.models.StarSearchRequestModel;
import edu.uci.ics.huymt2.service.movies.models.VerifyPrivilegeRequestModel;
import edu.uci.ics.huymt2.service.movies.models.VerifyPrivilegeResponseModel;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
    Most of the method in logical handler use these methods to help processing the request from the user.
    These methods are varies and does not follow any certain order.
 */
public class HelpMe {
    public static ArrayList<Movie> retrieveMovieFromRequestQuery(MovieSearchRequestModel requestModel){
        ServiceLogger.LOGGER.info("HelpMe:: retrieving movie...");
        String title = requestModel.getTitle();
        String genre = requestModel.getGenre();
        Integer year = requestModel.getYear();
        String director = requestModel.getDirector();
        Boolean hidden = requestModel.getHidden();
        Integer limit = requestModel.getLimit();
        Integer offset = requestModel.getOffset();
        String orderby = requestModel.getOrderby();
        String direction = requestModel.getDirection();

        // Putting the item to be returned in an ArrayList because it is not a complex data structure
        ArrayList<Movie> toReturn = new ArrayList<>();
        try{
            String query = "select M.id, M.title, M.director, M.year, R.rating, R.numVotes, M.hidden " +
                    "from movies M, ratings R, genres_in_movies GIM, genres G " +
                    "where R.movieId = M.id and GIM.movieId = M.id and GIM.genreId = G.id";

            // Validating information given from the request model. The user can decide to give or not give any
            // information. If the user does not provide any information. Default information will be assigned to the
            // variables
            if (title != null)
                query += " AND M.title LIKE '%"+title+"%'";
            if (director != null)
                query += " AND M.director LIKE '%"+director+"%'";
            if (year != null && year > 1900)
                query += " AND M.year = "+year;
            if (genre != null)
                query += " AND G.name LIKE '%"+genre+"%'";

            String result = " GROUP BY " + orderby + " ORDER BY "+orderby+" "+direction+" LIMIT "+offset+","+limit+";";
            query += result;

            PreparedStatement ps = MovieService.getCon().prepareStatement(query);

            ServiceLogger.LOGGER.info("HelpMe:: Trying query: "+ps.toString());
            ResultSet rs = ps.executeQuery();
            // Adding movies to the ArrayList
            while (rs.next())
                toReturn.add(new Movie(rs.getString(1), rs.getString("title"), rs.getString("director"), rs.getInt("year"), rs.getFloat("rating"), rs.getInt("numVotes")));
            ServiceLogger.LOGGER.info("HelpMe:: Successfully retrieved movie...\n");
            ServiceLogger.LOGGER.info("HelpMe:: length of array of movie: "+toReturn.size());
            return toReturn;
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("HelpMe:: failure to retrieve movie...");
            e.printStackTrace();
        }
        return null;
    }

    // This method will connect to the IDM microservice to access the privilege endpoint and verify the privilege level
    public static VerifyPrivilegeResponseModel verifyUserPrivilege(String email, int plevel){
        ServiceLogger.LOGGER.info("Verifying privilege of email: "+email);
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        String IDM_URI = MovieService.getMovieConfigs().getIdmConfigs().getIdmUri();
        String IDM_ENDPOINT = MovieService.getMovieConfigs().getIdmConfigs().getPrivilegePath();
        // The result would be /api/idm/privilege and this is how the method
        WebTarget webTarget = client.target(IDM_URI).path(IDM_ENDPOINT);
        Invocation.Builder invocation = webTarget.request(MediaType.APPLICATION_JSON); // Building the media type of the
                                                                                    // request that is going to be sent
        // Creating a request model to send and verify
        VerifyPrivilegeRequestModel requestModel = new VerifyPrivilegeRequestModel(email, plevel);

        ServiceLogger.LOGGER.info("Sending request...");
        Response response = invocation.post(Entity.entity(requestModel, MediaType.APPLICATION_JSON));
        // Send the request as a post method to the privilege endpoint in IDM
        ServiceLogger.LOGGER.info("Sent!");

        VerifyPrivilegeResponseModel responseModel = response.readEntity(VerifyPrivilegeResponseModel.class);
        ServiceLogger.LOGGER.info("resultCode: "+responseModel.getResultCode());
        return responseModel;
    }

    // Retrieving all the genres from the table
    public static ArrayList<Genre> retrieveGenreFromDB(){
        ArrayList<Genre> toReturn = new ArrayList<>();

        try{
            String query = "SELECT id, name FROM genres;";
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                toReturn.add(new Genre(rs.getInt("id"), rs.getString("name")));
            return toReturn;
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("HelpMe:: failure to retrieve list of genres.");
        }
        return null;
    }

    // Retrieve the genres of a specific movie given the movie id
    public static ArrayList<Genre> retrieveGenreOfMovieID(String movieId){
        ArrayList<Genre> toReturn = new ArrayList<>();
        ServiceLogger.LOGGER.info("Retrieving genres for movieID: "+movieId);
        try{
            // Select the required information given the movie ID and the genre ID
            String query = "SELECT genres.id, genres.name FROM genres, genres_in_movies WHERE genres_in_movies.movieId = ? AND genres.id = genres_in_movies.genreId;";
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieId);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                toReturn.add(new Genre(rs.getInt("id"), rs.getString("name")));
            return toReturn;

        }catch (SQLException e){
            ServiceLogger.LOGGER.info("HelpMe:: failure to retrieve genre for movieId above.");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return null;
    }

    // Retrieving the movie stars of a specific movie given the movie ID
    public static ArrayList<Star> retrieveStarOfMovieID(String movieId){
        ArrayList<Star> toReturn = new ArrayList<>();
        ServiceLogger.LOGGER.info("Retrieving stars for movieID: "+movieId);
        try{
            // Select the required information given the movie ID and the star ID
            String query = "SELECT S.id, S.name, S.birthYear FROM stars S, stars_in_movies SIM WHERE S.id = SIM.starId AND SIM.movieId = ?;";
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                toReturn.add(new Star(rs.getString("id"), rs.getString("name"), rs.getInt("birthYear")));
            return toReturn;
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("HelpMe:: failure to retrieve start for movieId above.");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return null;
    }

    // Validating the limit given by the user
    public static int verifyLimit(Integer limit){
        if (limit == null || limit == 10 || limit == 25 || limit == 50 || limit == 100)
            return limit;
        return 10;
    }

    // Validating the offset given by the user
    public static int verifyOffset(Integer limit, Integer offset){
        ServiceLogger.LOGGER.info("Verifying offset...");
        if (offset == null || offset < 0 || (offset % limit != 0))
            return 0;
        return offset;
    }

    // Validating the order by for movie star endpoint
    public static String verifyOrderbyForStar(String orderby){
        ServiceLogger.LOGGER.info("Verifying orderby...");
        if (orderby == null) {
            ServiceLogger.LOGGER.info("orderby is null");
            return "name";
        }
        if (orderby != null && (orderby.equals("name") || orderby.equals("birthYear")))
            return orderby;
        ServiceLogger.LOGGER.info("Returning 'name'...");
        return "name";
    }

    // Validating the direction given by the user
    public static String verifyDirection(String direction){
        ServiceLogger.LOGGER.info("Verifying direction...");
        if (direction != null && (direction.equalsIgnoreCase("asc") || direction.equalsIgnoreCase("desc")))
            return direction;
        ServiceLogger.LOGGER.info("Direction using default");
        return "asc";
    }


    public static ArrayList<Star> retrieveStarFromRequestQuery(StarSearchRequestModel requestModel){
        String name = requestModel.getName();
        String movieTitle = requestModel.getMovieTitle();
        Integer year = requestModel.getBirthYear();
        Integer limit = requestModel.getLimit();
        Integer offset = requestModel.getOffset();
        String orderby = requestModel.getOrderby();
        String direction = requestModel.getDirection();

        ArrayList<Star> toReturn = new ArrayList<>();
        try{
            String query = "SELECT stars.id, stars.name, stars.birthYear FROM stars, movies, stars_in_movies WHERE movies.id = stars_in_movies.movieId AND stars.id = stars_in_movies.starId";
            if (name != null)
                query += " AND stars.name LIKE '%"+name+"%'";
            if (movieTitle != null)
                query += " AND movies.title LIKE '%"+movieTitle+"%'";
            if (year != null)
                query += " AND stars.birthYear = "+year;
            query += " GROUP BY " + orderby + " ORDER BY "+orderby+" "+direction+" LIMIT "+offset+","+limit+";";

            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ServiceLogger.LOGGER.info("Trying query "+query);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                toReturn.add(new Star(rs.getString("id"), rs.getString("name"), rs.getInt("birthYear")));
            return toReturn;
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("HelpMe:: failure to retrieve star from query");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return null;
    }

    // Generating ID for movie and start
    public static String generateID(String type, int id){
        String prefix = type;
        String number = Integer.toString(id);
        int numberLen = number.length();
        int numOfZero = 9 - (numberLen + prefix.length());
        for (int i = 0; i < numOfZero; ++i)
            prefix += "0";
        return prefix + number;
    }

    public static boolean verifyPrivilegeLevel(String email){
        VerifyPrivilegeResponseModel rm = verifyUserPrivilege(email, ResultCode.EMPLOYEE);
        int resultCode = rm.getResultCode();
        if (resultCode != ResultCode.SUFFICIENT_PRIVILEGE)
            return false;
        return true;
    }

    public static String generateMessageFor(int resultCode){
        switch (resultCode){
            case ResultCode.MOVIE_FOUND:
                return ResponseMessage.MOVIE_FOUND;
            case ResultCode.MOVIE_ALREADY_EXISTED:
                return ResponseMessage.MOVIE_ALREADY_EXISTED;
            case ResultCode.MOVIE_NOT_ADDED:
                return ResponseMessage.MOVIE_NOT_ADDED;
            case ResultCode.MOVIE_NOT_REMOVED:
                return ResponseMessage.MOVIE_NOT_REMOVED;
            case ResultCode.MOVIE_NOT_FOUND:
                return ResponseMessage.MOVIE_NOT_FOUND;
            case ResultCode.JSON_MAP:
                return ResponseMessage.JSON_MAP;
            case ResultCode.JSON_PARSE:
                return ResponseMessage.JSON_PARSE;
            case ResultCode.GENRE_NOT_ADDED:
                return ResponseMessage.GENRE_NOT_ADDED;
            case ResultCode.SUCCESSFULLY_ADDED_GENRE:
                return ResponseMessage.SUCCESSFULLY_ADDED_GENRE;
            case ResultCode.SUCCESSFULLY_RETRIEVED_GENRE:
                return ResponseMessage.SUCCESSFULLY_RETRIEVED_GENRE;
            case ResultCode.RATING_NOT_UPDATED:
                return ResponseMessage.RATING_NOT_UPDATED;
            case ResultCode.SUCCESSFULLY_UPDATED_RATING:
                return ResponseMessage.SUCCESSFULLY_UPDATED_RATING;
            case ResultCode.STAR_FOUND:
                return ResponseMessage.STAR_FOUND;
            case ResultCode.STAR_ALREADY_EXISTED:
                return ResponseMessage.STAR_ALREADY_EXISTED;
            case ResultCode.STAR_NOT_FOUND:
                return ResponseMessage.STAR_NOT_FOUND;
            case ResultCode.STAR_NOT_ADDED:
                return ResponseMessage.STAR_NOT_ADDED;
            case ResultCode.STAR_MOVIE_ALREADY_EXISTED:
                return ResponseMessage.STAR_MOVIE_ALREADY_EXISTED;
            case ResultCode.SUCCESSFULLY_ADDED_STAR:
                return ResponseMessage.SUCCESSFULLY_ADDED_STAR;
            case ResultCode.SUCCESSFULLY_ADDED_STAR_MOVIE:
                return ResponseMessage.SUCCESSFULLY_ADDED_STAR_MOVIE;
            case ResultCode.INSUFFICIENT_PRIVILEGE:
                return ResponseMessage.INSUFFICIENT_PRIVILEGE;
            case ResultCode.SUFFICIENT_PRIVILEGE:
                return ResponseMessage.SUFFICIENT_PRIVILEGE;
            default:
                return "Unknown code";
        }
    }
}
