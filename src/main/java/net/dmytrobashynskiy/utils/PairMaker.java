package net.dmytrobashynskiy.utils;


import net.dmytrobashynskiy.cables.Cable;
import net.dmytrobashynskiy.cables.cable_components.Pair;

public class PairMaker {

    private final static float lowerBoundary = 0.5f;
    private final static float upperBoundary = 2.5f;

    public static Pair makePair(Cable cableParent){
        Pair pair = new Pair(getRandomNumber(), cableParent);
        return pair;
    }

    private static float getRandomNumber() {
        return (float) ((Math.random() * (upperBoundary - lowerBoundary)) + lowerBoundary);
    }


}
