package guipim.entity;

import java.util.Date;

public class PIMAppointment extends PIMEntity {
    private Date date;
    private String description;

    public PIMAppointment() {
        super();
        date = new Date();
        description = "";
    }

    @Override
    public void fromString(String s) {

    }

    @Override
    public String toString() {
        return getPriorityStr() + this.Owner + "的约会：" + this.description;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

}
