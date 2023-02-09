package com.chess.engine.core;

import java.util.concurrent.Future;

public class moveTrans {
    private final board transBoard;
    private final move m;
    private final moveStatus ms;

    public moveTrans(final board _transBoard, final move _m, final moveStatus _ms) {
        this.transBoard = _transBoard;
        this.m = _m;
        this.ms = _ms;
    }

    public moveStatus getStatus() { return this.ms; }
    public board getTransBoard() { return this.transBoard; }
}
