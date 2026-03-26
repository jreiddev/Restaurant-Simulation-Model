package entities;

public class Cook extends Employee {

    private boolean busy = false;

    public Cook(int id) {
        super(id);
    }

    public boolean isAvailable() {
        return !busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public double generateCookTime() {
        return 8 + Math.random() * 6; // 8–14 minutes
    } //cooks sometimes are focused/get distracted this simulates that within normal ranged window. also for increased difficulty meals
}

