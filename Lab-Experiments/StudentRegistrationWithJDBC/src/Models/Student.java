package Models;

public class Student {

    private int id;
    private String name;
    private String gender;
    private String branch;

    public Student(int id, String name, String gender, String branch) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.branch = branch;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getBranch() {
        return branch;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}