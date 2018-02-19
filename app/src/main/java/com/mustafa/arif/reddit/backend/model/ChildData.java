package com.mustafa.arif.reddit.backend.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by arifm2 on 2/16/2018.
 */

public class ChildData implements Parcelable {
    public ChildData() {
    }

    private Long created_utc;

    private String title;

    private String url;

    private String author;

    private int num_comments;

    private String thumbnail;


    public ChildData(Parcel in) {
        if (in.readByte() == 0) {
            created_utc = null;
        } else {
            created_utc = in.readLong();
        }
        title = in.readString();
        url = in.readString();
        author = in.readString();
        num_comments = in.readInt();
        thumbnail = in.readString();
    }

    public static final Creator<ChildData> CREATOR = new Creator<ChildData>() {
        @Override
        public ChildData createFromParcel(Parcel in) {
            return new ChildData(in);
        }

        @Override
        public ChildData[] newArray(int size) {
            return new ChildData[size];
        }
    };

    public Long getCreated_utc() {
        return created_utc;
    }

    public void setCreated_utc(Long created_utc) {
        this.created_utc = created_utc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getNum_comments() {
        return num_comments;
    }

    public void setNum_comments(int num_comments) {
        this.num_comments = num_comments;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(created_utc);

        parcel.writeString(title);

        parcel.writeString(url);

        parcel.writeString(author);

        parcel.writeInt(num_comments);

        parcel.writeString(thumbnail);
    }
}
