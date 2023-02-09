package com.chess.engine.core;

import com.chess.engine.abstractPieces.pawn;

public abstract class move {
    protected final board brd;
    protected final piece movedPiece;
    protected final int   destination;

    public static nilMove cachedNilMove = new nilMove();

    private final int chachedHashCode;

    private move(final board _brd, final piece _movedPiece, final int _destination) {
        this.brd = _brd;
        this.movedPiece = _movedPiece;
        this.destination = _destination;

        final int prime = 31;
        int result = 1;
        //result = prime * result + this.movedPiece.getPos();
        result = prime * result + this.destination;
        result = prime * result + this.movedPiece.hashCode();
        chachedHashCode = result;
    }

    @Override
    public int hashCode() {
        return this.chachedHashCode;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof move)) {
            return false;
        }

        final move otherMove = (move) other;

        return this.getDest() == otherMove.getDest() && this.getMovedPiece() == otherMove.getMovedPiece();
    }

    public int getSrc() {
        return this.movedPiece.getPos();
    }

    public int getDest() {
        return this.destination;
    }

    public piece getMovedPiece() {
        return this.movedPiece;
    }

    public boolean isAttack() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public piece getAttackedPiece() {
        return null;
    }

    public board execute() {
        final board.builder b = new board.builder();

        for (final piece p: this.brd.plr().getActivePieces()) {
            if (!this.movedPiece.equals(p)) {
                b.setPiece(p);
            }
        }

        for (final piece p: this.brd.plr().getOpp().getActivePieces()) {
            b.setPiece(p);
        }

        b.setPiece(this.movedPiece.movePiece(this));
        b.setMove(this.brd.plr().getOpp().getTeam());

        return b.build();
    }

    public static final class majorMove extends move {
        public majorMove(final board _brd, final piece _movedPiece, final int _destination) {
            super(_brd, _movedPiece, _destination);
        }
    }

    public static class attackMove extends move {
        final piece attackedPiece;

        public attackMove(final board _brd, final piece _movedPiece, final int _destination, final piece _attackedPiece) {
            super(_brd, _movedPiece, _destination);
            this.attackedPiece = _attackedPiece;
        }

        @Override
        public board execute() {
            return null;
        }

        @Override
        public boolean isAttack() { return true; }

        @Override
        public piece getAttackedPiece() { return this.attackedPiece; }

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            } else if (!(other instanceof attackMove)) {
                return false;
            }

            final attackMove otherAttackMove = (attackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }
    }

    public static class pawnMove extends attackMove {
        public pawnMove(final board _brd, final piece _movedPiece, final int _destination, final piece _attackedPiece) {
            super(_brd, _movedPiece, _destination, _attackedPiece);
        }
    }

    public static final class pawnEnPassantAttackMove extends pawnMove {
        public pawnEnPassantAttackMove(final board _brd, final piece _movedPiece, final int _destination, final piece _attackedPiece) {
            super(_brd, _movedPiece, _destination, _attackedPiece);
        }

        @Override
        public board execute() {
            final board.builder b = new board.builder();

            for (final piece p: this.brd.plr().getActivePieces()) {
                if (!this.movedPiece.equals(p)) {
                    b.setPiece(p);
                }
            }

            for (final piece p: this.brd.plr().getOpp().getActivePieces()) {
                b.setPiece(p);
            }

            final pawn movedPawn = (pawn) this.movedPiece.movePiece(this);
            b.setPiece(movedPawn);
            b.setEnPassantPawn(movedPawn);
            b.setMove(this.brd.plr().getOpp().getTeam());

            return b.build();
        }
    }

    public static class pawnJump extends move {
        public pawnJump(final board _brd, final piece _movedPiece, final int _destination) {
            super(_brd, _movedPiece, _destination);
        }
    }

    static abstract class castleMove extends move {
        public castleMove(final board _brd, final piece _movedPiece, final int _destination) {
            super(_brd, _movedPiece, _destination);
        }
    }

    public static class kingSideCastleMove extends move {
        public kingSideCastleMove(final board _brd, final piece _movedPiece, final int _destination) {
            super(_brd, _movedPiece, _destination);
        }
    }

    public static class queenSideCastleMove extends move {
        public queenSideCastleMove(final board _brd, final piece _movedPiece, final int _destination) {
            super(_brd, _movedPiece, _destination);
        }
    }

    public static class nilMove extends move {
        public nilMove() {
            super(null, null, -1);
        }

        @Override
        public board execute() throws RuntimeException {
            throw new RuntimeException("Cannot execute the nil move!");
        }
    }

    public static class moveFactory {
        private moveFactory() throws RuntimeException {
            throw new RuntimeException("Cannot init static class: moveFactory... sorry... ;)");
        }

        public static move initMove(final board b, final int cc, final int dc) {
            for (final move m: b.getAllLegals()) {
                if (m.getSrc() == cc && m.getDest() == dc) {
                    return m;
                }
            }

            return cachedNilMove;
        }
    }
}
