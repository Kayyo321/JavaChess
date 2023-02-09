package com.chess.engine.core;

import com.chess.engine.abstractPieces.pawn;
import com.chess.engine.driver.blackPlayer;
import com.chess.engine.driver.player;
import com.chess.engine.driver.whitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;

public class board {
    public static final boolean[] secondRow  = initRow(8);
    public static final boolean[] seventhRow = initRow(48);

    public static final boolean[] firstColumn   = initColumn(0);
    public static final boolean[] secondColumn  = initColumn(1);
    public static final boolean[] seventhColumn = initColumn(6);
    public static final boolean[] eightColumn   = initColumn(7);

    public static final int numTilesPerRow = 8;
    public static final int numTiles = 64;

    private final Collection<piece> whitePieces;
    private final Collection<piece> blackPieces;
    private final List<tile> gameBoard;

    private final whitePlayer white;
    private final blackPlayer black;
    private final player      curPlr;

    public board(final builder _b) {
        this.gameBoard = createGameBoard(_b);
        this.whitePieces = getActivePieces(this.gameBoard, team.white);
        this.blackPieces = getActivePieces(this.gameBoard, team.black);

        final Collection<move> whiteStdLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<move> blackStdLegalMoves = calculateLegalMoves(this.blackPieces);

        this.white = new whitePlayer(this, whiteStdLegalMoves, blackStdLegalMoves);
        this.black = new blackPlayer(this, whiteStdLegalMoves, blackStdLegalMoves);
        this.curPlr = _b.nextMove.choose(this.white, this.black);
    }

    public player plr() {
        return this.curPlr;
    }

    public Collection<piece> getBlackPieces() {
        return this.blackPieces;
    }

    public Collection<piece> getWhitePieces() {
        return this.whitePieces;
    }

    public player getWhite() {
        return this.white;
    }

    public player getBlack() {
        return this.black;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        String tileText;

        for (int i = 0; i < board.numTiles; i++) {
            tileText = this.gameBoard.get(i).toString();
            sb.append(String.format("%3s", tileText));

            if ((i+1) % board.numTilesPerRow == 0) {
                sb.append('\n');
            }
        }

        return sb.toString();
    }

    private Collection<move> calculateLegalMoves(final Collection<piece> _pieces) {
        final List<move> moves = new ArrayList<>();

        for (final piece p: _pieces) {
            moves.addAll(p.genLegalMoves(this));
        }

        return ImmutableList.copyOf(moves);
    }

    private static Collection<piece> getActivePieces(final List<tile> _gb, final team _t) {
        final List<piece> activePieces = new ArrayList<>();
        piece p;

        for (final tile t: _gb) {
            if (t.filled()) {
                p = t.get();
                if (p.getTeam() == _t) {
                    activePieces.add(p);
                }
            }
        }

        return ImmutableList.copyOf(activePieces);
    }

    private static boolean[] initColumn(int _colNum) {
        final boolean[] column = new boolean[numTiles];

        do {
            column[_colNum] = true;
            _colNum += numTilesPerRow;
        } while (_colNum < numTiles);

        return column;
    }

    private static boolean[] initRow(int _rowNum) {
        final boolean[] row = new boolean[numTiles];

        do {
            row[_rowNum] = true;
            _rowNum++;
        } while(_rowNum % numTilesPerRow != 0);

        return row;
    }

    private static List<tile> createGameBoard(final builder _b) {
        final tile[] tiles = new tile[board.numTiles];

        for (int i = 0; i < board.numTiles; i++) {
            tiles[i] = tile.initTile(i, _b.config.get(i));
        }

        return ImmutableList.copyOf(tiles);
    }

    public tile getTile(final int _tileNum) {
        return gameBoard.get(_tileNum);
    }

    public Iterable<move> getAllLegals() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.white.getLegals(), this.black.getLegals()));
    }

    public static class builder {
        Map<Integer, piece> config;
        team nextMove;
        pawn enPassantPawn;

        public builder() {
            this.config = new HashMap<>();
        }

        public builder setPiece(final piece _p) {
            this.config.put(_p.getPos(), _p);
            return this;
        }

        public builder setMove(final team _t) {
            this.nextMove = _t;
            return this;
        }

        public board build() {
            return new board(this);
        }

        public void setEnPassantPawn(pawn _movedPawn) {
            this.enPassantPawn = _movedPawn;
        }
    }
}
