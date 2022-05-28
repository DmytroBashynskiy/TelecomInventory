package net.dmytrobashynskiy.utils;

import java.util.HashSet;
import java.util.Set;

public class RandomNumberMaker {
    private static RandomNumberMaker INSTANCE;
    private Set<Integer> usedNumbers;

    private RandomNumberMaker(){
        usedNumbers = new HashSet<>();
    }

    public static RandomNumberMaker getInstance(){
        if(INSTANCE == null) {
            INSTANCE = new RandomNumberMaker();
        }
        return INSTANCE;
    }

    public int generateNumber(){
        int min = 0;
        int max = 1000000000;
        Integer result = 0;
        boolean success = false;
        while(!success){
            result = Math.abs((int) ((Math.random() * (max - min)) + min));
            if(!usedNumbers.contains(result)){
                usedNumbers.add(result);
                success = true;
            }
        }
        return result;
    }

}
