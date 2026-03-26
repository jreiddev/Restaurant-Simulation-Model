package models;

import java.util.Random;


//legacy class, keep for now JUST IN CASE, remove in final product
public class CookModel {

    private Random rand = new Random();

    public double getCookTime() {

        //cooking time between 8–14 minutes, sometimes
        return 8 + rand.nextDouble() * 6;

    }
}