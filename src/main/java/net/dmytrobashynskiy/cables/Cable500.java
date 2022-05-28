package net.dmytrobashynskiy.cables;

import net.dmytrobashynskiy.utils.RandomNumberMaker;

import java.util.ArrayList;

import static net.dmytrobashynskiy.utils.PairMaker.makePair;

public class Cable500 extends Cable {
    public Cable500() {
        pairs = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            pairs.add(makePair(this));
        }
        cableName = String.format("Cable500-N-%d", RandomNumberMaker.getInstance().generateNumber());
    }
}
