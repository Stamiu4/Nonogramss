package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entities.Rating;

import java.util.List;

public interface RatingService {



    void setRating(Rating rating) throws RatingException;
    int getAverageRating(String game) throws RatingException;
    int getRating(String game, String player) throws RatingException;
}