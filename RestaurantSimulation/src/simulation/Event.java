package simulation;

import entities.Customer;

public class Event implements Comparable<Event> {

    public double time;//when event happens
    public EventType type;
    public Customer customer;//WHICH customer is involved

    public Event(double time, EventType type, Customer customer) { //constructor
        this.time = time;
        this.type = type;
        this.customer = customer;
    }

    @Override
    public int compareTo(Event other) { //priority queue comparision
        return Double.compare(this.time, other.time);
    }
}