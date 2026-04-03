package be.cevada.panels;

import com.googlecode.lanterna.TextColor;

public interface WorldView {
    void setLocation(String location);

    void setDescription(String description);

    void addLogEntry(String text, TextColor color);

    void clearLog();
}

