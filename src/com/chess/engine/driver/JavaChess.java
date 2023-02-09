package com.chess.engine.driver;

import com.chess.engine.abstractPieces.*;
import com.chess.engine.core.board.abstractPieces.*;
import com.chess.engine.core.board;
import com.chess.engine.core.team;

public class JavaChess {
    private static board.builder initStd() {
        final board.builder bld = new board.builder();

        // Black layout
        bld.setPiece(new rook(0,    team.black));
        bld.setPiece(new knight(1,  team.black));
        bld.setPiece(new bishop(2,  team.black));
        bld.setPiece(new queen(3,   team.black));
        bld.setPiece(new king(4,    team.black));
        bld.setPiece(new bishop(5,  team.black));
        bld.setPiece(new knight(6,  team.black));
        bld.setPiece(new rook(7,    team.black));
        bld.setPiece(new pawn(8,    team.black));
        bld.setPiece(new pawn(9,    team.black));
        bld.setPiece(new pawn(10,   team.black));
        bld.setPiece(new pawn(11,   team.black));
        bld.setPiece(new pawn(12,   team.black));
        bld.setPiece(new pawn(13,   team.black));
        bld.setPiece(new pawn(14,   team.black));
        bld.setPiece(new pawn(15,   team.black));

        // White layout
        bld.setPiece(new pawn(48,   team.white));
        bld.setPiece(new pawn(49,   team.white));
        bld.setPiece(new pawn(50,   team.white));
        bld.setPiece(new pawn(51,   team.white));
        bld.setPiece(new pawn(52,   team.white));
        bld.setPiece(new pawn(53,   team.white));
        bld.setPiece(new pawn(54,   team.white));
        bld.setPiece(new pawn(55,   team.white));
        bld.setPiece(new rook(56,   team.white));
        bld.setPiece(new knight(57, team.white));
        bld.setPiece(new bishop(58, team.white));
        bld.setPiece(new queen(59,  team.white));
        bld.setPiece(new king(60,   team.white));
        bld.setPiece(new bishop(61, team.white));
        bld.setPiece(new knight(62, team.white));
        bld.setPiece(new rook(63,   team.white));

        return bld;
    }

    public static void main(String[] args) {
        board b = new board(initStd());
        System.out.println(b);
    }
}
