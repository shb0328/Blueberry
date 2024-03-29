package ssu.cheesecake.blueberry.custom;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;

import io.realm.RealmObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;


public class BusinessCard extends RealmObject implements Parcelable {
    public int id;

    private String name;
    private String phoneNumber;
    private String email;
    private String webSite;
    private String company;
    private String address;
    private String time;
    private String imageUrl;
    private String fileName;
    private String group;
    private boolean isFavorite;

    public BusinessCard() {
    }

    public BusinessCard(String name, String phoneNumber, String email, String company, String imageUrl) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.company = company;
        this.imageUrl = imageUrl;
        this.group = "None";
        this.isFavorite = false;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        this.fileName = user.getDisplayName() + "_" + user.getUid();
        this.time = makeTImeString();
    }

    public BusinessCard(Map<String, Object> map) {
        this.name = (String) map.get("name");
        this.phoneNumber = (String) map.get("phoneNumber");
        this.email = (String) map.get("email");
        this.company = (String) map.get("company");
        this.imageUrl = (String) map.get("imageUrl");
        this.fileName = (String)map.get("fileName");
        this.group = (String) map.get("group");
        this.isFavorite = (boolean) map.get("isFavorite");
    }

    protected BusinessCard(Parcel in) {
        id = in.readInt();
        name = in.readString();
        phoneNumber = in.readString();
        email = in.readString();
        webSite = in.readString();
        company = in.readString();
        address = in.readString();
        time = in.readString();
        imageUrl = in.readString();
        fileName = in.readString();
        group = in.readString();
        int tmp = in.readInt();
        if(tmp == 0) isFavorite = false;
        else isFavorite = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() { return address; }

    public void setAddress(String address) {this.address = address;}

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public static String makeTImeString() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        String timeStr = String.valueOf(year);
        if (month < 10)
            timeStr = timeStr + "-0" + month;
        else
            timeStr = timeStr + "-" + month;

        if (day < 10)
            timeStr = timeStr + "-0" + day;
        else
            timeStr = timeStr + "-" + day;

        if (hour < 10)
            timeStr = timeStr + "-0" + hour;
        else
            timeStr = timeStr + "-" + hour;

        if (minute < 10)
            timeStr = timeStr + ":0" + minute;
        else
            timeStr = timeStr + ":" + minute;

        if (second < 10)
            timeStr = timeStr + ":0" + second;
        else
            timeStr = timeStr + ":" + second;
        return timeStr;
    }

    public void Copy(BusinessCard src) {
        id = src.getId();
        name = src.getName();
        phoneNumber = src.getPhoneNumber();
        email = src.getEmail();
        webSite = src.getWebSite();
        company = src.getCompany();
        address = src.getAddress();
        time = src.getTime();
        imageUrl = src.getImageUrl();
        fileName = src.getFileName();
        group = src.getGroup();
        isFavorite = src.getIsFavorite();
        return;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("phoneNumber", phoneNumber);
        result.put("email", email);
        result.put("webSite",webSite);
        result.put("company", company);
        result.put("address",address);
        result.put("time", time);
        result.put("imageUrl", imageUrl);
        result.put("fileName", fileName);
        result.put("group", group);
        result.put("isFavorite", isFavorite);
        return result;
    }

    public String toString() {
        String result = new String();
        result += "id: ";
        result += id;
        result += "name: ";
        result += this.name + ',';
        result += "phoneNumber: ";
        result += this.phoneNumber + ',';
        result += "email: ";
        result += this.email + ',';
        result += "webSite: ";
        result += this.webSite + ',';
        result += "company: ";
        result += this.company;
        result += "address: ";
        result += this.address;
        result += "time: ";
        result += this.time;
        result += "imageUrl: ";
        result += this.imageUrl;
        result += "fileName: ";
        result += this.fileName;
        result += "group: ";
        result += this.group;
        result += "isFavorite: ";
        result += this.isFavorite;
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(phoneNumber);
        dest.writeString(email);
        dest.writeString(webSite);
        dest.writeString(company);
        dest.writeString(address);
        dest.writeString(time);
        dest.writeString(imageUrl);
        dest.writeString(fileName);
        dest.writeString(group);
        int tmp = 0;
        if (isFavorite)
            tmp = 1;
        else if (!isFavorite)
            tmp = 0;
        dest.writeInt(tmp);
        return;
    }

    public static final Creator<BusinessCard> CREATOR = new Creator<BusinessCard>(){

        @Override
        public BusinessCard createFromParcel(Parcel source) {
            return new BusinessCard(source);
        }

        @Override
        public BusinessCard[] newArray(int size) {
            return new BusinessCard[0];
        }
    };
}

