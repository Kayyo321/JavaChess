package com.chess.engine.driver;

import com.chess.engine.abstractPieces.rook;
import com.chess.engine.core.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class blackPlayer extends player {
    public blackPlayer(final board _b, final Collection<move> _legals, final Collection<move> _oppLegals) {
        super(_b, _oppLegals, _legals);
    }

    @Override
    public Collection<piece> getActivePieces() {
        return this.b.getBlackPieces();
    }

    @Override
    public team getTeam() {
        return team.black;
    }

    @Override
    public player getOpp() {
        return this.b.getWhite();
    }

    @Override
    protected Collection<move> calcKingCastles(final Collection<move> _playerLegals, final Collection<move> _oppLegals) {
        final List<move> kingCastles = new ArrayList<>();

        if (this.plrK.firstMove && !this.inCheck()) {
            // Black king side castle.
            if (!this.b.getTile(5).filled() && !this.b.getTile(6).filled()) {
                final tile rookTile = this.b.getTile(7);

                if (rookTile.filled() && rookTile.get().firstMove
                    && player.calcAttacksOnTile(2, _oppLegals).isEmpty() && player.calcAttacksOnTile(3, _oppLegals).isEmpty()
                    && rookTile.get().getPieceType() == piece.pieceType.rook) {
                    if (player.calcAttacksOnTile(5, _oppLegals).isEmpty()
                            && player.calcAttacksOnTile(6, _oppLegals).isEmpty()
                            && rookTile.get().getPieceType() == piece.pieceType.rook) {
                        kingCastles.add(new move.kingSideCastleMove(this.b,
                                this.plrK, 6,
                                (rook)rookTile.get(), rookTile.getTileCoord(),
                                5));
                    }
                }
            }

            if (!this.b.getTile(1).filled() && !this.b.getTile(2).filled() && !this.b.getTile(3).filled()) {
                final tile rookTile = this.b.getTile(0);

                if (rookTile.filled() && rookTile.get().firstMove) {
                    kingCastles.add(new move.queenSideCastleMove(this.b,
                            this.plrK, 2,
                            (rook) rookTile.get(), rookTile.getTileCoord(),
                            3));
                }
            }
        }

        return ImmutableList.copyOf(kingCastles);
    }
}
