package com.chess.engine.abstractPieces;

import com.chess.engine.core.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class queen extends piece {
    private final static int[] candidateVecNum = {-9, -8, -7, -1, 1, 7, 8, 9};

    public queen(final int _piecePos, final team _pieceTeam) {
        super(_piecePos, _pieceTeam, piece.pieceType.queen, true);
    }

    public queen(final int _piecePos, final team _pieceTeam, final boolean _firstMove) {
        super(_piecePos, _pieceTeam, piece.pieceType.rook, _firstMove);
    }

    @Override
    public String toString() {
        return piece.pieceType.queen.toString();
    }

    @Override
    public Collection<move> genLegalMoves(final board _b) {
        final List<move> legals = new ArrayList<>();
        int cdc;
        tile cdt;
        piece pad;
        team pt;

        for (final int cco: candidateVecNum) {
            cdc = this.piecePos;

            while (validTileNum(cdc)) {
                if (firstColumnEx(cdc, cco) || eightColumnEx(cdc, cco)) {
                    break;
                }

                cdc += cco;

                if (validTileNum(cdc)) {
                    cdt = _b.getTile(cdc);

                    if (!cdt.filled()) {
                        legals.add(new move.majorMove(_b, this, cdc));
                    } else {
                        pad = cdt.get();
                        pt = pad.getTeam();

                        if (this.pieceTeam != pt) {
                            legals.add(new move.attackMove(_b, this, cdc, pad));
                        }
                    }

                    break;
                }
            }
        }

        return ImmutableList.copyOf(legals);
    }

    @Override
    public piece movePiece(final move _m) {
        return new queen(_m.getDest(), _m.getMovedPiece().getTeam());
    }

    private static boolean firstColumnEx(final int _curPos, final int _canOff) {
        return board.firstColumn[_curPos] && (_canOff == -1 || _canOff == -9 || _canOff == 7);
    }

    private static boolean eightColumnEx(final int _curPos, final int _canOff) {
        return board.eightColumn[_curPos] && (_canOff == -7 || _canOff == -1 || _canOff == 9);
    }
}
