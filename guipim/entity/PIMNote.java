package guipim.entity;


public class PIMNote extends PIMEntity {
    private String note;

    public PIMNote() {
        super();
        note = "";
    }

    @Override
    public void fromString(String s) {

    }

    @Override
    public String toString() {
        return getPriorityStr() + this.Owner + "的备忘：\n" + this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return this.note;
    }

}
