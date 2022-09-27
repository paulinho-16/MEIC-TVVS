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

package de.dominik_geyer.jtimesched.project;

import de.dominik_geyer.jtimesched.JTimeSchedApp;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.AbstractTableModel;


@SuppressWarnings("serial")
public class ProjectTableModel extends AbstractTableModel {
    private static final int COLUMN_COUNT = 8;

    public static final int COLUMN_ACTION_DELETE = 0;
    public static final int COLUMN_CHECK = 1;
    public static final int COLUMN_TITLE = 2;
    public static final int COLUMN_COLOR = 3;
    public static final int COLUMN_CREATED = 4;
    public static final int COLUMN_TIMEOVERALL = 5;
    public static final int COLUMN_TIMETODAY = 6;
    public static final int COLUMN_ACTION_STARTPAUSE = 7;

    private String[] columnNames = new String[]{
        "", "", "Title", "", "Created", "Time Overall", "Time Today", "",
    };


    private ArrayList<Project> arPrj;

    public ProjectTableModel(ArrayList<Project> arPrj) {
        this.arPrj = arPrj;
    }

    @Override
    public int getColumnCount() {
        return ProjectTableModel.COLUMN_COUNT;
    }

    @Override
    public int getRowCount() {
        return arPrj.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        Object o;

        Project prj = arPrj.get(row);

        switch (column) {
            case ProjectTableModel.COLUMN_TITLE:
                o = prj.getTitle();
                break;
            case ProjectTableModel.COLUMN_CHECK:
                o = (prj.isChecked()) ? new Boolean(true) : new Boolean(false);
                break;
            case ProjectTableModel.COLUMN_COLOR:
                o = prj.getColor();
                break;
            case ProjectTableModel.COLUMN_CREATED:
                o = prj.getTimeCreated();
                break;
            case ProjectTableModel.COLUMN_TIMEOVERALL:
                o = new Integer(prj.getSecondsOverall());
                break;
            case ProjectTableModel.COLUMN_TIMETODAY:
                o = new Integer(prj.getSecondsToday());
                break;
            case ProjectTableModel.COLUMN_ACTION_DELETE:
            case ProjectTableModel.COLUMN_ACTION_STARTPAUSE:
                o = (prj.isRunning()) ? new Boolean(true) : new Boolean(false);
                break;
            default:
                o = "wtf?";
        }


        return o;
    }

    public Project getProjectAt(int row) {
        return this.arPrj.get(row);
    }

    @Override
    public String getColumnName(int column) {
        return this.columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case ProjectTableModel.COLUMN_COLOR:
                return Color.class;
            case ProjectTableModel.COLUMN_CREATED:
                return Date.class;
            case ProjectTableModel.COLUMN_TIMEOVERALL:
            case ProjectTableModel.COLUMN_TIMETODAY:
                return Integer.class;
            case ProjectTableModel.COLUMN_CHECK:
            case ProjectTableModel.COLUMN_ACTION_DELETE:
            case ProjectTableModel.COLUMN_ACTION_STARTPAUSE:
                return Boolean.class;
            default:
                return String.class;
            //return getValueAt(0, column).getClass();   // WARNING: sorter would throw exception!
        }
    }


    @Override
    public boolean isCellEditable(int row, int column) {
        Project prj = this.getProjectAt(row);

        switch (column) {
            case ProjectTableModel.COLUMN_CHECK:
            case ProjectTableModel.COLUMN_TITLE:
            case ProjectTableModel.COLUMN_COLOR:
            case ProjectTableModel.COLUMN_CREATED:
                return true;
            case ProjectTableModel.COLUMN_TIMEOVERALL:
            case ProjectTableModel.COLUMN_TIMETODAY:
                // running tasks cannot be edited
                return (prj.isRunning() ? false : true);
            default:
                return false;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        Project prj = this.getProjectAt(row);

        switch (column) {
            case ProjectTableModel.COLUMN_CHECK:
                Boolean checked = (Boolean) value;
                JTimeSchedApp.getLogger().info(String.format("%s check for project '%s'",
                    (checked) ? "Set" : "Unset",
                    prj.getTitle()));
                prj.setChecked(checked);
                break;
            case ProjectTableModel.COLUMN_TITLE:
                String title = (String) value;
                JTimeSchedApp.getLogger().info(String.format("Renamed project '%s' to '%s'",
                    prj.getTitle(),
                    title));
                prj.setTitle(title);
                break;
            case ProjectTableModel.COLUMN_COLOR:
                prj.setColor((Color) value);
                break;
            case ProjectTableModel.COLUMN_CREATED:
                JTimeSchedApp.getLogger().info(String.format("Manually set create date for project '%s' from %s to %s",
                    prj.getTitle(),
                    ProjectTime.formatDate(prj.getTimeCreated()),
                    ProjectTime.formatDate((Date) value)));
                prj.setTimeCreated((Date) value);
                break;
            case ProjectTableModel.COLUMN_TIMEOVERALL:
            case ProjectTableModel.COLUMN_TIMETODAY:
                int oldSeconds = (column == ProjectTableModel.COLUMN_TIMEOVERALL) ? prj.getSecondsOverall() : prj.getSecondsToday();
                int newSeconds = ((Integer) value).intValue();
                JTimeSchedApp.getLogger().info(String.format("Manually set time %s for project '%s' from %s to %s",
                    (column == ProjectTableModel.COLUMN_TIMEOVERALL) ? "overall" : "today",
                    prj.getTitle(),
                    ProjectTime.formatSeconds(oldSeconds),
                    ProjectTime.formatSeconds(newSeconds)));

                if (column == ProjectTableModel.COLUMN_TIMEOVERALL)
                    prj.setSecondsOverall(newSeconds);
                else
                    prj.adjustSecondsToday(newSeconds);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + column);
        }

        this.fireTableRowsUpdated(row, row);
    }

    public void addProject(Project p) {
        this.arPrj.add(p);
        this.fireTableRowsInserted(this.getRowCount() - 1, this.getRowCount() - 1);

        JTimeSchedApp.getLogger().info("Created new project");
    }

    public void removeProject(int row) {
        Project p = this.getProjectAt(row);
        this.arPrj.remove(p);
        this.fireTableRowsDeleted(row, row);

        JTimeSchedApp.getLogger().info(String.format("Removed project '%s' (time overall: %s, time today: %s)",
            p.getTitle(),
            ProjectTime.formatSeconds(p.getSecondsOverall()),
            ProjectTime.formatSeconds(p.getSecondsToday())));
    }
}
