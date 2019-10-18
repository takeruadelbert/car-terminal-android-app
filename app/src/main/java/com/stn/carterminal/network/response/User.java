package com.stn.carterminal.network.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class User implements Parcelable {
    @SerializedName("id")
    private Long userId;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("biodata")
    private Biodata biodata;
    @SerializedName("userGroup")
    private UserGroup userGroup;

    public User(Long userId, String username, String email, Biodata biodata, UserGroup userGroup) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.biodata = biodata;
        this.userGroup = userGroup;
    }

    protected User(Parcel in) {
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readLong();
        }
        username = in.readString();
        email = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(userId);
        }
        dest.writeString(username);
        dest.writeString(email);
    }
}
