package com.jhnr.myclub.utils;

import com.jhnr.myclub.entities.Club;
import com.jhnr.myclub.entities.Event;
import com.jhnr.myclub.entities.Review;
import com.jhnr.myclub.entities.enums.ClubTypes;
import com.jhnr.myclub.generated.api.dto.ClubModel;
import com.jhnr.myclub.generated.api.dto.EventModel;
import com.jhnr.myclub.generated.api.dto.ReviewModel;
import com.jhnr.myclub.generated.api.dto.UserModel;
import com.jhnr.myclub.entities.AppUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityToDtoMapper {
    public static ClubModel convertToClubModel(Club club) {
        ClubModel clubModel = new ClubModel();
        clubModel.setId(club.getId().intValue());
        clubModel.setNom(club.getNom());
        clubModel.setDescription(club.getDescription());
        clubModel.setLongitude(club.getLongitude().toString());
        clubModel.setLatitude(club.getLatitude().toString());
        clubModel.setAdresse(club.getAdresse());
        clubModel.setTelephone(club.getTelephone());
        clubModel.setImage(club.getImage());
        clubModel.setTypes(club.getTypes().stream().map(ClubTypes::name).collect(Collectors.toList()));
        clubModel.setEvents(convertToEventModels(club.getEvents()));
        clubModel.setReviews(convertToReviewModels(club.getReviews()));
        clubModel.setUsers(convertToUserModels(club.getUsers()));
        return clubModel;
    }

    public static List<EventModel> convertToEventModels(List<Event> events) {
        return events.stream()
                .map(EntityToDtoMapper::convertToEventModel)
                .collect(Collectors.toList());
    }

    public static EventModel convertToEventModel(Event event) {
        EventModel eventModel = new EventModel();
        eventModel.setId(event.getId().intValue());
        eventModel.setDateAndTime(event.getDateAndTime().toString());
        eventModel.setLocation(event.getLocation());
        eventModel.setName(event.getName());
        return eventModel;
    }

    public static List<ReviewModel> convertToReviewModels(List<Review> reviews) {
        return reviews.stream()
                .map(EntityToDtoMapper::convertToReviewModel)
                .collect(Collectors.toList());
    }

    public static ReviewModel convertToReviewModel(Review review) {
        ReviewModel reviewModel = new ReviewModel();
        reviewModel.setIdCommentaire(review.getIdCommentaire().intValue());
        reviewModel.setNote(review.getNote());
        reviewModel.setCommentaire(review.getCommentaire());
        return reviewModel;
    }

    public static List<UserModel> convertToUserModels(List<AppUser> users) {
        return users.stream()
                .map(EntityToDtoMapper::convertToUserModel)
                .collect(Collectors.toList());
    }

    public static UserModel convertToUserModel(AppUser user) {
        UserModel userModel = new UserModel();
        userModel.setId(user.getId().intValue());
        userModel.setUsername(user.getUserName());
        userModel.setEmail(user.getEmail());
        return userModel;
    }
}
