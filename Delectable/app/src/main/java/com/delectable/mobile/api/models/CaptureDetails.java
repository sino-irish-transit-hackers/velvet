package com.delectable.mobile.api.models;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CaptureDetails extends Capture {

    public static int MAX_RATING_VALUE = 40;

    String short_share_url;

    String tweet;

    HashMap<String, Integer> ratings;

    PhotoHash photo;

    BaseWine base_wine;

    WineProfile wine_profile;

    String transcription_error_message;

    String location_name;

    ArrayList<Account> liking_participants;

    ArrayList<Account> commenting_participants;

    TaggeeParticipants taggee_participants;

    ArrayList<CaptureComment> comments;

    public static CaptureDetails buildFromJson(JSONObject jsonObj) {
        JSONObject payloadObj = jsonObj.optJSONObject("payload");
        CaptureDetails newResource = null;
        if (payloadObj != null && payloadObj.optJSONObject("capture") != null) {
            newResource = buildFromJson(payloadObj.optJSONObject("capture"), CaptureDetails.class);
        }

        return newResource;
    }

    /**
     * Updates existing capture with updated capture
     *
     * @param newCapture - Capture must be the same "object" / ID as this, and context must be
     *                   detailed
     */
    public void updateWithNewCapture(CaptureDetails newCapture) {
        setPrivate(newCapture.getPrivate());
        short_share_url = newCapture.getShortShareUrl();
        tweet = newCapture.getTweet();
        ratings = newCapture.getRatings();
        photo = newCapture.getPhoto();
        base_wine = newCapture.getBaseWine();
        wine_profile = newCapture.getWineProfile();
        transcription_error_message = newCapture.getTranscriptionErrorMessage();
        location_name = newCapture.getLocationName();
        liking_participants = newCapture.getLikingParticipants();
        commenting_participants = newCapture.getCommentingParticipants();
        setCapturerParticipant(newCapture.getCapturerParticipant());
        taggee_participants = newCapture.getTaggeeParticipants();
        comments = newCapture.getComments();
        e_tag = newCapture.getETag();
    }

    public ArrayList<CaptureComment> getCommentsForUserId(String id) {
        ArrayList<CaptureComment> captureComments = new ArrayList<CaptureComment>();
        if (comments != null && comments.size() > 0) {
            for (CaptureComment comment : comments) {
                if (comment.account_id.equalsIgnoreCase(id)) {
                    captureComments.add(comment);
                }
            }
        }
        return captureComments;
    }

    public CaptureComment getComment(String commentId) {
        CaptureComment captureComment = null;
        if (comments != null && comments.size() > 0) {
            for (CaptureComment comment : comments) {
                if (comment.id.equalsIgnoreCase(commentId)) {
                    captureComment = comment;
                    break;
                }
            }
        }
        return captureComment;
    }

    /**
     * Get % of Rating
     *
     * @param id = User ID linked to Ratings Hash
     * @return -1.0f if no rating exists, or value between 0.0f and 1.0f
     */
    public float getRatingPercentForId(String id) {
        float ratingPercent = -1.0f;
        ratingPercent = (float) getRatingForId(id) / MAX_RATING_VALUE;
        if (ratingPercent <= 0.0f) {
            ratingPercent = -1.0f;
        }
        return ratingPercent;
    }

    public int getRatingForId(String id) {
        int rating = -1;
        if (ratings != null && ratings.containsKey(id)) {
            rating = ratings.get(id);
        }
        return rating;
    }

    public void updateRatingForUser(String id, int rating) {
        ratings.put(id, rating);
    }

    public int getLikesCount() {
        return liking_participants != null ? liking_participants.size() : 0;
    }

    public boolean doesUserLikeCapture(String accountId) {
        boolean likesCapture = false;
        if (liking_participants != null) {
            for (Account account : liking_participants) {
                if (account.getId().equalsIgnoreCase(accountId)) {
                    likesCapture = true;
                    break;
                }
            }
        }
        return likesCapture;
    }

    public void toggleUserLikesCapture(String accountId) {
        boolean userLikedCapture = false;
        if (liking_participants != null) {
            for (Account account : liking_participants) {
                if (account.getId().equalsIgnoreCase(accountId)) {
                    userLikedCapture = true;
                    liking_participants.remove(account);
                    break;
                }
            }
        }
        if (!userLikedCapture) {
            if (liking_participants == null) {
                liking_participants = new ArrayList<Account>();
            }
            Account tempUserAccount = new Account();
            tempUserAccount.setId(accountId);
            liking_participants.add(tempUserAccount);
        }
    }

    public int getNumberTaggedParticipants() {
        int numParticipants = 0;
        if (getRegisteredParticipants() != null) {
            numParticipants += getRegisteredParticipants().size();
        }
        if (getFacebookParticipants() != null) {
            numParticipants += getFacebookParticipants().size();
        }
        if (getContactParticipants() != null) {
            numParticipants += getContactParticipants().size();
        }
        return numParticipants;
    }

    /**
     * Helper to get Display Title
     *
     * @return Wine Producer Name if it was a match, otherwise "UNIDENTIFIED"
     */
    public String getDisplayTitle() {
        String title = "";
        if (getWineProfile() != null) {
            title = getWineProfile().getProducerName();
            // Else if the capture went through that had no wine
        } else if (getTranscriptionErrorMessage() == null) {
            title = "UNIDENTIFIED";
        }
        return title;
    }

    /**
     * Helper to get Display Desciption
     *
     * @return Wine Name if it was a match, otherwise error / currently identifying wine text
     */
    public String getDisplayDescription() {
        String desc = "";
        if (getWineProfile() != null) {
            desc = getWineProfile().getName();
            // Else if the capture went through that had no wine
        } else if (getTranscriptionErrorMessage() != null) {
            desc = getTranscriptionErrorMessage();
        } else {
            desc = "We are identifying this wine.";
        }
        return desc;
    }

    public String getShortShareUrl() {
        return short_share_url;
    }

    public void setShortShareUrl(String short_share_url) {
        this.short_share_url = short_share_url;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public HashMap<String, Integer> getRatings() {
        return ratings;
    }

    public void setRatings(HashMap<String, Integer> ratings) {
        this.ratings = ratings;
    }

    public PhotoHash getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoHash photo) {
        this.photo = photo;
    }

    public BaseWine getBaseWine() {
        return base_wine;
    }

    public void setBaseWine(BaseWine base_wine) {
        this.base_wine = base_wine;
    }

    public WineProfile getWineProfile() {
        return wine_profile;
    }

    public void setWineProfile(WineProfile wine_profile) {
        this.wine_profile = wine_profile;
    }

    public String getTranscriptionErrorMessage() {
        return transcription_error_message;
    }

    public void setTranscriptionErrorMessage(String transcription_error_message) {
        this.transcription_error_message = transcription_error_message;
    }

    public String getLocationName() {
        return location_name;
    }

    public void setLocationName(String location_name) {
        this.location_name = location_name;
    }

    public ArrayList<Account> getLikingParticipants() {
        return liking_participants;
    }

    public void setLikingParticipants(ArrayList<Account> liking_participants) {
        this.liking_participants = liking_participants;
    }

    public ArrayList<Account> getCommentingParticipants() {
        return commenting_participants;
    }

    public void setCommentingParticipants(ArrayList<Account> commenting_participants) {
        this.commenting_participants = commenting_participants;
    }

    public ArrayList<Account> getRegisteredParticipants() {
        return taggee_participants != null ? taggee_participants.registered : null;
    }

    public ArrayList<TaggeeContact> getFacebookParticipants() {
        return taggee_participants != null ? taggee_participants.facebook : null;
    }

    public ArrayList<TaggeeContact> getContactParticipants() {
        return taggee_participants != null ? taggee_participants.contact : null;
    }

    public TaggeeParticipants getTaggeeParticipants() {
        return taggee_participants;
    }

    public void setTaggeeParticipants(TaggeeParticipants taggee_participants) {
        this.taggee_participants = taggee_participants;
    }

    public ArrayList<CaptureComment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<CaptureComment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "CaptureDetails{" +
                "short_share_url='" + short_share_url + '\'' +
                ", tweet='" + tweet + '\'' +
                ", ratings=" + ratings +
                ", photo=" + photo +
                ", base_wine=" + base_wine +
                ", wine_profile=" + wine_profile +
                ", transcription_error_message='" + transcription_error_message + '\'' +
                ", location_name='" + location_name + '\'' +
                ", liking_participants=" + liking_participants +
                ", commenting_participants=" + commenting_participants +
                ", taggee_participants=" + taggee_participants +
                ", comments=" + comments +
                '}';
    }

    public static class TaggeeParticipants {

        ArrayList<Account> registered;

        ArrayList<TaggeeContact> facebook;

        ArrayList<TaggeeContact> contact;

        @Override
        public String toString() {
            return "TaggeeParticipants{" +
                    "registered=" + registered +
                    ", facebook=" + facebook +
                    ", contact=" + contact +
                    '}';
        }
    }
}
