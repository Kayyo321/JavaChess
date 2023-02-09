package com.chess.engine.abstractPieces;

import com.chess.engine.core.board;
import com.chess.engine.core.move;
import com.chess.engine.core.piece;
import com.chess.engine.core.team;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class pawn extends piece {
    private final static int[] candidateMoveNum = {7, 8, 9, 16};

    public pawn(final int _piecePos, final team _pieceTeam) {
        super(_piecePos, _pieceTeam, piece.pieceType.pawn, true);
    }

    public pawn(final int _piecePos, final team _pieceTeam, final boolean _firstMove) {
        super(_piecePos, _pieceTeam, piece.pieceType.rook, _firstMove);
    }

    @Override
    public String toString() {
        return piece.pieceType.pawn.toString();
    }

    @Override
    public Collection<move> genLegalMoves(final board _b) {
        final List<move> legals = new ArrayList<>();
        int cdc;
        int bhd;
        piece poc;

        for (final int cco: candidateMoveNum) {
            cdc = this.piecePos + (this.getTeam().getDir() * cco);

            switch (cco) {
                case 7:
                    if (!   ((board.eightColumn[this.piecePos] && this.pieceTeam == team.white)
                        ||  ( board.firstColumn[this.piecePos] && this.pieceTeam == team.black))) {
                        if (_b.getTile(cdc).filled()) {
                            poc = _b.getTile(cdc).get();
                            if (this.pieceTeam != poc.getTeam()) {
                                legals.add(new move.pawnEnPassantAttackMove(_b, this, cdc, poc));
                            }
                        }
                    }
                    break;

                case 8:
                    if (!_b.getTile(cdc).filled()) {
                        // TODO: more work to do here!
                        legals.add(new move.majorMove(_b, this, cdc));
                    }
                    break;

                case 9:
                    if (!   ((board.firstColumn[this.piecePos] && this.pieceTeam == team.white)
                        ||  ( board.eightColumn[this.piecePos] && this.pieceTeam == team.black))) {
                        if (_b.getTile(cdc).filled()) {
                            poc = _b.getTile(cdc).get();
                            if (this.pieceTeam != poc.getTeam()) {
                                legals.add(new move.pawnEnPassantAttackMove(_b, this, cdc, poc));
                            }
                        }
                    }
                    break;

                case 16:
                    if (this.firstMove
                        && (board.seventhRank[this.piecePos]  && this.pieceTeam == team.black)
                        || (board.secondRank[this.piecePos] && this.pieceTeam == team.white)) {
                        bhd = this.piecePos + (this.pieceTeam.getDir() * 8);

                        if (!_b.getTile(bhd).filled() && !_b.getTile(cdc).filled()) {
                            legals.add(new move.pawnJump(_b, this, cdc));
                        }
                    }
                    break;
            }
        }

        return ImmutableList.copyOf(legals);
    }

    @Override
    public piece movePiece(final move _m) {
        return new pawn(_m.getDest(), _m.getMovedPiece().getTeam());
    }
}
