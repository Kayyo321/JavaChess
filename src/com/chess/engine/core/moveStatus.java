package com.chess.engine.core;

public enum moveStatus {
    done,    // possible move.
    illegal, // impossible move.
    suicide  // Leaves king in check if move is done.
}
