package entities;

public class Cook extends Employee {

    public Cook(int id) {
        super(id);
    }

    public double generateCookTime() {
        return 10 + (Math.random() * 10);
    }
}

/*
To DO:
need cooking delays via error/utilization rates
assign meals to cooks
have cooks "clocked in" and "clocked out"
cut cooks based on utilization rate after peak hours

 */