package ssu.cheesecake.blueberry.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Vector;

import io.realm.Realm;
import io.realm.RealmResults;
import ssu.cheesecake.blueberry.custom.AutoSave;
import ssu.cheesecake.blueberry.custom.BusinessCard;
import ssu.cheesecake.blueberry.custom.CustomGroup;

import static ssu.cheesecake.blueberry.util.RealmController.WhichResult.*;

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
        loadCard();
        loadGroup();
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

    public Realm getmRealm() {
        return mRealm;
    }

    public void setmRealm(Realm mRealm) {
        this.mRealm = mRealm;
    }

    public void initializeCards() {
        cards = new Vector<BusinessCard>();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealm.delete(BusinessCard.class);
            }
        });
    }

    public void initializeGroup() {
        RealmResults<CustomGroup> result = mRealm.where(CustomGroup.class).findAll();
        if (result.size() < 3) {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    mRealm.delete(CustomGroup.class);
                    CustomGroup workerGroup = realm.createObject(CustomGroup.class);
                    workerGroup.setGroupName("직장");
                    CustomGroup familyGroup = realm.createObject(CustomGroup.class);
                    familyGroup.setGroupName("가족");
                    CustomGroup friendGroup = realm.createObject(CustomGroup.class);
                    friendGroup.setGroupName("친구");
                }
            });
        }
        return;
    }

    public void initializeAutoSave(final boolean value) {
        RealmResults<AutoSave> result = mRealm.where(AutoSave.class).findAll();
        if (result == null || result.isEmpty()) {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    mRealm.delete(AutoSave.class);
                    AutoSave autoSave = realm.createObject(AutoSave.class);
                    autoSave.setIsAutoSave(value);
                }
            });
        }
        return;
    }

    public AutoSave getIsAutoSave() {
        return mRealm.where(AutoSave.class).findFirst();
    }

    public void changeAutoSave() {
        final AutoSave autoSave = mRealm.where(AutoSave.class).findFirst();
        final boolean isAuto = autoSave.getIsAutoSave();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (isAuto) {
                    autoSave.setIsAutoSave(false);
                } else {
                    autoSave.setIsAutoSave(true);
                }
            }
        });

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
        this.groups = new Vector<CustomGroup>();
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
    public void deleteCustomGroup(final String groupName, final Context context) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CustomGroup deleteGroup = realm.where(CustomGroup.class).equalTo("groupName", groupName).findFirst();
                if (deleteGroup.getGroupName().equals("직장") || deleteGroup.getGroupName().equals("가족") || deleteGroup.getGroupName().equals("친구")) {
                    Toast.makeText(context, "기본 그룹은 삭제할 수 없습니다!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    groups.remove(deleteGroup);
                    deleteGroup.deleteFromRealm();

                    RealmResults<BusinessCard> resCards = realm.where(BusinessCard.class).equalTo("group", groupName).findAll();
                    for (BusinessCard card : resCards) {
                        card.setGroup(null);
                    }
                }
            }
        });
    }

    public Vector<BusinessCard> getCards() {
        return cards;
    }

    public Vector<CustomGroup> getGroups() {
        return groups;
    }

    //Realm에 BusinessCard 추가하는 함수
    public void addBusinessCard(final BusinessCard card) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number currentIdNum = realm.where(BusinessCard.class).max("id");
                int nextId;
                if (currentIdNum == null) {
                    nextId = 0;
                } else {
                    nextId = currentIdNum.intValue() + 1;
                }
                card.setId(nextId);
                BusinessCard addCard = realm.createObject(BusinessCard.class);
                addCard.Copy(card);
                addCard.setTime(BusinessCard.makeTImeString());
                cards.add(addCard);
            }
        });
    }

    public void editBusinessCard(final BusinessCard card, final int position) {
        final int id = card.getId();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //Realm에서 수정
                BusinessCard editCard = realm.where(BusinessCard.class).equalTo("id", id).findFirst();
                editCard.Copy(card);
                //RealmController가 갖고 있는 List에서 수정
                //Favorite Fragment에서 호출했을 경우 editPosition 찾음
                for (int i = 0; i < cards.size(); i++)
                    if (cards.get(i).getId() == id) {
                        cards.get(i).Copy(editCard);
                        break;
                    }
            }
        });
        return;
    }

    public void changeAllId() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i = 0; i < cards.size(); i++) {
                    cards.get(i).setId(i);
                }
            }
        });
        return;
    }

    public void changeName(final BusinessCard card, final String name) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                int id = card.getId();
                BusinessCard editCard = realm.where(BusinessCard.class).equalTo("id", id).findFirst();
                editCard.setName(name);
            }
        });
        return;
    }

    public void changePhoneNumber(final BusinessCard card, final String phoneNumber) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                int id = card.getId();
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
                int id = card.getId();
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
                int id = card.getId();
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
                int id = card.getId();
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
                int id = card.getId();
                BusinessCard editCard = realm.where(BusinessCard.class).equalTo("id", id).findFirst();
                editCard.setIsFavorite(!editCard.getIsFavorite());
            }
        });
    }

    public void deleteBusinessCard(final BusinessCard card) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                int id = card.getId();
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
