package ssu.cheesecake.blueberry;

import android.util.Log;

import java.util.HashMap;

public class OCRResult {
    private final String originString;
    private String[] words;

    public OCRResult(String originString){
        this.originString = originString;
    }

    public HashMap<String,String> parsing() {
        HashMap<String,String> map = new HashMap<>();
        //TODO
        //key : name, email, phone ...
        //value : ex) hyebeen, shb0328@gmail.com, 01055074148 ...
        //using split()!
        split();

        for(int i = 0; i<words.length; ++i){
            if(isForky(words[i])){
                Log.i("OCR Result","hahaha is Forky!\n"+words[i]);
            }
            else if(isEmail(words[i])){
                map.put("email",words[i]);
            }else if(isPhone(words[i])){
                map.put("phone",words[i]);
            }else if(isName(words[i])){
                map.put("name",words[i]);
            }
        }
        return map;
    }
    
    private void split()
    {
        words = originString.split("\\s");
    }
    private boolean isForky(String str)
    {
        str = str.toLowerCase();
        if(
            0 == str.compareTo("email")
            || 0 == str.compareTo("name")
            || 0 == str.compareTo("phone")
            || 0 == str.compareTo("company"))
        {
            return true;
        }
        else if(str.length() <= 1)
        {
            return true;
        }
        return false;


    }

    private boolean isEmail(String str)
    {
        for(int i = 0; i<str.length();++i){
            if(str.charAt(i) == '@'){
                return true;
            }
        }
        return false;
    }

    private boolean isPhone(String str)
    {
        for(int i = 0; i<str.length();++i){
            if(str.charAt(i) >= '0' && str.charAt(i) <= '9'){
                return true;
            }
        }
        return false;
    }

    private boolean isName(String str)
    {
        return true;
    }

}
