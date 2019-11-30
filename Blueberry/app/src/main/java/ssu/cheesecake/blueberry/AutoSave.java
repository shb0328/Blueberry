package ssu.cheesecake.blueberry;

import io.realm.RealmObject;

public class AutoSave extends RealmObject {
    private boolean isAutoSave;

    public AutoSave() {
        this.isAutoSave = true;
    }

    public AutoSave(boolean isAutoSave) {
        this.isAutoSave = isAutoSave;
    }

    public boolean getIsAutoSave() {
        return isAutoSave;
    }

    public void setIsAutoSave(boolean isAutoSave) {
        this.isAutoSave = isAutoSave;
    }
}
