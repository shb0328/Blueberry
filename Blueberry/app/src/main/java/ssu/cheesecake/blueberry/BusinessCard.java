package ssu.cheesecake.blueberry;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import io.realm.RealmObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class BusinessCard extends RealmObject {
    @PrimaryKey
    public String id;

    public String enName;
    public String krName;
    public String phoneNumber;
    public String email;
    public String company;
    public String time;
    public String imageUrl;
    public String path;
    public String group;
    public boolean isFavorite;

    public BusinessCard(){
    }

    public BusinessCard(String enName, String krName, String phoneNumber, String email, String company, String imageUrl) {
        this.enName = enName;
        this.krName = krName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.company = company;
        this.imageUrl = imageUrl;
        this.group = "None";
        this.isFavorite = false;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        this.path = user.getDisplayName() + "_" + user.getUid();
        makeTImeString();
    }

    public String getId() {return id;}
    public String getEnName() {return enName;}
    public String getKrName() { return krName;}
    public String getPhoneNumber() { return phoneNumber;}
    public String getEmail(){return email;}
    public String getCompany(){return company;}
    public String getTime(){return time;}
    public String getImageUrl(){return imageUrl;}
    public String getPath(){return path;}
    public String getGroup(){return group;}
    public boolean getIsFavorite(){return isFavorite;}

    public void setEnName(String str){this.enName = str;}
    public void setKrName(String str){this.krName = str;}
    public void setPhoneNumber(String str) {this.phoneNumber = str;}
    public void setEmail(String str) {this.email = email;}
    public void setCompany(String str){this.company = company;}
    public void setGroup(String group){this.group = group;}
    public void setIsFavorite(boolean isFavorite){this.isFavorite = isFavorite;}

    public void makeTImeString(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        this.time = String.valueOf(year);
        if(month < 10)
            this.time = this.time + "-0" + month;
        else
            this.time = this.time + "-" + month;

        if(day < 10)
            this.time = this.time + "-0" + day;
        else
            this.time = this.time + "-" + day;

        if(hour < 10)
            this.time = this.time + "-0" + hour;
        else
            this.time = this.time + "-" + hour;

        if(minute < 10)
            this.time = this.time + ":0" + minute;
        else
            this.time = this.time + ":" + minute;

        if(second < 10)
            this.time = this.time + ":0" + second;
        else
            this.time = this.time + ":" + second;

    }

    public void Copy(BusinessCard src){
        enName = src.getEnName();
        krName = src.getKrName();
        phoneNumber = src.getPhoneNumber();
        email = src.getEmail();
        company = src.getCompany();
        time = src.getTime();
        imageUrl = src.getImageUrl();
        path = src.getPath();
        group = src.getGroup();
        isFavorite = src.getIsFavorite();
        return;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("enName", enName);
        result.put("krName", krName);
        result.put("phoneNumber", phoneNumber);
        result.put("email", email);
        result.put("company", company);
        result.put("time", time);
        result.put("imageUrl", imageUrl);
        return result;
    }

    public String toString(){
        String result = new String();
        result += this.enName + '\t';
        result += this.krName + '\t';
        result += this.phoneNumber + '\t';
        result += this.email + '\t';
        result += this.company;
        return result;
    }

}

