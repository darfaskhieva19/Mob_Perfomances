package com.example.mobile_perfomances;

import android.os.Parcel;
import android.os.Parcelable;

public class Perfom implements Parcelable {

    private Integer ID;
    private String Title;
    private String Genre;
    private String Producer;
    private String Image;

    public Perfom(Integer Id, String title, String genre, String producer, String image)
    {
        ID = Id;
        Title = title;
        Genre = genre;
        Producer = producer;
        Image = image;
    }

    protected Perfom(Parcel in) {
        ID = in.readInt();
        Title = in.readString();
        Genre = in.readString();
        Producer = in.readString();
        Image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(Title);
        dest.writeString(Genre);
        dest.writeString(Producer);
        dest.writeString(Image);
    }

    public static final Creator<Perfom> CREATOR = new Creator<Perfom>() {
        @Override
        public Perfom createFromParcel(Parcel in) {
            return new Perfom(in);
        }

        @Override
        public Perfom[] newArray(int size) {
            return new Perfom[size];
        }
    };

    public void setID(Integer Id) {
        ID = Id;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public void setProducer(String producer) {
        Producer = producer;
    }

    public void setImage(String image) {
        Image = image;
    }

    public  int getID()
    {
        return  ID;
    }

    public String getTitle() {
        return Title;
    }

    public String getGenre() {
        return Genre;
    }

    public String getProducer() {
        return Producer;
    }

    public String getImage() {
        return Image;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
