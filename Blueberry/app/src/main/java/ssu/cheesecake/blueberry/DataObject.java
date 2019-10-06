package ssu.cheesecake.blueberry;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;

import static android.os.Build.ID;

public class DataObject {
    public String id;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String email;
    public String company;
    public String time;

    public DataObject(){
    }

    public DataObject(String id, String firstName, String lastName, String phoneNumber, String email, String company){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.company = company;
        makeTImeString();
    }

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
        result.put("ID", id);
        result.put("LastName", lastName);
        result.put("FirstName", firstName);
        result.put("PhoneNumber", phoneNumber);
        result.put("Email", email);
        result.put("Company", company);
        result.put("Date", time);
        return result;
    }

    public void postFirebaseDatabase(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String path = user.getDisplayName() + "_" + user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mPostReference = database.getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        postValues = this.toMap();
        mPostReference.child("users").child(path).child(time).setValue(postValues);
        //childUpdates.put(ID, postValues);
        //mPostReference.updateChildren(childUpdates);
    }

}

