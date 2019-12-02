package ssu.cheesecake.blueberry.custom;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Vector;


//문자열과 단어 종류를 갖고 있는 Custom Class
public class Word {

    //단어의 종류를 판별하는 열거형
    //empty: 아직 판단 X
    //none: 판단 불가
    //trash: 쓰레기값
    public enum WordType {
        empty, krName, enName, email, number, site, company, address, position, none, trash
    }

    //단어의 언어를 판별하는 열거형
    //empty: 아직 판단 X
    //sign: 특수 문자
    public enum Language {
        empty, sign, kr, en, number;
    }

    String str;
    WordType wordType;
    Language language;

    public Word() {
    }

    public Word(String str){
        this.str = str;
        if(str == null){
            this.str = "Word class construct ... str is null\n" +
                    "010 1234 5678 \n" +
                    "www.abc.com\n" +
                    "shb0328@gmail.com\n";
        }
        this.wordType = WordType.empty;
        this.DetermineLanguage();
    }

    public Word(String str, WordType wordType){
        this.str = str;
        this.wordType = wordType;
        this.DetermineLanguage();
    }

    public Word(String str, Language language) {
        this.str = str;
        this.wordType = WordType.empty;
        this.language = language;
    }

    public Word(String str, WordType wordType, Language language) {
        this.str = str;
        this.wordType = wordType;
        this.language = language;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public WordType getWordType() {
        return wordType;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public void setWordType(WordType wordType) {
        this.wordType = wordType;
    }

    @NonNull
    @Override
    public String toString() {
        String str = new String();
        str += "{" + this.str;
        str += "__";
        str += this.wordType + "__";
        str += this.language + "}";
        return str;
    }

    //Language 결정
    public void DetermineLanguage() {
        boolean numeric = false;
        boolean alpha = false;
        boolean korean = false;
        boolean sign = false;
        for(int i = 0; i < this.str.length(); i++){
            int index = this.str.charAt(i);
            if(index >= '0' && index <= '9'){
                numeric = true;
            }
            else if((index >= 'A' && index <= 'Z')|| (index>='a' && index <= 'z')){
                alpha = true;
            }
            else if (index < 128) {
                sign = true;
            }
            else {
                korean = true;
            }
        }
        if(korean)
            this.language = Language.kr;
        else if(alpha)
            this.language = Language.en;
        else if(numeric)
            this.language = Language.number;
        else this.language = Language.sign;
        return;
    }


    //String Vector를 Word Vector로 변환
    public static Vector<Word> ConvertStringVectorToWordVector(Vector<String> strVector) {
        Vector<Word> wordVector = new Vector<Word>();
        int len = strVector.size();
        Language language;
        for (int i = 0; i < len; i++) {
            wordVector.add(new Word(strVector.get(i)));
        }
        return wordVector;
    }

    //Word Vector를 로그로 출력
    public static void PrintWordVector(Vector<Vector<Word>> vector, String TAG) {
        for (int i = 0; i < vector.size(); i++)
            Log.d(TAG, vector.get(i).toString());
    }

}


