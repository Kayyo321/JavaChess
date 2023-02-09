package com.chess.engine.driver;

import com.chess.engine.core.board;
import com.chess.engine.core.move;
import com.chess.engine.core.piece;
import com.chess.engine.core.team;

import java.util.Collection;

public class whitePlayer extends player {
    public whitePlayer(final board _b, final Collection<move> _legals, final Collection<move> _oppLegals) {
        super(_b, _legals, _oppLegals);
    }

    @Override
    public Collection<piece> getActivePieces() {
        return this.b.getWhitePieces();
    }

    @Override
    public team getTeam() {
        return team.white;
    }

    @Override
    public player getOpp() {
        return this.b.getBlack();
    }
}
