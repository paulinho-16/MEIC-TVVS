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

import java.awt.Color;
import java.util.Date;


public class Project {
    private String title;
    private String notes;

    private Date timeCreated;
    private Color color;
    private boolean checked;

    private int secondsOverall;
    private int secondsToday;

    private int quotaOverall;
    private int quotaToday;

    private boolean running;
    private Date timeStart;

    public Project() {
        this("project");
    }

    public Project(String name) {
        this.title = name;
        this.notes = "";
        this.color = null;

        this.timeStart = new Date();
        this.timeCreated = new Date();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public boolean isRunning() {
        return this.running;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    protected int getElapsedSeconds() throws ProjectException {
        if (!this.isRunning())
            throw new ProjectException("Timer not running");

        Date currentTime = new Date();
        return (int) ((currentTime.getTime() - this.timeStart.getTime()) / 1000);
    }

    public void start() throws ProjectException {
        if (this.isRunning())
            throw new ProjectException("Timer already running");

        // save current time
        this.timeStart = new Date();

        this.running = true;
    }

    public void pause() throws ProjectException {
        if (!this.isRunning())
            throw new ProjectException("Timer not running");

        int secondsElapsed = this.getElapsedSeconds();
        this.secondsOverall += secondsElapsed;
        this.secondsToday += secondsElapsed;

        this.running = false;
    }

    public void toggle() {
        try {
            if (this.isRunning()) {
                this.pause();
            } else {
                this.start();
            }
        } catch (ProjectException e) {
            // I don't care... eat it
        }
    }

    public int getSecondsToday() {
        int seconds = this.secondsToday;

        if (this.isRunning())
            try {
                seconds += this.getElapsedSeconds();
            } catch (ProjectException e) {
                e.printStackTrace();
            }

        return seconds;
    }

    public int getSecondsOverall() {
        int seconds = this.secondsOverall;

        if (this.isRunning())
            try {
                seconds += this.getElapsedSeconds();
            } catch (ProjectException e) {
                e.printStackTrace();
            }

        return seconds;
    }

    public void setSecondsOverall(int secondsOverall) {
        if (secondsOverall < 0)
            secondsOverall = 0;

        this.secondsOverall = secondsOverall;
    }

    public void setSecondsToday(int secondsToday) {
        if (secondsToday < 0)
            secondsToday = 0;

        this.secondsToday = secondsToday;
    }

    public void adjustSecondsToday(int secondsToday) {
        if (secondsToday < 0)
            secondsToday = 0;

        int secondsDelta = secondsToday - this.secondsToday;

        this.setSecondsOverall(this.getSecondsOverall() + secondsDelta);
        this.setSecondsToday(secondsToday);
    }

    public void resetToday() {
        this.secondsToday = 0;
        this.quotaToday = 0;

        // reset time-started
        this.timeStart = new Date();
    }

    public int getQuotaOverall() {
        return quotaOverall;
    }

    public void setQuotaOverall(int quotaOverall) {
        this.quotaOverall = quotaOverall;
    }

    public int getQuotaToday() {
        return quotaToday;
    }

    public void setQuotaToday(int quotaToday) {
        this.quotaToday = quotaToday;
    }

    @Override
    public String toString() {
        return String.format("Project [title=%s, running=%s, secondsOverall=%d, secondsToday=%d, checked=%s]",
            title,
            (running) ? "yes" : "no",
            secondsOverall, secondsToday,
            (checked) ? "yes" : "no");
    }
}
