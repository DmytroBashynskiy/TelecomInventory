package net.dmytrobashynskiy.cables;

import net.dmytrobashynskiy.utils.RandomNumberMaker;

import java.util.ArrayList;

import static net.dmytrobashynskiy.utils.PairMaker.makePair;

public class Cable100 extends Cable {
    public Cable100() {
        pairs = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            pairs.add(makePair(this));
        }
        cableName = String.format("Cable100-N-%d", RandomNumberMaker.getInstance().generateNumber());
    }

}
