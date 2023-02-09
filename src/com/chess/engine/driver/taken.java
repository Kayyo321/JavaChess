package com.chess.engine.driver;

import com.chess.engine.core.move;
import com.chess.engine.core.piece;
import com.chess.engine.core.team;
import com.google.common.primitives.Ints;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class taken extends JPanel {
    private static final EtchedBorder panelBorder = new EtchedBorder(EtchedBorder.RAISED);
    private static final Color panelColor = Color.decode("0xFD5E6");
    private static final Dimension takenDim = new Dimension(40, 80);

    private final JPanel north, south;

    public taken() {
        super(new BorderLayout());
        setBackground(panelColor);
        setBorder(panelBorder);

        this.north = new JPanel(new GridLayout(8, 2));
        this.south = new JPanel(new GridLayout(8, 2));

        this.north.setBackground(panelColor);
        this.south.setBackground(panelColor);

        add(this.north);
        add(this.south);

        setPreferredSize(takenDim);
    }

    public void redo(final table.moveLog _ml) {
        this.south.removeAll();
        this.north.removeAll();

        final List<piece> whiteTaken = new ArrayList<>();
        final List<piece> blackTaken = new ArrayList<>();

        piece takenPiece;

        for (final move m: _ml.moves()) {
            if (m.isAttack()) {
                takenPiece = m.getAttackedPiece();

                if (takenPiece.getTeam() == team.white) {
                    whiteTaken.add(takenPiece);
                } else {
                    blackTaken.add(takenPiece);
                }
            }
        }

        whiteTaken.sort(new Comparator<piece>() {
            @Override
            public int compare(piece o1, piece o2) {
                return Ints.compare(o1.getPieceVal(), o2.getPieceVal());
            }
        });

        blackTaken.sort(new Comparator<piece>() {
            @Override
            public int compare(piece o1, piece o2) {
                return Ints.compare(o1.getPieceVal(), o2.getPieceVal());
            }
        });

        for (final piece tp: whiteTaken) {
            try {
                final BufferedImage bi = ImageIO.read(new File("imgs/"
                + tp.getTeam().toString().substring(0, 1) + "" + tp.toString()));

                final ImageIcon icon = new ImageIcon(bi);
                final JLabel img = new JLabel();
                this.south.add(img);
            } catch (final IOException ioe) {
                System.out.println(ioe.toString());
            }
        }

        for (final piece tp: blackTaken) {
            try {
                final BufferedImage bi = ImageIO.read(new File("imgs/"
                        + tp.getTeam().toString().substring(0, 1) + "" + tp.toString()));

                final ImageIcon icon = new ImageIcon(bi);
                final JLabel img = new JLabel();
                this.south.add(img);
            } catch (final IOException ioe) {
                System.out.println(ioe.toString());
            }
        }

        validate();
    }
}
