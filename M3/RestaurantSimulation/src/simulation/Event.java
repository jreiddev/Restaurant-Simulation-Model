package simulation;

import entities.Cook;
import entities.Customer;

public class Event implements Comparable<Event> {

    public double time;//when event happens
    public EventType type;
    public Customer customer;//WHICH customer is involved
    public Cook cook;

    //constructors
    public Event(double time, EventType type, Customer customer) {
        this.time = time;
        this.type = type;
        this.customer = customer;
        this.cook = null; //important, error w/o
    }
    public Event(double time, EventType type, Customer customer, Cook cook) {
        this.time = time;
        this.type = type;
        this.customer = customer;
        this.cook = cook;
    }

    @Override
    public int compareTo(Event other) { //priority queue comparision
        return Double.compare(this.time, other.time);
    }
}