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
        return this.pt.getVal();
    }

    public enum pieceType {
        pawn("P", 100),
        knight("N", 300),
        bishop("B", 300),
        rook("R", 500),
        queen("Q", 900),
        king("K", 10000);

        private final String pieceName;
        private final int val;

        pieceType(final String _pieceName, final int _val) {
            this.pieceName = _pieceName;
            this.val = _val;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public int getVal() {
            return this.val;
        }
    }
}
