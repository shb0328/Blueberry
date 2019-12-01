package ssu.cheesecake.blueberry.util;

import android.util.Log;

import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import ssu.cheesecake.blueberry.custom.Word;
import ssu.cheesecake.blueberry.custom.Word.WordType;


public class StringPharser {
    private static final String TAG = "DEBUG!";
    private static final int SIZE  = 30;
    FirebaseVisionText srcText;
    List<FirebaseVisionText.TextBlock> blocks;
    Vector<FirebaseVisionText.Line> lines;
    String[][] wordArray;
    Vector<Vector<Word>> words;

    //Debug 위한 Default 생성자
    //Debug 위해 LoginActivity에서 호출
    public StringPharser() {
        String[][] words1 = {{"CREATOR"},
                {"푸디","보이"},
                {"010","7730","2345"},
                {"foodieboy@kakaopd.com"},
                {"SAN","OX"},
                {"sandboxnetwork.net"}};
        String[][] words2 = {{"SAMSUNG"},
                {"최지훈","Jihun","Choi"},
                {"SAMSUNG"},
                {"Pro/Engineer"},
                {"BIOLOGICS"},
                {"정제","팀","/","정제","1","파트"},
                {"삼성","바이오","로직스"},
                {"Purification","Team","/","Purificatian","1","Part"}};
        String[][] words3 = {{"ictaulic"},
                {"Metro","Team"},
                {"명","섭"},
                {"부장"},
                {"한국","물류","센터"},
                {"경기도","용인시","기흥구","어정","로","117","(","상하동",")","(","16986",")"},
                {"Tel","031)","281-3844","Fax","031)","281-4844"},
                {"서울","사무소"},
                {"서울","서초구","방배","로","18","길","5","BH","빌딩","1","층","(","방배동",")","(","06664",")"},
                {"Tel","02)","521-7235","Fax","02)","3476-5743"},
                {"mobile",":","010-2366-9887","e-mail",":","Myoung-Sup.Oh@victaulic.com"},
                {"www.victaulic.com"},
                {"어"}};

        //====================For HyeBin====================
        //원하는 words 예제 골라 넣기
        wordArray = words3;
        //2차원 String Array를 2차원 String Vector로 변환
        ArrayToVector();
        //WordType 결정
        DetermineWordType();
        //Log 출력
        Word.PrintWordVector(words, TAG);
    }

    //FirebaseVisionText 받아와 Vector로 변환하는 생성자
    public StringPharser(FirebaseVisionText srcText) {
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
        Word.PrintWordVector(words, TAG);
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
        FindSite();
        FindNumber();
        FindKrName();
        FindEnName();
        FindCompany();
        FindPosition();
        FindAddress();
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

    //====================For HyeBin====================
    //순서대로 구현하는 것이 가장 정확도 높을 듯!!!!!
    //words들을 탐색하면서 Email이라 생각되는 것들을 찾음
    public void FindEmail(){
        //Line 탐색
        for(int i = 0; i < words.size(); i++){
            //Word 탐색
            for(int j = 0; j < words.get(i).size(); j++){
                //Word가 아직 판별되지 않은 것들일 때에만
                if(words.get(i).get(j).getWordType() == WordType.empty) {
                    //Language가 En일 때에만
                    if (words.get(i).get(j).getLanguage() == Word.Language.en) {
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

    //words들을 탐색하면서 웹사이트 주소라 생각되는 것들을 찾음
    public void FindSite(){
        //Line 탐색
        for(int i = 0; i < words.size(); i++){
            //Word 탐색
            for(int j = 0; j < words.get(i).size(); j++){
                //Word가 아직 판별되지 않은 것들일 때에만
                if(words.get(i).get(j).getWordType() == WordType.empty) {
                    //Language가 En일 때에만
                    if (words.get(i).get(j).getLanguage() == Word.Language.en) {
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
                                if(words.get(i).get(j+1).getWordType() == WordType.empty || words.get(i).get(j+1).getLanguage() == Word.Language.en) {
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

    //words들을 탐색하면서 전화번호라 생각되는 것들을 찾음
    public void FindNumber(){
        return;
    }

    //words들을 탐색하면서 국문이름이라 생각되는 것들을 찾음
    public void FindKrName(){
        return;
    }

    //words들을 탐색하면서 영어이름이라 생각되는 것들을 찾음
    public void FindEnName(){
        return;
    }

    //words들을 탐색하면서 회사명이라 생각되는 것들을 찾음
    public void FindCompany(){
        return;
    }

    //words들을 탐색하면서 직책이라 생각되는 것들을 찾음
    public void FindPosition(){
        return;
    }

    //words들을 탐색하면서 주소라 생각되는 것들을 찾음
    public void FindAddress(){
        return;
    }

}
