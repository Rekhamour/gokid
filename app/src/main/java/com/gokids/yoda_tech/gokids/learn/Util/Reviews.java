package com.gokids.yoda_tech.gokids.learn.Util;

import java.io.Serializable;

/**
 * Created by manoj2prabhakar on 22/04/17.
 */

public class Reviews implements Serializable {
    private String review;
    private String reviewer;
    private String emailID;


    public Reviews(String review, String reviewer, String emailID) {
        this.review = review;
        this.reviewer = reviewer;
        this.emailID = emailID;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }
}
