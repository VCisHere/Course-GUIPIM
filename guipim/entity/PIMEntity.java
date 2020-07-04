package guipim.entity;

import guipim.user.UserData;

import java.io.Serializable;

public abstract class PIMEntity implements Serializable {
    public static final int NONE = 0;
    public static final int LOW = 1;
    public static final int MID = 2;
    public static final int HIGH = 3;

    String Owner;
    boolean isPublic;
    int Priority;

    PIMEntity() {
        Owner = UserData.getInstance().getSelectUserName();
        isPublic = false;
        Priority = NONE;
    }

    public int getPriority() {
        return Priority;
    }

    public String getPriorityStr() {
        switch (Priority) {
            case LOW: return "[!]";
            case MID: return "[!!]";
            case HIGH: return "[!!!]";
            default: return "";
        }
    }

    public void setIsPublic(boolean b) {
        isPublic = b;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public String getOwner() {
        return Owner;
    }

    public void setPriority(int p) {
        Priority = p;
    }

    abstract public void fromString(String s);

    abstract public String toString();
}
