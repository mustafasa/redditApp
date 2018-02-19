package com.mustafa.arif.reddit.backend.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by musta on 2/18/2018.
 */

public class Children implements Parcelable
{

    private ChildData data;

    private String kind;

    public Children(Parcel in) {
        data = in.readParcelable(ChildData.class.getClassLoader());
        kind = in.readString();
    }

    public static final Creator<Children> CREATOR = new Creator<Children>() {
        @Override
        public Children createFromParcel(Parcel in) {
            return new Children(in);
        }

        @Override
        public Children[] newArray(int size) {
            return new Children[size];
        }
    };

    public Children() {
    }

    public ChildData getData ()
    {
        return data;
    }

    public void setData (ChildData data)
    {
        this.data = data;
    }

    public String getKind ()
    {
        return kind;
    }

    public void setKind (String kind)
    {
        this.kind = kind;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(kind);
        parcel.writeParcelable(data,1);

    }
}

