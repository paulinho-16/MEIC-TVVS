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
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;


@SuppressWarnings("serial")
public class ColorCellRenderer extends JLabel implements TableCellRenderer {

    public ColorCellRenderer() {
        this.setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value, boolean isSelected, boolean hasFocus, int row,
                                                   int column) {

        if (value != null) {
            this.setBackground((Color) value);
            this.setBorder(new LineBorder(Color.WHITE, 2));
        } else {
            ProjectTableModel tstm = (ProjectTableModel) table.getModel();
            int modelRow = table.convertRowIndexToModel(row);
            Project prj = tstm.getProjectAt(modelRow);

            if (prj.isRunning()) {
                this.setBackground(CustomCellRenderer.COLOR_RUNNING);
            } else {
                if (isSelected) {
                    this.setBackground(table.getSelectionBackground());
                } else {
                    this.setBackground(table.getBackground());
                }
            }

            this.setBorder(null);
        }

        return this;
    }
}
