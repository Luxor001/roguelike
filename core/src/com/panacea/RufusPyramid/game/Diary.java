package com.panacea.RufusPyramid.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gio on 29/07/15.
 */
public class Diary {
    ArrayList<String> lines;

    public Diary() {
        this.lines = new ArrayList<String>();
    }

    public void addLine(String lineOfText) {
        this.lines.add(lineOfText);
    }

    public List<String> getLastThreeLines() {
        ArrayList<String> toReturn = new ArrayList<String>();
        int numElements = Math.min(3, lines.size());

        for (int i = 0; i < numElements; i++) {
            toReturn.add(this.lines.get(lines.size() - 1 - i));
        }
        return toReturn;
    }

    public ArrayList<String> getAllLines() {
        return this.lines;
    }
}
