package edu.uci.ics.huymt2.service.movies.core;

// This class contains the appropriate message that correlates to the result code
public class ResponseMessage {
    public static final String MOVIE_FOUND = "Found movies with search parameters";
    public static final String MOVIE_NOT_FOUND = "No movies found with search parameters.";
    public static final String STAR_FOUND = "Found stars with search parameter.";
    public static final String STAR_NOT_FOUND = "No stars found with search parametes.";
    public static final String JSON_PARSE = "JSON parse exception.";
    public static final String JSON_MAP = "JSON mapping exception.";
    public static final String SUCCESSFULLY_ADDED_MOVIE = "Movie successfully added.";
    public static final String MOVIE_NOT_ADDED = "Could not add movie.";
    public static final String MOVIE_ALREADY_EXISTED = "Movie already exists.";
    public static final String SUCCESSFULLY_REMOVED_MOVIE = "Movie successfully removed.";
    public static final String MOVIE_NOT_REMOVED = "Could not remove movie.";
    public static final String MOVIE_ALREADY_REMOVED = "Movie has been already removed";
    public static final String SUCCESSFULLY_RETRIEVED_GENRE = "Genre successfully retrieved.";
    public static final String SUCCESSFULLY_ADDED_GENRE = "Genre successfully added.";
    public static final String GENRE_NOT_ADDED = "Genre could not be added.";
    public static final String SUCCESSFULLY_ADDED_STAR = "Star successfully added";
    public static final String STAR_NOT_ADDED = "Could not add star.";
    public static final String STAR_ALREADY_EXISTED = "Star already exists.";
    public static final String SUCCESSFULLY_ADDED_STAR_MOVIE = "Star successfully added to movie.";
    public static final String STAR_MOVIE_NOT_ADDED = "Could not add star to movie.";
    public static final String STAR_MOVIE_ALREADY_EXISTED = "Star already exists in movie.";
    public static final String SUCCESSFULLY_UPDATED_RATING = "Rating successfully updated.";
    public static final String RATING_NOT_UPDATED = "Could not update rating.";
    public static final String SUFFICIENT_PRIVILEGE = "User has sufficient privilege level.";
    public static final String INSUFFICIENT_PRIVILEGE = "User has insufficient privilege level.";
}
