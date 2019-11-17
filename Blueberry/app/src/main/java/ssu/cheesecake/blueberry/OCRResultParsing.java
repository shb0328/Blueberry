package ssu.cheesecake.blueberry;

import java.util.ArrayList;
import java.util.Map;

public class OCRResultParsing {
    private final String originString;

    public OCRResultParsing(String originString){
        this.originString = originString;
    }

    public Map parsing() {
        //TODO
        //key : name, email, phone ...
        //value : ex) hyebeen, shb0328@gmail.com, 01055074148 ...
        //using split()!
    }
    
    private ArrayList<String> split(){
        ArrayList<String> res = new ArrayList<String>();
        return res;
    }

}
