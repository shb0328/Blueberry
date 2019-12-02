package ssu.cheesecake.blueberry.util;

import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import ssu.cheesecake.blueberry.custom.Word;
import ssu.cheesecake.blueberry.custom.Word.WordType;
import ssu.cheesecake.blueberry.custom.Word.Language;


public class StringParser {
    private static final String TAG = "DEBUG!";
    private static final int SIZE  = 30;

    private FirebaseVisionText srcText;
    private List<FirebaseVisionText.TextBlock> blocks;
    private Vector<FirebaseVisionText.Line> lines;
    private String[][] wordArray;
    public Vector<Vector<Word>> words;

    //FirebaseVisionText 받아와 Vector로 변환하는 생성자
    public StringParser(FirebaseVisionText srcText) {
        this.srcText = srcText;
        blocks = srcText.getTextBlocks();
        lines = new Vector<FirebaseVisionText.Line>();
        //Block 고려하지 않고 Line Vector 생성
        //block 탐색
        for (FirebaseVisionText.TextBlock block : blocks) {
            //block에 line이 1개이면
            if (block.getLines().size() == 1) {
                lines.add(block.getLines().get(0));
                //languageList 받아옴, 그 중 최상위 값만 가져옴
                List <RecognizedLanguage>languageList = block.getLines().get(0).getRecognizedLanguages();
            }
            //block에 line이 여러개이면
            else {
                //line 반복
                for (FirebaseVisionText.Line line : block.getLines()) {
                    lines.add(line);
                    //languageList 받아옴, 그 중 최상위 값만 가져옴
                    List <RecognizedLanguage>languageList = block.getLines().get(0).getRecognizedLanguages();
                }
            }
        }
        //wordArray 2차원 String 배열 생성
        int lineCount = lines.size();
        wordArray = new String[lineCount][SIZE];
        for(int i = 0; i < lineCount; i++) {
            List<FirebaseVisionText.Element> elements = lines.get(i).getElements();
            for(int j = 0; j < elements.size(); j++) {
                wordArray[i][j] = elements.get(j).getText();
            }
        }
        //2차원 String Array를 2차원 String Vector로 변환
        ArrayToVector();
        //WordType 결정
        DetermineWordType();
        //Log 출력
        Word.PrintWordVector(words, "Create StringParser Object");
    }

    //wordsArray(2차원 String Array)를 words(2차원 String Vector)로 변환
    public void ArrayToVector(){
        words = new Vector<>();
        for(int i = 0; i < wordArray.length; i++) {
            words.add(Word.ConvertStringVectorToWordVector(new Vector(Arrays.asList(wordArray[i]))));
        }
    }

    public void DetermineWordType(){
        FindEmail();
        FindNumber();
        FindWebSite();
        FindCompany();
        FindPosition();
        FindAddress();
    }
    
    public void MergeVector(Vector<Word>vector, int from, int to, WordType wordType, Language language){
        MergeVector(vector, from, to, wordType);
        vector.get(from).setLanguage(language);
    }

    //Vector에서 from부터 to까지 from에 병합 후 나머지는 삭제하는 함수
    public void MergeVector(Vector<Word>vector, int from, int to, WordType wordType){
        for(int i = from + 1; i <= to; i++){
            //from의 string에 병합
            vector.get(from).setStr(vector.get(from).getStr() + vector.get(i).getStr());
            //삭제
            vector.remove(i);
        }
        vector.get(from).setWordType(wordType);
    }

    public void FindNumber(){
        //Line 탐색
        for(int i = 0; i < words.size(); i++){
            //Word 탐색
            for(int j = 0; j < words.get(i).size(); j++){
                //Word가 아직 판별되지 않은 것들일 때에만
                if(words.get(i).get(j).getWordType() == WordType.empty) {
                    //Language가 number일 때에만
                    if (words.get(i).get(j).getLanguage() == Language.number) {
                        String str = words.get(i).get(j).getStr();
                        boolean firstIsZero = false;
                        //0으로 시작하는 Number일 경우
                        if(str.startsWith("0")){
                            firstIsZero = true;
                        }
                        //+82로 시작하는 Number일 경우
                        else if(str.startsWith("+82")){
                            firstIsZero = true;
                        }
                        if (firstIsZero) {
                            //같은 행에 단어가 뒤에 더 없을 때까지 반복
                            while(j + 1 < words.get(i).size()) {
                                String nextStr = words.get(i).get(j + 1).getStr();
                                //다음 단어의 Type이 아직 판별되지 않았고, 언어가 number일 때
                                if(words.get(i).get(j+1).getWordType() == WordType.empty && words.get(i).get(j+1).getLanguage() == Language.number) {
                                    //다음 단어가 0으로 시작하지 않는 number일 경우 경우
                                    if (!nextStr.startsWith("0")) {
                                        //다음 단어와 병합
                                        MergeVector(words.get(i), j, j + 1, WordType.number);
                                    }
                                }
                                //다음 단어가 특수문자일 경우
                                else if(words.get(i).get(j+1).getWordType() == WordType.empty && words.get(i).get(j+1).getLanguage() == Language.sign){
                                    //다음 단어와 병합
                                    MergeVector(words.get(i), j, j + 1, WordType.number);
                                }
                                else break;
                            }
                        }
                        //길이가 짧은 경우 WordType empty로 설정
                        if(words.get(i).get(j).getStr().length() < 10){
                            words.get(i).get(j).setWordType(WordType.empty);
                        }
                    }
                }
            }
        }
        return;
    }

    public void FindEmail(){
        //Line 탐색
        for(int i = 0; i < words.size(); i++){
            //Word 탐색
            for(int j = 0; j < words.get(i).size(); j++){
                //Word가 아직 판별되지 않은 것들일 때에만
                if(words.get(i).get(j).getWordType() == WordType.empty) {
                    //Language가 En일 때에만
                    if (words.get(i).get(j).getLanguage() == Language.en) {
                        String str = words.get(i).get(j).getStr();
                        boolean hasEmail = false;
                        //Char 탐색
                        for (int k = 0; k < str.length(); k++) {
                            //@ 찾음
                            if (str.charAt(k) == '@') {
                                hasEmail = true;
                                break;
                            }
                        }
                        if (hasEmail) {
                            words.get(i).get(j).setWordType(WordType.email);
                        }
                    }
                }
            }
        }
        return;
    }

    public void FindWebSite(){
        //Line 탐색
        for(int i = 0; i < words.size(); i++){
            //Word 탐색
            for(int j = 0; j < words.get(i).size(); j++){
                //Word가 아직 판별되지 않은 것들일 때에만
                if(words.get(i).get(j).getWordType() == WordType.empty) {
                    //Language가 En일 때에만
                    if (words.get(i).get(j).getLanguage() == Language.en) {
                        String str = words.get(i).get(j).getStr();
                        boolean hasSite = false;
                        //"www", "http"로 Site 주소 판별
                        if(str.contains("www.")){
                            hasSite = true;
                        }
                        if(str.contains("http")){
                            hasSite = true;
                        }
                        //Site 주소일 경우
                        if (hasSite) {
                            //같은 행에 단어가 뒤에 더 있을 경우
                            if(j + 1 < words.get(i).size()) {
                                String nextStr = words.get(i).get(j + 1).getStr();
                                //다음 단어의 Type이 아직 판별되지 않았고, 언어가 en일 때
                                if(words.get(i).get(j+1).getWordType() == WordType.empty || words.get(i).get(j+1).getLanguage() == Language.en) {
                                    //다음 단어가 .으로 시작할 경우
                                    if (nextStr.startsWith(".")) {
                                        //다음 단어와 병합
                                        MergeVector(words.get(i), j, j + 1, WordType.email);
                                    }
                                    else {
                                        words.get(i).get(j).setWordType(WordType.site);
                                    }
                                }
                            }
                            else{
                                words.get(i).get(j).setWordType(WordType.site);
                            }
                        }
                    }
                }
            }
        }
        return;
    }

    //TODO:words들을 탐색하면서 회사명이라 생각되는 것들을 찾음
    public void FindCompany(){
        return;
    }

    //TODO:words들을 탐색하면서 직책이라 생각되는 것들을 찾음
    public void FindPosition(){
        return;
    }

    //TODO:words들을 탐색하면서 주소라 생각되는 것들을 찾음
    public void FindAddress(){
        return;
    }

    public ArrayList<String> GetNumberArray(){
        ArrayList<String> result = new ArrayList<String>();
        for(int i = 0; i < words.size(); i++){
            for(int j = 0; j < words.get(i).size(); j++){
                if(words.get(i).get(j).getWordType() == WordType.number){
                    result.add(words.get(i).get(j).getStr());
                }
            }
        }
        return result;
    }

    public ArrayList<String> GetEmailArray(){
        ArrayList<String> result = new ArrayList<String>();
        for(int i = 0; i < words.size(); i++){
            for(int j = 0; j < words.get(i).size(); j++){
                if(words.get(i).get(j).getWordType() == WordType.email){
                    result.add(words.get(i).get(j).getStr());
                }
            }
        }
        return result;
    }

    public ArrayList<String> GetWebSiteArray()
    {
        ArrayList<String> result = new ArrayList<String>();
        for(int i = 0; i < words.size(); i++){
            for(int j = 0; j < words.get(i).size(); j++){
                if(words.get(i).get(j).getWordType() == WordType.site){
                    result.add(words.get(i).get(j).getStr());
                }
            }
        }
        return result;
    }

    public ArrayList<String> GetCompanyArray(){
        ArrayList<String> result = new ArrayList<String>();
        for(int i = 0; i < words.size(); i++){
            for(int j = 0; j < words.get(i).size(); j++){
                if(words.get(i).get(j).getWordType() == WordType.company){
                    result.add(words.get(i).get(j).getStr());
                }
            }
        }
        return result;
    }

    public ArrayList<String> GetAddressArray(){
        ArrayList<String> result = new ArrayList<String>();
        result.add("address sample");
        return result;
    }

}
