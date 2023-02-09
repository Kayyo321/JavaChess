package com.chess.engine.driver;

import com.chess.engine.abstractPieces.rook;
import com.chess.engine.core.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class whitePlayer extends player {
    public whitePlayer(final board _b, final Collection<move> _legals, final Collection<move> _oppLegals) {
        super(_b, _legals, _oppLegals);
    }

    @Override
    public Collection<piece> getActivePieces() {
        return this.b.getWhitePieces();
    }

    @Override
    public team getTeam() {
        return team.white;
    }

    @Override
    public player getOpp() {
        return this.b.getBlack();
    }

    @Override
    protected Collection<move> calcKingCastles(final Collection<move> _playerLegals, final Collection<move> _oppLegals) {
        final List<move> kingCastles = new ArrayList<>();

        if (this.plrK.firstMove && !this.inCheck()) {
            // Whites king side castle.
            if (!this.b.getTile(61).filled() && !this.b.getTile(62).filled()) {
                final tile rookTile = this.b.getTile(63);

                if (rookTile.filled() && rookTile.get().firstMove) {
                    if (player.calcAttacksOnTile(61, _oppLegals).isEmpty()
                            && player.calcAttacksOnTile(62, _oppLegals).isEmpty()
                            && rookTile.get().getPieceType() == piece.pieceType.rook) {
                        kingCastles.add(new move.kingSideCastleMove(this.b,
                                this.plrK, 62,
                                (rook)rookTile.get(), rookTile.getTileCoord(),
                                59));
                    }
                }
            }

            if (!this.b.getTile(59).filled() && !this.b.getTile(58).filled() && !this.b.getTile(57).filled()) {
                final tile rookTile = this.b.getTile(56);

                if (rookTile.filled() && rookTile.get().firstMove
                    && player.calcAttacksOnTile(58, _oppLegals).isEmpty()
                    && player.calcAttacksOnTile(59, _oppLegals).isEmpty()
                    && rookTile.get().getPieceType() == piece.pieceType.rook) {
                    kingCastles.add(new move.queenSideCastleMove(this.b,
                            this.plrK, 58,
                            (rook)rookTile.get(), rookTile.getTileCoord(),
                            59));
                }
            }
        }

        return ImmutableList.copyOf(kingCastles);
    }
}
