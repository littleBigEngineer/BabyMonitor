package neo.baba.neonatalmonitoring.neo.baba.neonatalmonitoring.model;

public class Account {
    private String email, firstName, lastName, uid, password, phone;

    public Account(String email, String firstName, String lastName, String uid, String password, String phone) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.uid = uid;
        this.password = password;
        this.phone = phone;
    }

    public Account(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String fName) {
        this.firstName = fName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
