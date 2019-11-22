package ssu.cheesecake.blueberry;

import java.util.ArrayList;
import java.util.HashMap;

public class OCRResult {
    private final String originString;

    public OCRResult(String originString){
        this.originString = originString;
    }

    public HashMap<String,String> parsing() {
        HashMap<String,String> map = new HashMap<>();
        //TODO
        //key : name, email, phone ...
        //value : ex) hyebeen, shb0328@gmail.com, 01055074148 ...
        //using split()!
        return map;
    }
    
    private ArrayList<String> split(){
        ArrayList<String> res = new ArrayList<String>();
        return res;
    }

}
