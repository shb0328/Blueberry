package ssu.cheesecake.blueberry.camera;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;

public class OCRResult {
    private final String srcString;
    private ArrayList<String> words;

    public OCRResult(String srcString) {
        this.srcString = srcString;
        split();
        filtering();
    }

    private synchronized void filtering() {

//**ConcurrentModificationException**
//            for (String word : words) {
//                if (isImpossible(word)) {
//                    Log.i("OCR Result", "hahaha is Forky!\n" + word);
//                    words.remove(word);
//                }
//            }

        for (int i = 0; i < words.size(); ++i){
            if (isImpossible(words.get(i))) {
                Log.i("OCR Result", "hahaha is Forky!\n" + words.get(i));
                words.remove(words.get(i));
            }
        }

    }

    private boolean isImpossible(String str) {
        str = str.toLowerCase();
        if (
                0 == str.compareTo("email")
                        || 0 == str.compareTo("name")
                        || 0 == str.compareTo("phone")
                        || 0 == str.compareTo("company")) {
            return true;
        } else if (str.length() <= 1) {
            return true;
        }
        return false;


    }

    private void split() {
        String[] tmp = srcString.split("\\s");
        words = new ArrayList<>(Arrays.asList(tmp));
    }

    public synchronized String parseEmail() {
        String res = "";
        for (int i = 0; i < words.size(); ++i) {
            if (isEmail(words.get(i))) {
                res += words.get(i);
                words.remove(i);
            }
        }
        return res;
    }

    private boolean isEmail(String str) {
        for (int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) == '@') {
                return true;
            }
        }
        return false;
    }

    public String parsePhone() {
        ArrayList<String> candidate = findPhoneCandidate();
        for (int i = 0; i < candidate.size(); ++i) {
            String res = "";
            for (int j = i; j < candidate.size(); ++j) {
                res += candidate.get(j);
                if (isPossiblePhoneLength(res.length())
                        && isPossiblePhoneStartNumber(res.charAt(0) - '0')) {
                    return res;
                } else if (res.length() > 11) {
                    break;
                }
            }
        }
        return "01012345678";
    }

    private boolean isPossiblePhoneStartNumber(int n) {
        return n == 0 || n == 1;
    }

    private boolean isPossiblePhoneLength(int length) {
        return 9 <= length && length <= 11;
    }

    private ArrayList<String> findPhoneCandidate() {
        ArrayList<String> candidate = new ArrayList<>();
        for (String word : words) {
            if (hasNumber(word)) {
                candidate.add(removeNotNumber(word));
            }
        }
        return candidate;
    }

    private String removeNotNumber(String str) {
        String res = "";
        for (int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {
                res += str.charAt(i);
            }
        }
        return res;
    }

    private boolean hasNumber(String str) {
        for (int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {
                return true;
            }
        }
        return false;
    }

}
