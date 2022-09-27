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

import de.dominik_geyer.jtimesched.project.ProjectTime;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class TimeCellComponent extends JLabel {
    private static final Color BAR_COLOR = new Color(80, 200, 80);
    private static final Color BAR_COLOR_OVERDUE = new Color(200, 50, 50);
    private static final int BAR_HEIGHT = 3;
    private static final int BAR_THRESHOLD = 1;

    private static final Color HASQUOTA_COLOR = new Color(50, 50, 200);
    private static final int HASQUOTA_HEIGHT = 3;

    private int time;
    private int quota;

    public TimeCellComponent(int time, int quota) {
        this.setOpaque(true);

        this.time = time;
        this.quota = quota;

        String text = ProjectTime.formatSeconds(time);
        this.setHorizontalAlignment(SwingConstants.RIGHT);
        this.setText(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.quota > 0) {
            Color color = (this.time < this.quota)
                ? TimeCellComponent.BAR_COLOR
                : TimeCellComponent.BAR_COLOR_OVERDUE;
            int width = (this.time < this.quota)
                ? this.getWidth() * this.time / this.quota
                : this.getWidth();

            if (width > TimeCellComponent.BAR_THRESHOLD) {
                g.setColor(color);
                g.fillRect(0, 0, width, TimeCellComponent.BAR_HEIGHT);
            } else {
                // indicate quota if bar not yet visible
                g.setColor(TimeCellComponent.HASQUOTA_COLOR);
                g.fillRect(0, this.getHeight() - TimeCellComponent.HASQUOTA_HEIGHT, TimeCellComponent.HASQUOTA_HEIGHT, TimeCellComponent.HASQUOTA_HEIGHT);
            }
        }
    }
}
