package entities;

public abstract class Employee {

    protected int id;

    public Employee(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}