package ssu.cheesecake.blueberry;

import android.util.Log;

import com.google.android.gms.common.util.Strings;

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
    Vector<CustomGroup> groups;

    //RealmController의 생성자
    public RealmController(Realm mRealm, WhichResult whichResult) {
        this.cards = new Vector<BusinessCard>();
        this.mRealm = mRealm;
        this.whichResult = whichResult;
        this.group = null;
        this.searchWord = null;
        this.groups = new Vector<CustomGroup>();
        loadCard();
        loadGroup();
    }

    public RealmController() {
    }

    //Search, Group의 결과 List 관리하는 RealmController의 생성자
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
        if (whichResult != Search) {
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
        } else {
            searchBusinessCard();
        }
    }

    //RealmController 생성 시 Realm에 등록되어 있는 GroupList를 Vector로 받아옴
    public void loadGroup() {
        RealmResults<CustomGroup> result = null;
        result = mRealm.where(CustomGroup.class).findAll();
        if (result != null) {
            int len = result.size();
            for (int i = 0; i < len; i++) {
                groups.add(result.get(i));
            }
        }
    }

    //Realm에 CustomGroup 추가하는 함수
    public boolean addCustomGroup(final String groupName) {
        int len = groups.size();
        for (int i = 0; i < len; i++)
            if (((groups.get(i)).getGroupName()).compareTo(groupName) == 0) {
                return false;
            }
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CustomGroup group = new CustomGroup(groupName);
                CustomGroup addGroup = realm.createObject(CustomGroup.class);
                addGroup.Copy(group);
                groups.add(addGroup);
            }
        });
        return true;
    }

    //Realm에 CustomGroup 수정하는 함수
    public boolean editCustomGroup(final String srcGroupName, final String changedGroupName) {
        int len = groups.size();
        for (int i = 0; i < len; i++)
            if (((groups.get(i)).getGroupName()).compareTo(changedGroupName) == 0) {
                return false;
            }
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CustomGroup group = new CustomGroup(srcGroupName);
                CustomGroup editGroup = realm.where(CustomGroup.class).equalTo("groupName", srcGroupName).findFirst();
                editGroup.setGroupName(changedGroupName);
            }
        });
        return true;
    }

    //Realm에 CustomGroup 삭제하는 함수
    public void deleteCustomGroup(final String groupName) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CustomGroup group = new CustomGroup(groupName);
                CustomGroup deleteGroup = realm.where(CustomGroup.class).equalTo("groupName", groupName).findFirst();
                groups.remove(deleteGroup);
                deleteGroup.deleteFromRealm();
            }
        });
    }

    //Realm에 BusinessCard 추가하는 함수
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

    public void searchBusinessCard() {
        cards = new Vector<BusinessCard>();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<BusinessCard> results = mRealm.where(BusinessCard.class).findAll();
                int len = results.size();
                for (int i = 0; i < len; i++) {
                    if (results.get(i).toString().contains(searchWord))
                        cards.add(results.get(i));
                }
            }
        });
    }
}
