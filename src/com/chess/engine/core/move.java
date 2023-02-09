package com.chess.engine.core;

import com.chess.engine.abstractPieces.pawn;
import com.chess.engine.abstractPieces.rook;

public abstract class move {
    protected final board brd;
    protected final piece movedPiece;
    protected final int   destination;
    protected final boolean firstMove;

    private final int cachedHashCode;

    private move(final board _brd, final piece _movedPiece, final int _destination) {
        this.brd = _brd;
        this.movedPiece = _movedPiece;
        this.destination = _destination;

        this.firstMove = this.movedPiece.firstMove;

        final int prime = 31;
        int result = 1;
        result = prime * result + this.destination;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPos();
        this.cachedHashCode = result;
    }

    private move(final board _brd, final int _destination) {
        this.brd = _brd;
        this.movedPiece = null;
        this.destination = _destination;
        this.firstMove = false;
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destination;
        this.cachedHashCode = result;
    }

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof move)) {
            return false;
        }

        final move otherMove = (move) other;

        return this.getDest() == otherMove.getDest() && this.getMovedPiece() == otherMove.getMovedPiece() && this.getSrc() == otherMove.getSrc();
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

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof majorMove && super.equals(other);
        }

        @Override
        public String toString() {
            return this.movedPiece.getPieceType().toString() + board.getPosAtCoord(this.destination);
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
    }

    public static class pawnJump extends move {
        public pawnJump(final board _brd, final piece _movedPiece, final int _destination) {
            super(_brd, _movedPiece, _destination);
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

    static abstract class castleMove extends move {
        protected final rook castleRook;
        protected final int  rookSrc;
        protected final int  rookDest;

        public castleMove(final board _brd, final piece _movedPiece, final int _destination, final rook _castleRook, final int _rookSrc, final int _rookDest) {
            super(_brd, _movedPiece, _destination);
            this.castleRook = _castleRook;
            this.rookSrc = _rookSrc;
            this.rookDest = _rookDest;
        }

        public rook getCastleRook() { return this.castleRook; }

        @Override
        public boolean isCastlingMove() { return true; }

        @Override
        public board execute() {
            final board.builder b = new board.builder();

            for (final piece p: this.brd.plr().getActivePieces()) {
                if (!this.movedPiece.equals(p) && !this.castleRook.equals(p)) {
                    b.setPiece(p);
                }
            }

            for (final piece p: this.brd.plr().getOpp().getActivePieces()) {
                b.setPiece(p);
            }

            b.setPiece(this.movedPiece.movePiece(this));
            b.setPiece(new rook(this.castleRook.getPos(), this.castleRook.getTeam()));
            b.setMove(this.brd.plr().getOpp().getTeam());

            return b.build();
        }
    }

    public static class kingSideCastleMove extends castleMove {
        public kingSideCastleMove(final board _brd, final piece _movedPiece, final int _destination, final rook _castleRook, final int _rookSrc, final int _rookDest) {
            super(_brd, _movedPiece, _destination, _castleRook, _rookSrc, _rookDest);
        }

        @Override
        public String toString() {
            return "0-0";
        }
    }

    public static class queenSideCastleMove extends castleMove {
        public queenSideCastleMove(final board _brd, final piece _movedPiece, final int _destination, final rook _castleRook, final int _rookSrc, final int _rookDest) {
            super(_brd, _movedPiece, _destination, _castleRook, _rookSrc, _rookDest);
        }

        @Override
        public String toString() {
            return "0-0-0";
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

            return null;
        }
    }
}
