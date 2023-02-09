package com.chess.engine.core;

import com.chess.engine.driver.blackPlayer;
import com.chess.engine.driver.player;
import com.chess.engine.driver.whitePlayer;

public enum team {
    white {
        @Override
        public final int getDir() { return -1; }

        @Override
        public player choose(final whitePlayer white, final blackPlayer black) {
            return white;
        }
    },

    black {
        @Override
        public final int getDir() { return 1; }

        @Override
        public player choose(final whitePlayer white, final blackPlayer black) {
            return black;
        }
    };

    public abstract int getDir();

    public abstract player choose(whitePlayer white, blackPlayer black);
}
