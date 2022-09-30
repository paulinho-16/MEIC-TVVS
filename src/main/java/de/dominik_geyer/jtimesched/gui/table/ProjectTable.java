/* jTimeSched - A simple and lightweight time tracking tool
 * Copyright (C) 2010-2012 Dominik D. Geyer <dominik.geyer@gmail.com>
 * See LICENSE.txt for details.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.dominik_geyer.jtimesched.gui.table;

import de.dominik_geyer.jtimesched.project.Project;
import de.dominik_geyer.jtimesched.project.ProjectTableModel;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


@SuppressWarnings("serial")
public class ProjectTable extends JTable {
    private static final int ICON_SIZE = 16;
    private static final int COLUMN_ICON_PADDING = 6;
    private static final int COLUMN_ICON_SIZE = ICON_SIZE + COLUMN_ICON_PADDING;

    private JFrame parentFrame;
    private String highlightString = "";

    public ProjectTable(JFrame parentFrame, ProjectTableModel ptm) {
        super(ptm);

        this.parentFrame = parentFrame;

        this.setFillsViewportHeight(true);
        this.setShowGrid(true);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setAutoCreateRowSorter(true);
        this.setRowHeight(ProjectTable.COLUMN_ICON_SIZE);
        //this.tblSched.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE); // not needed?
        this.getTableHeader().setReorderingAllowed(false);

        // set a custom default cell-renderer 
        //TableCellRenderer defaultRenderer = this.getDefaultRenderer(Object.class);
        //this.setDefaultRenderer(Object.class, new CustomCellRenderer(/*defaultRenderer*/));


        // set default sort-column
        List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
        sortKeys.add(new RowSorter.SortKey(ProjectTableModel.COLUMN_CREATED, SortOrder.ASCENDING));
        this.getRowSorter().setSortKeys(sortKeys);


        // determine minimum size of a checkbox without text
        final Dimension chkSize = (new JCheckBox()).getMinimumSize();


        // define and set column properties
        Object[][] columnProps = new Object[][]{
            /*
             * {
             *     column index,
             *     preferred width,
             *     minimum width,
             *     maximum width,
             *     cell renderer,
             *     cell editor
             * }
             */
            {
                ProjectTableModel.COLUMN_CHECK,
                -1,
                chkSize.width + ProjectTable.COLUMN_ICON_PADDING,
                chkSize.width + ProjectTable.COLUMN_ICON_PADDING,
                new CheckCellRenderer(),
                null
            },
            {
                ProjectTableModel.COLUMN_TITLE,
                200,
                100,
                -1,
                new CustomCellRenderer(),
                null
            },
            {
                ProjectTableModel.COLUMN_COLOR,
                -1,
                ProjectTable.COLUMN_ICON_SIZE,
                ProjectTable.COLUMN_ICON_SIZE,
                new ColorCellRenderer(),
                new ColorCellEditor(this.parentFrame)
            },
            {
                ProjectTableModel.COLUMN_CREATED,
                -1,
                80,
                80,
                new CustomCellRenderer(),
                new DateCellEditor()
            },
            {
                ProjectTableModel.COLUMN_TIMEOVERALL,
                95,
                60,
                95,
                new TimeCellRenderer(),
                new TimeCellEditor()
            },
            {
                ProjectTableModel.COLUMN_TIMETODAY,
                95,
                60,
                95,
                new TimeCellRenderer(),
                new TimeCellEditor()
            },
            {
                ProjectTableModel.COLUMN_ACTION_DELETE,
                -1,
                ProjectTable.COLUMN_ICON_SIZE,
                ProjectTable.COLUMN_ICON_SIZE,
                new CustomCellRenderer(),
                null
            },
            {
                ProjectTableModel.COLUMN_ACTION_STARTPAUSE,
                -1,
                ProjectTable.COLUMN_ICON_SIZE,
                ProjectTable.COLUMN_ICON_SIZE,
                new CustomCellRenderer(),
                null
            },
        };


        TableColumnModel tcm = this.getColumnModel();
        for (Object[] cp : columnProps) {
            TableColumn tc = tcm.getColumn((Integer) cp[0]);

            if ((Integer) cp[1] > 0) {
                tc.setPreferredWidth((Integer) cp[1]);
            }

            if ((Integer) cp[2] > 0) {
                tc.setMinWidth((Integer) cp[2]);
            }

            if ((Integer) cp[3] > 0) {
                tc.setMaxWidth((Integer) cp[3]);
            }

            if ((TableCellRenderer) cp[4] != null) {
                tc.setCellRenderer((TableCellRenderer) cp[4]);
            }

            if ((TableCellEditor) cp[5] != null) {
                tc.setCellEditor((TableCellEditor) cp[5]);
            }
        }
    }

    public void setHighlightString(String highlightString) {
        this.highlightString = highlightString;
    }

    public boolean isHighlightRow(int row) {
        ProjectTableModel ptm = (ProjectTableModel) this.getModel();

        boolean isHighlight = false;

        Project p = ptm.getProjectAt(row);
        String strPattern = this.highlightString.trim();

        if (!strPattern.isEmpty()) {
            //isHighlight = Pattern.matches(this.highlightString, p.getTitle());

            Pattern pattern = Pattern.compile(Pattern.quote(strPattern),
                Pattern.CASE_INSENSITIVE);
            isHighlight = pattern.matcher(p.getTitle()).find();
        }

        return isHighlight;
    }
}
