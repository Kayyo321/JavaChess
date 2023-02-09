package com.chess.engine.core;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public abstract class tile {
    // Default to a failed value.
    protected final int tileNum;

    private static final Map<Integer, emptyTile> emptyTiles = initTiles();

    private tile(final int _tileNum) { this.tileNum = _tileNum; }

    public static tile initTile(final int _tileNum, final piece _piece) {
        return _piece != null ? new filledTile(_tileNum, _piece) : emptyTiles.get(_tileNum);
    }

    // Initialize all possible empty tiles.
    private static Map<Integer, emptyTile> initTiles() {
        final Map<Integer, emptyTile> etp = new HashMap<>();

        for (int i = 0; i < board.numTiles; i++) {
            etp.put(i, new emptyTile(i));
        }

        return ImmutableMap.copyOf(etp);
    }

    // If the tile is filled by a chess piece or not.
    public abstract boolean filled();

    // Get the current piece.
    public abstract piece get();

    public static final class emptyTile extends tile {
        public emptyTile(final int _tileNum) { super(_tileNum); }

        @Override
        public final boolean filled() { return false; }

        @Override
        public final piece get() { return null; }

        @Override
        public String toString() {
            return "-";
        }
    }

    public static final class filledTile extends tile {
        private final piece pieceOnTile;

        public filledTile(final int _tileNum, final piece _pieceOnTile) {
            super(_tileNum);
            this.pieceOnTile = _pieceOnTile;
        }

        @Override
        public final boolean filled() { return true; }

        @Override
        public final piece get() { return this.pieceOnTile; }

        @Override
        public String toString() {
            return this.get().getTeam() == team.black ? this.get().toString().toLowerCase() : this.get().toString();
        }
    }
}
