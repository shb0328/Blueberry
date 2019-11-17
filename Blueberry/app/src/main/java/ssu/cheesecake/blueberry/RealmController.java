package ssu.cheesecake.blueberry;

import android.util.Log;

import java.util.Vector;

import io.realm.Realm;
import io.realm.RealmResults;

import static ssu.cheesecake.blueberry.RealmController.WhichResult.*;

public class RealmController {
    public enum WhichResult {
        List, Search, Favorite, Group;
    }

    Realm mRealm;
    Vector<BusinessCard> cards;
    WhichResult whichResult;
    String group;
    String searchWord;

    //RealmController의 생성자
    public RealmController(Realm mRealm, WhichResult whichResult) {
        this.cards = new Vector<BusinessCard>();
        this.mRealm = mRealm;
        this.whichResult = whichResult;
        this.group = null;
        this.searchWord = null;
        loadCard();
    }

    public RealmController() {
    }

    //Search, Group 관리하는 RealmController의 생성자
    public RealmController(Realm mRealm, WhichResult whichResult, String str) {
        if (whichResult == Group) {
            this.cards = new Vector<BusinessCard>();
            this.mRealm = mRealm;
            this.whichResult = Group;
            this.group = str;
            loadCard();
        } else if (whichResult == Search) {
            this.cards = new Vector<BusinessCard>();
            this.mRealm = mRealm;
            this.whichResult = Search;
            this.searchWord = str;
            loadCard();
        }
    }

    //RealmController 생성 시 Realm에 등록되어 있는 BusinessCard를 Vector로 받아옴
    public void loadCard() {
        RealmResults<BusinessCard> result = null;
        if(whichResult != Search) {
            switch (this.whichResult) {
                case List:
                    result = mRealm.where(BusinessCard.class).findAll();
                    break;
                case Favorite:
                    result = mRealm.where(BusinessCard.class).equalTo("isFavorite", true).findAll();
                    break;
                case Group:
                    result = mRealm.where(BusinessCard.class).equalTo("group", this.group).findAll();
                    break;
            }
            if (result != null) {
                int len = result.size();
                for (int i = 0; i < len; i++) {
                    cards.add(result.get(i));
                }
            }
        }
        else{
            searchBusinessCard();
        }
    }

    //Realm에 추가하는 함수
    public void addBusinessCard(final BusinessCard card) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                BusinessCard addCard = realm.createObject(BusinessCard.class, card.getTime());
                addCard.Copy(card);
                cards.add(addCard);
            }
        });
    }

    public Vector<BusinessCard> getCards() {
        return cards;
    }

    public void changeKrName(final BusinessCard card, final String krName) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                String id = card.getId();
                BusinessCard editCard = realm.where(BusinessCard.class).equalTo("id", id).findFirst();
                editCard.setKrName(krName);
            }
        });
        return;
    }

    public void changePhoneNumber(final BusinessCard card, final String phoneNumber) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                String id = card.getId();
                BusinessCard editCard = realm.where(BusinessCard.class).equalTo("id", id).findFirst();
                editCard.setPhoneNumber(phoneNumber);
            }
        });
        return;
    }

    public void changeEmail(final BusinessCard card, final String email) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                String id = card.getId();
                BusinessCard editCard = realm.where(BusinessCard.class).equalTo("id", id).findFirst();
                editCard.setEmail(email);
            }
        });
        return;
    }

    public void changeCompany(final BusinessCard card, final String company) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                String id = card.getId();
                BusinessCard editCard = realm.where(BusinessCard.class).equalTo("id", id).findFirst();
                editCard.setCompany(company);
            }
        });
        return;
    }

    public void changeGroup(final BusinessCard card, final String group) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                String id = card.getId();
                BusinessCard editCard = realm.where(BusinessCard.class).equalTo("id", id).findFirst();
                editCard.setGroup(group);
            }
        });
        return;
    }

    public void changeIsFavorite(final BusinessCard card) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                String id = card.getId();
                BusinessCard editCard = realm.where(BusinessCard.class).equalTo("id", id).findFirst();
                editCard.setIsFavorite(!editCard.getIsFavorite());
            }
        });
    }

    public void deleteBusinessCard(final BusinessCard card) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                String id = card.getId();
                BusinessCard deleteCard = realm.where(BusinessCard.class).equalTo("id", id).findFirst();
                cards.remove(deleteCard);
                deleteCard.deleteFromRealm();
            }
        });
    }

    public void searchBusinessCard(){
        cards = new Vector<BusinessCard>();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<BusinessCard> results = mRealm.where(BusinessCard.class).findAll();
                int len = results.size();
                for(int i = 0; i < len; i++){
                    if(results.get(i).toString().contains(searchWord))
                        cards.add(results.get(i));
                }
            }
        });
    }
}
