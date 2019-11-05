package ssu.cheesecake.blueberry;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;

public class BusinessCard {
    public String id;
    public String enName;
    public String krName;
    public String phoneNumber;
    public String email;
    public String company;
    public String time;
    public String imageUrl;
    public String path;

    public BusinessCard(){
    }

    public BusinessCard(String enName, String krName, String phoneNumber, String email, String company, String imageUrl){
        this.enName = enName;
        this.krName = krName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.company = company;
        this.imageUrl = imageUrl;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        this.path = user.getDisplayName() + "_" + user.getUid();
        makeTImeString();
    }

    public String getEnName() {return enName;}
    public String getKrName() { return krName;}
    public String getPhoneNumber() { return phoneNumber;}
    public String getEmail(){return email;}
    public String getCompany(){return company;}
    public String getTime(){return time;}
    public String getImageUrl(){return imageUrl;}
    public String getPath(){return path;}

    public void putEnName(String str){this.enName = str;}
    public void putKrName(String str){this.krName = str;}
    public void putPhoneNumber(String str) {this.phoneNumber = str;}
    public void putEmail(String str) {this.email = email;}
    public void putCompany(String str){this.company = company;}

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
            this.time = this.time + "-0" + String.valueOf(month);
        else
            this.time = this.time + "-" + String.valueOf(month);

        if(day < 10)
            this.time = this.time + "-0" + String.valueOf(day);
        else
            this.time = this.time + "-" + String.valueOf(day);

        if(hour < 10)
            this.time = this.time + "-0" + String.valueOf(hour);
        else
            this.time = this.time + "-" + String.valueOf(hour);

        if(minute < 10)
            this.time = this.time + ":0" + String.valueOf(minute);
        else
            this.time = this.time + ":" + String.valueOf(minute);

        if(second < 10)
            this.time = this.time + ":0" + String.valueOf(second);
        else
            this.time = this.time + ":" + String.valueOf(second);

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

    public void postFirebaseDatabase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mPostReference = database.getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        postValues = this.toMap();
        mPostReference.child("users").child(path).push().setValue(postValues);
    }

}

