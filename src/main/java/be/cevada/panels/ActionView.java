package be.cevada.panels;

public interface ActionView {
    void clearActions();

    void addButton(String label, Runnable action);

    void focusFirst();
}
