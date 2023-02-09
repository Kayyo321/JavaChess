package com.chess.engine.driver;

import com.chess.engine.core.board;
import com.chess.engine.core.move;
import com.chess.engine.core.team;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class history extends JPanel {
    private final dataModel model;
    private final JScrollPane scrollPane;

    private static final Dimension historyDim = new Dimension(100, 400);

    history() {
        this.setLayout(new BorderLayout());
        this.model = new dataModel();

        final JTable jt = new JTable(this.model);
        jt.setRowHeight(15);
        this.scrollPane = new JScrollPane();
        this.scrollPane.setColumnHeaderView(jt.getTableHeader());
        this.add(scrollPane, BorderLayout.CENTER);
        this.setPreferredSize(historyDim);
        this.setVisible(true);
    }

    private String calcCheckNMateHash(final board _b) {
        if (_b.plr().inCheckMate()) {
            return "#";
        } else if (_b.plr().inCheck()) {
            return "+";
        } else {
            return "";
        }
    }

    public void redo(final board _b, final table.moveLog _ml) {
        int cr = 0;

        this.model.clear();

        String moveText;

        for (final move m: _ml.moves()) {
            moveText = m.toString();
            if (m.getMovedPiece().getTeam() == team.white) {
                this.model.setValueAt(moveText, cr, 0);
            } else {
                this.model.setValueAt(moveText, cr, 1);
                cr++;
            }
        }

        if (_ml.moves().size() > 0) {
            final move last = _ml.moves().get(_ml.moves().size()-1);
            final String mt = last.toString();
            if (last.getMovedPiece().getTeam() == team.white) {
                this.model.setValueAt(mt + calcCheckNMateHash(_b), cr - 1, 1);
            }
        }

        final JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    private static class dataModel extends DefaultTableModel implements com.chess.engine.driver.dataModel {
        private final List<row> values;
        private static final String[] names = {"White", "Black"};

        private dataModel() {
            this.values = new ArrayList<>();
            setRowCount(0);
        }

        @Override
        public int getRowCount() {
            return (this.values == null) ? 0 : this.values.size();
        }

        @Override
        public int getColumnCount() {
            return names.length;
        }

        @Override
        public Object getValueAt(final int _row, final int _column) {
            final row cr = this.values.get(_row);
            if (_column == 0) {
                return cr.getWhiteMove();
            } else {
                return cr.getBlackMove();
            }
        }

        @Override
        public void setValueAt(final Object _aValue, final int _row, final int _column) {
            final row cr;

            if (this.values.size() <= _row) {
                cr = new row();
                this.values.add(cr);
            } else {
                cr = this.values.get(_row);
            }

            if (_column == 0) {
                cr.setWhiteMove((String)_aValue);
            } else {
                cr.setBlackMove((String)_aValue);
                fireTableCellUpdated(_row, _column);
            }
        }

        @Override
        public Class<?> getColumnClass(final int _column) {
            return move.class;
        }

        @Override
        public String getColumnName(final int _column) {
            return names[_column];
        }

        public void clear() {
            this.values.clear();
        }
    }

    private static class row {
        private String whiteMove, blackMove;

        row() {

        }

        public String getWhiteMove() {
            return this.whiteMove;
        }

        public String getBlackMove() {
            return this.blackMove;
        }

        public void setWhiteMove(final String _move) {
            this.whiteMove = _move;
        }

        public void setBlackMove(final String _move) {
            this.blackMove = _move;
        }
    }
}
