package com.chess.engine.driver;

import com.chess.engine.abstractPieces.*;
import com.chess.engine.core.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class table {
    private final JFrame jf;
    private boardPanel bp;
    private board chessBoard;

    private tile srcTile;
    private tile destTile;
    private piece humanMovedPiece;

    private static final Dimension outerFrameDim = new Dimension(600, 600);

    private Color lightColor;
    private Color darkColor;

    private static final String iconPath = "imgs/";

    private static board.builder initStd() {
        final board.builder bld = new board.builder();

        bld.setMove(team.white);

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

    public table() {
        this.jf = new JFrame("Java Chess ☕ -- By Sullivan");
        this.jf.setLayout(new BorderLayout());

        chessBoard = new board(initStd());

        lightColor = new Color(179, 135, 117);
        darkColor = new Color(255, 221, 186);

        final JMenuBar jmb = new JMenuBar();

        // File menu
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN File...");
        openPGN.addActionListener(event -> System.out.println("Opening .PGN file..."));
        fileMenu.add(openPGN);

        final JMenuItem savePGN = new JMenuItem("Save PGN File...");
        savePGN.addActionListener(event -> System.out.println("Saving .PGN file..."));
        fileMenu.add(savePGN);

        final JMenuItem exit = new JMenuItem("Exit Java Chess...");
        exit.addActionListener(event -> {
            System.out.println("Exiting Java Chess...");
            exit(0);
        });
        fileMenu.add(exit);

        // Preferences menu
        final JMenu prefMenu = new JMenu("Preferences");
        final JMenuItem colors = new JMenuItem("Change Board Colors...");
        colors.addActionListener(event -> System.out.println("Changing board colors..."));
        prefMenu.add(colors);

        jmb.add(fileMenu);
        jmb.add(prefMenu);

        this.jf.setJMenuBar(jmb);

        initGUIBoard();

        this.jf.setSize(outerFrameDim);
        this.jf.setVisible(true);
        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void initGUIBoard() {
        this.bp = new boardPanel();
        this.jf.add(bp, BorderLayout.CENTER);
    }

    private class boardPanel extends JPanel {
        final List<tilePanel> boardTiles;

        boardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();

            tilePanel tp;

            for (int i = 0; i < board.numTiles; i++) {
                tp = new tilePanel(this, i);
                this.boardTiles.add(tp);
                add(tp);
            }

            setPreferredSize(new Dimension(400, 300));
            validate();
        }

        public void drawBoard(final board _chessBoard) {
            removeAll();
            for (final tilePanel tp: boardTiles) {
                tp.drawTile(_chessBoard);
                add(tp);
            }
            validate();
            repaint();
        }
    }

    private class tilePanel extends JPanel {
        private final int tileId;
        private final boardPanel bp;

        tilePanel(final boardPanel _bp, final int _tileId) {
            super(new GridBagLayout());
            this.tileId = _tileId;
            this.bp = _bp;
            setPreferredSize(new Dimension(10, 10));
            assignTileColor();
            assignTileIcon(chessBoard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (isRightMouseButton(e)) {
                        // UnSelect.
                        srcTile = null;
                        destTile = null;
                        humanMovedPiece = null;
                    } else if (isLeftMouseButton(e)) {
                        // Select.
                        if (srcTile == null) {
                            srcTile = chessBoard.getTile(tileId);
                            humanMovedPiece = srcTile.get();

                            if (humanMovedPiece == null) {
                                srcTile = null;
                            }
                        } else {
                            destTile = chessBoard.getTile(tileId);
                            // FIXME: fix stub.
                            final move m = move.moveFactory.initMove(chessBoard, srcTile.getTileCoord(), destTile.getTileCoord());
                            if (m != null) {
                                final moveTrans mt = chessBoard.plr().makeMove(m);
                                if (mt.getStatus() == moveStatus.done) {
                                    chessBoard = mt.getTransBoard();
                                    // TODO add the move that was made to the move log.
                                }

                                srcTile = null;
                                destTile = null;
                                humanMovedPiece = null;
                            }
                        }

                        SwingUtilities.invokeLater(() -> bp.drawBoard(chessBoard));
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {}

                @Override
                public void mouseReleased(MouseEvent e) {}

                @Override
                public void mouseEntered(MouseEvent e) {}

                @Override
                public void mouseExited(MouseEvent e) {}
            });

            validate();
        }

        public void drawTile(final board _board) {
            assignTileColor();
            assignTileIcon(_board);
            validate();
            repaint();
        }

        private void assignTileIcon(final board _b) {
            this.removeAll();
            if (_b.getTile(this.tileId).filled()) {
                try {
                    final BufferedImage bf = ImageIO.read(
                        new File(iconPath + _b.getTile(this.tileId).get().getTeam().toString().substring(0, 1)
                                 + _b.getTile(this.tileId).get().toString() + ".gif")
                    );

                    add(new JLabel(new ImageIcon(bf)));
                } catch (IOException ioe) {
                    System.out.println("Couldn't load piece icons... FAILING!");
                    exit(1);
                }
            }
        }

        private void assignTileColor() {
            if (board.eighthRank[this.tileId]
                || board.sixthRank[this.tileId]
                || board.fourthRank[this.tileId]
                || board.secondRank[this.tileId]) {
                setBackground(this.tileId % 2 == 0 ? lightColor : darkColor);
            } else if (board.seventhRank[this.tileId]
                       || board.fifthRank[this.tileId]
                       || board.thirdRank[this.tileId]
                       || board.firstRank[this.tileId]) {
                setBackground(this.tileId % 2 != 0 ? lightColor : darkColor);
            }
        }
    }
}
