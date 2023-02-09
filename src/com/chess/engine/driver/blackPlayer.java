package com.chess.engine.driver;

import com.chess.engine.core.board;
import com.chess.engine.core.move;
import com.chess.engine.core.piece;
import com.chess.engine.core.team;

import java.util.Collection;

public class blackPlayer extends player {
    public blackPlayer(final board _b, final Collection<move> _legals, final Collection<move> _oppLegals) {
        super(_b, _oppLegals, _legals);
    }

    @Override
    public Collection<piece> getActivePieces() {
        return this.b.getBlackPieces();
    }

    @Override
    public team getTeam() {
        return team.black;
    }

    @Override
    public player getOpp() {
        return this.b.getWhite();
    }
}
