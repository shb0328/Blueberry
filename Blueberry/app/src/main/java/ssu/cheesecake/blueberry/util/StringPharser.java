package ssu.cheesecake.blueberry.util;

import android.util.Log;

import com.google.android.gms.vision.L;
import com.google.firebase.ml.vision.text.FirebaseVisionText;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import ssu.cheesecake.blueberry.custom.Word;


public class StringPharser {
    private static final String TAG = "DEBUG!";
    private static final int SIZE  = 30;
    FirebaseVisionText srcText;
    List<FirebaseVisionText.TextBlock> blocks;
    Vector<FirebaseVisionText.Line> lines;
    String[][] wordArray;
    Vector<Vector<Word>> words;


    public StringPharser() {
        String[][] words1 = {{"CREATOR"},
                {"푸디","보이"},
                {"010","7730","2345"},
                {"foodieboy@kakaopd.com"},
                {"SAN"},{"OX"},
                {"sandboxnetwork.net"}};
        String[][] words2 = {{"SAMSUNG"},
                {"최지훈","Jihun","Choi"},
                {"SAMSUNG"},
                {"Pro/Engineer"},
                {"BIOLOGICS"},
                {"정제","팀","/","정제","1","파트"},
                {"Purification","Team","/","Purificatian","1","Part"},
                {"삼성","바이오","로직스"}};
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

        wordArray = words3;
        ArrayToVector();
        Word.PrintWordVector(words);
    }

    public StringPharser(FirebaseVisionText srcText) {
        this.srcText = srcText;
        blocks = srcText.getTextBlocks();
        lines = new Vector<FirebaseVisionText.Line>();
        //Block 고려하지 않고 Line Vector 생성
        for (FirebaseVisionText.TextBlock block : blocks) {
            if (block.getLines().size() == 1) {
                lines.add(block.getLines().get(0));
            } else {
                for (FirebaseVisionText.Line line : block.getLines()) {
                    lines.add(line);
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

    }

    //wordArray를 words Vector로 변환
    public void ArrayToVector(){
        words = new Vector<>();
        for(int i = 0; i < wordArray.length; i++) {
            words.add(Word.ConvertStringVectorToWordVector(new Vector(Arrays.asList(wordArray[i]))));
        }
    }
}
