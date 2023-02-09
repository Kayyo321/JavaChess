package com.chess.engine.abstractPieces;

import com.chess.engine.core.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class knight extends piece {
    private final static int[] candidateMoveNum = {-17, -15, -10, -6, 6, 10, 15, 17};

    public knight(final int _pieceNum, final team _pieceTeam) {
        super(_pieceNum, _pieceTeam, piece.pieceType.knight);
    }

    @Override
    public String toString() {
        return piece.pieceType.knight.toString();
    }

    @Override
    public Collection<move> genLegalMoves(final board _b) {
        int cdc;
        final List<move> legals = new ArrayList<>();
        tile cdt;
        piece pad;
        team pt;

        for (final int cur: candidateMoveNum) {
            cdc = this.piecePos + cur;

            if (validTileNum(cdc)) {
                if (   firstColumnEx(this.piecePos, cur)   || secondColumnEx(this.piecePos, cur)
                    || seventhColumnEx(this.piecePos, cur) || eightColumnEx(this.piecePos, cur)) {
                    continue;
                }

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
            }
        }

        return ImmutableList.copyOf(legals);
    }

    @Override
    public piece movePiece(final move _m) {
        return new knight(_m.getDest(), _m.getMovedPiece().getTeam());
    }

    private static boolean firstColumnEx(final int _curPos, final int _canOff) {
        return board.firstColumn[_curPos] && ((_canOff == -17) || (_canOff == -10) || _canOff == 6 || _canOff == 15);
    }

    private static boolean secondColumnEx(final int _curPos, final int _canOff) {
        return board.secondColumn[_curPos] && ((_canOff == -10) || _canOff == 6);
    }

    private static boolean seventhColumnEx(final int _curPos, final int _canOff) {
        return board.seventhColumn[_curPos] && ((_canOff == -6) || _canOff == 10);
    }

    private static boolean eightColumnEx(final int _curPos, final int _canOff) {
        return board.eightColumn[_curPos] && ((_canOff == -15) || (_canOff == -6) || (_canOff == 10) || (_canOff == 17));
    }
}
