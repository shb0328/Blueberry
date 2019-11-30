package ssu.cheesecake.blueberry.custom;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Vector;

enum WordClass{
    empty, krName, enName, email, number, site, company, address, position, none, trash
}

public class Word {
    String str;
    WordClass wordClass;

    public Word(){}

    public Word(String str) {
        this.str = str;
        this.wordClass = WordClass.empty;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public WordClass getWordClass() {
        return wordClass;
    }

    public void setWordClass(WordClass wordClass) {
        this.wordClass = wordClass;
    }

    @NonNull
    @Override
    public String toString() {
        String str = new String();
        str += "{" + this.str;
        str += "__";
        str += this.wordClass + "}";
        return str;
    }

    //String Vector를 Word Vector로 변환
    public static Vector<Word> ConvertStringVectorToWordVector(Vector<String> strVector){
        Vector<Word> wordVector = new Vector<Word>();
        int len = strVector.size();
        for(int i = 0; i < len; i++){
            wordVector.add(new Word(strVector.get(i)));
        }
        return wordVector;
    }

    //Word Vector를 로그로 출력
    public static void PrintWordVector(Vector<Vector<Word>> vector){
        for(int i = 0; i < vector.size(); i++)
            Log.d("DEBUG!", vector.get(i).toString());
    }
}


