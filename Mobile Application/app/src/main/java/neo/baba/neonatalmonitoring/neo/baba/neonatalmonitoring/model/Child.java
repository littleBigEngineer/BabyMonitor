package neo.baba.neonatalmonitoring.neo.baba.neonatalmonitoring.model;

public class Child {

    private String firstName;
    private String dob;

    private String id;

    public Child() {
    }

    public Child(String firstName, String dob, String id) {
        this.firstName = firstName;
        this.dob = dob;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
