package ssu.cheesecake.blueberry.custom;

import io.realm.RealmObject;

public class CustomGroup extends RealmObject {
    String groupName;

    public CustomGroup() {
        this.groupName = null;
    }

    public CustomGroup(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void Copy(CustomGroup src) {
        this.groupName = src.getGroupName();
        return;
    }
}
