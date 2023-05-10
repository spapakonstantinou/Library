import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Library {

    public static void main (String[] args) {

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {

                    JTabbedPane tabs = new JTabbedPane();
                    tabs.add("HOME", new Home());
                    tabs.add("CUSTOMERS", new Customers());
                    tabs.add("BOOKS", new Books());
                    tabs.add("RENDING", new Rending());

                    JFrame frame = new JFrame("LIBRARY");
                    frame.add(tabs);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(true);
                    frame.pack();

                }
            });

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

}
