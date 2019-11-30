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
        this.wordType = WordType.empty;
        this.language = Language.empty;
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

    //String Vector를 Word Vector로 변환
    public static Vector<Word> ConvertStringVectorToWordVector(Vector<String> strVector, String strLanguage) {
        Vector<Word> wordVector = new Vector<Word>();
        int len = strVector.size();
        Language language;
        if(strLanguage.equals("empty"))
            language = Language.empty;
        else if(strLanguage.equals("ko"))
            language = Language.kr;
        else if(strLanguage.equals("en"))
            language = Language.en;
        else language = Language.empty;
        for (int i = 0; i < len; i++) {
            wordVector.add(new Word(strVector.get(i), language));
        }
        return wordVector;
    }

    //Word Vector를 로그로 출력
    public static void PrintWordVector(Vector<Vector<Word>> vector, String TAG) {
        for (int i = 0; i < vector.size(); i++)
            Log.d(TAG, vector.get(i).toString());
    }

}


