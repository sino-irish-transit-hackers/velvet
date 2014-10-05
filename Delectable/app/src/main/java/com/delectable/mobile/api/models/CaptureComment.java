package com.delectable.mobile.api.models;

public class CaptureComment {

    String id;

    double created_at;

    String comment;

    String account_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(double created_at) {
        this.created_at = created_at;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAccountId() {
        return account_id;
    }

    public void setAccountId(String account_id) {
        this.account_id = account_id;
    }

    @Override
    public String toString() {
        return "CaptureComment{" +
                "id='" + id + '\'' +
                ", created_at=" + created_at +
                ", comment='" + comment + '\'' +
                ", account_id='" + account_id + '\'' +
                '}';
    }
}
