package com.delectable.mobile.api.models;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public class PhotoHash implements Parcelable {

    public static final Parcelable.Creator<PhotoHash> CREATOR
            = new Parcelable.Creator<PhotoHash>() {
        public PhotoHash createFromParcel(Parcel source) {
            return new PhotoHash(source);
        }

        public PhotoHash[] newArray(int size) {
            return new PhotoHash[size];
        }
    };

    String url;

    ChildResolution child_resolutions;

    public PhotoHash() {
    }

    private PhotoHash(Parcel in) {
        this.url = in.readString();

        this.child_resolutions = new ChildResolution();
        this.child_resolutions.size_nano = in.readString();
        this.child_resolutions.size_micro = in.readString();
        this.child_resolutions.size_thumb = in.readString();
        this.child_resolutions.size_250 = in.readString();
        this.child_resolutions.size_450 = in.readString();
        this.child_resolutions.size_medium = in.readString();
        this.child_resolutions.size_blur = in.readString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ChildResolution getChildResolutions() {
        return child_resolutions;
    }

    public void setChildResolutions(ChildResolution child_resolutions) {
        this.child_resolutions = child_resolutions;
    }

    public String getNanoUrl() {
        return this.child_resolutions.size_nano;
    }

    public String getMicroUrl() {
        return this.child_resolutions.size_micro;
    }

    public String getThumbUrl() {
        return this.child_resolutions.size_thumb;
    }

    public String get250Url() {
        return this.child_resolutions.size_250;
    }

    public String get450Url() {
        return this.child_resolutions.size_450;
    }

    public String getMediumUrl() {
        return this.child_resolutions.size_medium;
    }

    public String getBlurUrl() {
        return this.child_resolutions.size_blur;
    }

    @Override
    public String toString() {
        return "PhotoHash{" +
                "url='" + url + '\'' +
                ", child_resolutions=" + child_resolutions +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.child_resolutions.size_nano);
        dest.writeString(this.child_resolutions.size_micro);
        dest.writeString(this.child_resolutions.size_thumb);
        dest.writeString(this.child_resolutions.size_250);
        dest.writeString(this.child_resolutions.size_450);
        dest.writeString(this.child_resolutions.size_medium);
        dest.writeString(this.child_resolutions.size_blur);
    }

    class ChildResolution {

        @SerializedName("50")
        String size_nano;

        @SerializedName("110")
        String size_micro;

        @SerializedName("150")
        String size_thumb;

        @SerializedName("250")
        String size_250;

        @SerializedName("450")
        String size_450;

        @SerializedName("640")
        String size_medium;

        @SerializedName("1280")
        String size_blur;

        @Override
        public String toString() {
            return "ChildResolution{" +
                    "size_nano='" + size_nano + '\'' +
                    ", size_micro='" + size_micro + '\'' +
                    ", size_thumb='" + size_thumb + '\'' +
                    ", size_250='" + size_250 + '\'' +
                    ", size_450='" + size_450 + '\'' +
                    ", size_medium='" + size_medium + '\'' +
                    ", size_blur='" + size_blur + '\'' +
                    '}';
        }


    }
}
