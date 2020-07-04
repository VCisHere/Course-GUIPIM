package guipim.entity;


public class PIMContact extends PIMEntity {

    private String firstName;
    private String lastName;
    private String emailAddress;

    public PIMContact() {
        super();
        firstName = "";
        lastName = "";
        emailAddress = "";
    }

    @Override
    public void fromString(String s) {}

    @Override
    public String toString() {
        return getPriorityStr() + Owner + "的联系人：" + firstName + " " + lastName + "(" + emailAddress + ")";
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}
