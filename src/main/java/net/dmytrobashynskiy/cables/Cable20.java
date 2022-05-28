package net.dmytrobashynskiy.cables;

import net.dmytrobashynskiy.utils.RandomNumberMaker;

import java.util.ArrayList;

import static net.dmytrobashynskiy.utils.PairMaker.makePair;

public class Cable20 extends Cable {
    public Cable20() {
        pairs = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            pairs.add(makePair(this));
        }
        cableName = String.format("Cable20-N-%d",RandomNumberMaker.getInstance().generateNumber());
    }
    
}
