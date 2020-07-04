package guipim.entity;

import java.util.Date;

public class PIMTodo extends PIMEntity {
    private Date date;
    private String todo;

    public PIMTodo() {
        super();
        date = new Date();
        todo = "";
    }

    @Override
    public void fromString(String s) {

    }

    @Override
    public String toString() {
        return getPriorityStr() + this.Owner + "的待办：" + this.todo;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return this.date;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getTodo() {
        return this.todo;
    }
}
