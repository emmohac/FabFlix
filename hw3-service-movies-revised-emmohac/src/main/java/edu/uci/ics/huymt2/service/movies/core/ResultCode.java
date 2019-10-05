package edu.uci.ics.huymt2.service.movies.core;

// This class contains the result code of of the appropriate endpoint
public class ResultCode {
    public static int movieID = 0;
    public static int starID = 0;
    public static final int MOVIE_FOUND = 210;
    public static final int MOVIE_NOT_FOUND = 211;
    public static final int STAR_FOUND = 212;
    public static final int STAR_NOT_FOUND = 213;
    public static final int JSON_PARSE = -3;
    public static final int JSON_MAP = -2;
    public static final int SUCCESSFULLY_ADDED_MOVIE = 214;
    public static final int MOVIE_NOT_ADDED = 215;
    public static final int MOVIE_ALREADY_EXISTED = 216;
    public static final int SUCCESSFULLY_REMOVED_MOVIE = 240;
    public static final int MOVIE_NOT_REMOVED = 241;
    public static final int MOVIE_ALREADY_REMOVED = 242;
    public static final int SUCCESSFULLY_RETRIEVED_GENRE = 219;
    public static final int SUCCESSFULLY_ADDED_GENRE = 217;
    public static final int GENRE_NOT_ADDED = 218;
    public static final int SUCCESSFULLY_ADDED_STAR = 220;
    public static final int STAR_NOT_ADDED = 221;
    public static final int STAR_ALREADY_EXISTED = 222;
    public static final int SUCCESSFULLY_ADDED_STAR_MOVIE = 230;
    public static final int STAR_MOVIE_NOT_ADDED = 231;
    public static final int STAR_MOVIE_ALREADY_EXISTED = 232;
    public static final int SUCCESSFULLY_UPDATED_RATING = 250;
    public static final int RATING_NOT_UPDATED = 251;
    public static final int SUFFICIENT_PRIVILEGE = 140;
    public static final int INSUFFICIENT_PRIVILEGE = 141;
    public static final int ROOT = 1;
    public static final int ADMIN = 2;
    public static final int EMPLOYEE = 3;
    public static final int SERVICE = 4;
    public static final int USER = 5;
}
