package com.chess.engine.core;

import java.util.Collection;

public abstract class piece {
    public final boolean firstMove;

    protected final int piecePos;
    protected final team pieceTeam;
    protected final pieceType pt;

    private final int cachedHashCode;

    public piece(final int _piecePos, final team _pieceTeam, final pieceType _pt, final boolean _firstMove) {
        this.piecePos = _piecePos;
        this.pieceTeam = _pieceTeam;
        this.firstMove = _firstMove;
        this.pt = _pt;

        int result = this.pt.hashCode();
        result = 31 * result + this.pieceTeam.hashCode();
        result = 31 * result + this.piecePos;
        result = 31 * result + (this.firstMove ? 1 : 0);
        this.cachedHashCode = result;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof piece)) {
            return false;
        }

        final piece otherPiece = (piece)other;

        return     (this.piecePos  == otherPiece.getPos() ) && (this.pt        == otherPiece.getPieceType())
                && (this.pieceTeam == otherPiece.getTeam()) && (this.firstMove == otherPiece.firstMove     );
    }

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    public team getTeam() { return this.pieceTeam; }
    public static boolean validTileNum(final int _tileNum) {
        return _tileNum >= 0 && _tileNum < 64;
    }
    public abstract Collection<move> genLegalMoves(final board _b);

    public int getPos() {
        return this.piecePos;
    }

    public pieceType getPieceType() {
        return this.pt;
    }

    public abstract piece movePiece(final move _m);

    public int getPieceVal() {
        // FIXME: stub.
        return 0;
    }

    public enum pieceType {
        pawn("P"),
        knight("N"),
        bishop("B"),
        rook("R"),
        queen("Q"),
        king("K");

        private String pieceName;

        pieceType(final String _pieceName) {
            this.pieceName = _pieceName;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }
    }
}
