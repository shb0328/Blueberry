package ssu.cheesecake.blueberry.util;

import android.util.Log;

import com.google.firebase.database.core.operation.Merge;
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
    private static final int SIZE = 30;

    private FirebaseVisionText srcText;
    private List<FirebaseVisionText.TextBlock> blocks;
    private Vector<FirebaseVisionText.Line> lines;
    private String[][] wordArray;
    public Vector<Vector<Word>> words;

    //Debug 위한 Default 생성자
    //Debug 위해 LoginActivity에서 호출
    public StringParser() {
        String[][] words1 = {{"CREATOR"},
                {"푸디", "보이"},
                {"010", "7730", "2345"},
                {"foodieboy@kakaopd.com"},
                {"SAN", "OX"},
                {"sandboxnetwork.net"}};
        String[][] words2 = {{"SAMSUNG"},
                {"최지훈", "Jihun", "Choi"},
                {"SAMSUNG"},
                {"Pro/Engineer"},
                {"BIOLOGICS"},
                {"정제", "팀", "/", "정제", "1", "파트"},
                {"삼성", "바이오", "로직스"},
                {"Purification", "Team", "/", "Purificatian", "1", "Part"}};
        String[][] words3 = {{"ictaulic"},
                {"Metro", "Team"},
                {"명", "섭"},
                {"부장"},
                {"한국", "물류", "센터"},
                {"경기도", "용인시", "기흥구", "어정", "로", "117", "(", "상하동", ")", "(", "16986", ")"},
                {"Tel", "031)", "281-3844", "Fax", "031)", "281-4844"},
                {"서울", "사무소"},
                {"서울", "서초구", "방배", "로", "18", "길", "5", "BH", "빌딩", "1", "층", "(", "방배동", ")", "(", "06664", ")"},
                {"Tel", "02)", "521-7235", "Fax", "02)", "3476-5743"},
                {"mobile", ":", "010-2366-9887", "e-mail", ":", "Myoung-Sup.Oh@victaulic.com"},
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
                List<RecognizedLanguage> languageList = block.getLines().get(0).getRecognizedLanguages();
            }
            //block에 line이 여러개이면
            else {
                //line 반복
                for (FirebaseVisionText.Line line : block.getLines()) {
                    lines.add(line);
                    //languageList 받아옴, 그 중 최상위 값만 가져옴
                    List<RecognizedLanguage> languageList = block.getLines().get(0).getRecognizedLanguages();
                }
            }
        }
        //wordArray 2차원 String 배열 생성
        int lineCount = lines.size();
        wordArray = new String[lineCount][SIZE];
        for (int i = 0; i < lineCount; i++) {
            List<FirebaseVisionText.Element> elements = lines.get(i).getElements();
            for (int j = 0; j < elements.size(); j++) {
                wordArray[i][j] = elements.get(j).getText();
            }
        }
        //2차원 String Array를 2차원 String Vector로 변환
        ArrayToVector();
        //2차원 String Array를 2차원 String Vector로 변환
        //WordType 결정
        DetermineWordType();
        //Log 출력
        Word.PrintWordVector(words, TAG);
    }

    //wordsArray(2차원 String Array)를 words(2차원 String Vector)로 변환
    public void ArrayToVector() {
        words = new Vector<>();
        for (int i = 0; i < wordArray.length; i++) {
            words.add(Word.ConvertStringVectorToWordVector(new Vector(Arrays.asList(wordArray[i]))));
        }
    }

    public void DetermineWordType() {
        FindEmail();
        FindWebSite();
        FindNumber();
        FindOthers();
    }

    public void MergeVector(Vector<Word> vector, int from, int to, WordType wordType, Language language) {
        MergeVector(vector, from, to, wordType);
        vector.get(from).setLanguage(language);
    }

    public void MergeVector(Vector<Word> vector, int from, int to, WordType wordType) {
        MergeVector(vector, from, to, wordType, "");
    }

    //Vector에서 from부터 to까지 from에 병합 후 나머지는 삭제하는 함수
    public void MergeVector(Vector<Word> vector, int from, int to, WordType wordType, String str) {
        for (int i = from + 1; i <= to; i++) {
            //from의 string에 병합
            vector.get(from).setStr(vector.get(from).getStr() + str + vector.get(i).getStr());
            vector.get(from).DetermineLanguage();
            //삭제
            vector.remove(i);
        }
        vector.get(from).setWordType(wordType);
    }

    //====================For HyeBin====================
    //순서대로 구현하는 것이 가장 정확도 높을 듯!!!!!
    public void FindEmail() {
        //Line 탐색
        for (int i = 0; i < words.size(); i++) {
            //Word 탐색
            for (int j = 0; j < words.get(i).size(); j++) {
                //Word가 아직 판별되지 않은 것들일 때에만
                if (words.get(i).get(j).getWordType() == WordType.empty) {
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

    public void FindWebSite() {
        //Line 탐색
        for (int i = 0; i < words.size(); i++) {
            //Word 탐색
            for (int j = 0; j < words.get(i).size(); j++) {
                //Word가 아직 판별되지 않은 것들일 때에만
                if (words.get(i).get(j).getWordType() == WordType.empty) {
                    //Language가 En일 때에만
                    if (words.get(i).get(j).getLanguage() == Language.en) {
                        String str = words.get(i).get(j).getStr();
                        //www나 http 갖고 있는 경우
                        boolean hasPreKeyword = false;

                        boolean hasPostKeyword = false;
                        //"www", "http"로 Site 주소 판별
                        if (str.contains("www.")) {
                            hasPreKeyword = true;
                        }
                        if (str.contains("http")) {
                            hasPreKeyword = true;
                        }
                        if (str.contains(".com")) {
                            hasPostKeyword = true;
                        }
                        if (str.contains(".co.kr")) {
                            hasPostKeyword = true;
                        }
                        if (str.contains(".net")) {
                            hasPostKeyword = true;
                        }
                        //http나 www를 포함한 경우
                        if (hasPreKeyword) {
                            //같은 행에 단어가 뒤에 더 있을 경우
                            if (j + 1 < words.get(i).size()) {
                                String nextStr = words.get(i).get(j + 1).getStr();
                                //다음 단어의 Type이 아직 판별되지 않았고, 언어가 en일 때
                                if (words.get(i).get(j + 1).getWordType() == WordType.empty || words.get(i).get(j + 1).getLanguage() == Language.en) {
                                    //다음 단어가 .으로 시작할 경우
                                    if (nextStr.startsWith(".")) {
                                        //다음 단어와 병합
                                        MergeVector(words.get(i), j, j + 1, WordType.email);
                                    } else {
                                        words.get(i).get(j).setWordType(WordType.site);
                                    }
                                }
                            } else {
                                words.get(i).get(j).setWordType(WordType.site);
                            }
                        }
                        //.com .co.kr .net를 포함한 경우
                        else if (hasPostKeyword) {
                            //Line의 첫번째 단어가 아닌 경우
                            if (j != 0) {
                                //Line의 앞 단어 반복
                                for (int k = j; k > 0; k--) {
                                    //앞 단어가 아직 판별되지 않았고, 언어가 en일 때
                                    if (words.get(i).get(k).getWordType() == WordType.empty && words.get(i).get(k).getLanguage() == Language.en) {
                                        //현재 단어가 .으로 시작한다면
                                        if (words.get(i).get(k).getStr().startsWith(".")) {
                                            //앞단어와 Merge 후 WordType Site로 변경
                                            MergeVector(words.get(i), k - 1, k, WordType.site);
                                            k--;
                                        }
                                    }
                                }
                            }
                            //Line의 첫번째 단어인 경우
                            else {
                                //현재 단어가 아직 판별되지 않았고, 언어가 en일 때
                                if (words.get(i).get(j).getWordType() == WordType.empty && words.get(i).get(j).getLanguage() == Language.en) {
                                    //WordType Site로 변경
                                    words.get(i).get(j).setWordType(WordType.site);
                                }
                            }
                        }
                    }
                }
            }
        }
        return;
    }

    public void FindNumber() {
        //Line 탐색
        for (int i = 0; i < words.size(); i++) {
            //Word 탐색
            for (int j = 0; j < words.get(i).size(); j++) {
                //Word가 아직 판별되지 않은 것들일 때에만
                if (words.get(i).get(j).getWordType() == WordType.empty) {
                    //Language가 number일 때에만
                    if (words.get(i).get(j).getLanguage() == Language.number) {
                        String str = words.get(i).get(j).getStr();
                        boolean firstIsZero = false;
                        //0으로 시작하는 Number일 경우
                        if (str.startsWith("0")) {
                            firstIsZero = true;
                        }
                        //+82로 시작하는 Number일 경우
                        else if (str.startsWith("+82")) {
                            firstIsZero = true;
                        }
                        if (firstIsZero) {
                            //Line의 마지막 단어일 경우
                            if(j + 1 == words.get(i).size()) {
                                words.get(i).get(j).setWordType(WordType.number);
                            }
                            //Line의 마지막 단어가 아닐 경우
                            else {
                                //같은 행에 단어가 뒤에 더 없을 때까지 반복
                                while (j + 1 < words.get(i).size()) {
                                    String nextStr = words.get(i).get(j + 1).getStr();
                                    //다음 단어의 Type이 아직 판별되지 않았고, 언어가 number일 때
                                    if (words.get(i).get(j + 1).getWordType() == WordType.empty && words.get(i).get(j + 1).getLanguage() == Language.number) {
                                        //다음 단어가 0으로 시작하지 않는 number일 경우 경우
                                        if (!nextStr.startsWith("0")) {
                                            //다음 단어와 병합
                                            MergeVector(words.get(i), j, j + 1, WordType.number);
                                        }
                                    }
                                    //다음 단어가 특수문자일 경우
                                    else if (words.get(i).get(j + 1).getWordType() == WordType.empty && words.get(i).get(j + 1).getLanguage() == Language.sign) {
                                        //다음 단어와 병합
                                        MergeVector(words.get(i), j, j + 1, WordType.number);
                                    } else {
                                        words.get(i).get(j).setWordType(WordType.number);
                                        break;
                                    }
                                }
                            }
                        }
                        //길이가 짧은 경우 WordType empty로 설정
                        if (words.get(i).get(j).getStr().length() < 10) {
                            words.get(i).get(j).setWordType(WordType.empty);
                        }
                    }
                }
            }
        }
        return;
    }

    //나머지 Word들을 모두 WordType Others로 변경 및 병합하는 함수
    public void FindOthers() {
        for (int i = 0; i < words.size(); i++) {
            //Line의 Word가 1개인 경우
            if (words.get(i).size() == 1) {
                //Word가 아직 판별되지 않았을 경우
                if(words.get(i).get(0).getWordType() == WordType.empty)
                    //WordType Others로 변경
                    words.get(i).get(0).setWordType(WordType.others);
            }
            //Line의 Word가 여러 개인 경우
            else {
                //Line의 Word 갯수 - 1까지 반복
                for (int j = 0; j < words.get(i).size() - 1; j++) {
                    //현재 Word가 아직 판별되지 않았을 경우
                    if (words.get(i).get(j).getWordType() == WordType.empty) {
                        int k = j;
                        //다음 Word의 WordType이 Empty나 Others가 아닐 때까지 반복
                        while (k < words.get(i).size() - 1 && (words.get(i).get(k).getWordType() == WordType.empty || words.get(i).get(k).getWordType() == WordType.others)) {
                            //다음 Word의 WordType이 Empty나 Others이면
                            if (words.get(i).get(k + 1).getWordType() == WordType.empty) {
                                //현재 Word와 다음 Word를 Merge, (WordType Others, 두 Word 사이 공백 삽입)
                                MergeVector(words.get(i), k, k + 1, WordType.others, " ");
                            }
                            //다음 Word의 WordType이 Empty나 Others가 아니면
                            else {
                                //현재 Word의 WordType을 Other로 변경
                                words.get(i).get(k).setWordType(WordType.others);
                                //다음 Word로 넘어감
                                k++;
                            }
                        }
                    }
                }
            }
        }
        return;
    }

    public ArrayList<String> GetEmailArray() {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < words.size(); i++) {
            for (int j = 0; j < words.get(i).size(); j++) {
                if (words.get(i).get(j).getWordType() == WordType.email) {
                    result.add(words.get(i).get(j).getStr());
                }
            }
        }
        return result;
    }

    public ArrayList<String> GetNumberArray() {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < words.size(); i++) {
            for (int j = 0; j < words.get(i).size(); j++) {
                if (words.get(i).get(j).getWordType() == WordType.number) {
                    result.add(words.get(i).get(j).getStr());
                }
            }
        }
        return result;
    }

    public ArrayList<String> GetOthersArray() {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < words.size(); i++) {
            for (int j = 0; j < words.get(i).size(); j++) {
                if (words.get(i).get(j).getWordType() == WordType.others) {
                    result.add(words.get(i).get(j).getStr());
                }
            }
        }
        return result;
    }

    public void swapString(ArrayList<String> list, int n, int m){
        String tmp = list.get(n);
        list.set(n, list.get(m));
        list.set(m, tmp);
        return;
    }

    public ArrayList<String> GetCompanyArray(){
        ArrayList<String> result = GetOthersArray();
        for(int i = 0; i < result.size(); i++){
            for(int j = 0; j < result.size() - 1 - i; j++){
                if(result.get(j).length() > result.get(j+1).length()){
                    swapString(result, j, j + 1);
                }
            }
        }
        return result;
    }

    public ArrayList<String> GetAdressArray() {
        ArrayList<String> result = GetOthersArray();
        for(int i = 0; i < result.size(); i++){
            for(int j = 0; j < result.size() - 1 - i; j++){
                if(result.get(j).length() < result.get(j+1).length()){
                    swapString(result, j, j + 1);
                }
            }
        }
        return result;
    }

}
