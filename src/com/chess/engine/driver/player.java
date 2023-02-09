package com.chess.engine.driver;

import com.chess.engine.abstractPieces.king;
import com.chess.engine.core.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class player {
    protected final board b;
    protected final king plrK;
    protected final Collection<move> legals;

    private final boolean isInCheck;

    public player(final board _b, final Collection<move> _legals, final Collection<move> _oppLegals) throws RuntimeException {
        this.b = _b;
        this.plrK = establishKing();
        if (this.plrK == null) {
            throw new RuntimeException("King was not established... Board initialized unsuccessfully...");
        }
        this.legals = ImmutableList.copyOf(Iterables.concat(_legals, calcKingCastles(_legals, _oppLegals)));
        this.isInCheck = !player.calcAttacksOnTile(this.plrK.getPos(), _oppLegals).isEmpty();
    }

    protected static Collection<move> calcAttacksOnTile(final int _pos, final Collection<move> _moves) {
        final List<move> attackMoves = new ArrayList<>();

        for (final move m: _moves) {
            if (_pos == m.getDest()) {
                attackMoves.add(m);
            }
        }

        return ImmutableList.copyOf(attackMoves);
    }

    public Collection<move> getLegals() {
        return this.legals;
    }

    public boolean moveLegal(final move _m) {
        return this.legals.contains(_m);
    }

    public boolean inCheck() {
        return this.isInCheck;
    }

    public boolean inCheckMate() {
        return this.isInCheck && !hasEscapeMoves();
    }

    public boolean inStaleMate() {
        return !this.isInCheck && !hasEscapeMoves();
    }

    public boolean castled() {
        // TODO: implement.
        return false;
    }

    public moveTrans makeMove(final move _m) {
        if (!moveLegal(_m)) {
            return new moveTrans(this.b, _m, moveStatus.illegal);
        }

        final board transitionBrd = _m.execute();

        final Collection<move> kingAttacks = player.calcAttacksOnTile(transitionBrd.plr().getOpp().plrK.getPos(), transitionBrd.plr().legals);

        if (!kingAttacks.isEmpty()) {
            return new moveTrans(this.b, _m, moveStatus.suicide);
        }

        return new moveTrans(transitionBrd, _m, moveStatus.done);
    }

    public abstract Collection<piece> getActivePieces();
    public abstract team getTeam();
    public abstract player getOpp();

    protected abstract Collection<move> calcKingCastles(final Collection<move> _playerLegals, final Collection<move> _oppLegals);

    protected boolean hasEscapeMoves() {
        for (final move m: this.legals) {
            final moveTrans transition = makeMove(m);
            if (transition.getStatus() == moveStatus.done) {
                return true;
            }
        }

        return false;
    }

    private king establishKing() {
        for (final piece p : getActivePieces()) {
            if (p.getPieceType().equals(piece.pieceType.king)) {
                return (king) p;
            }
        }

        return null;
    }
}
