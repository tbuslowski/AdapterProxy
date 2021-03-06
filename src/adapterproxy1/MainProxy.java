package adapterproxy1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

/**
 *
 * @author A5US
 */
public class MainProxy {

    public static void main(String[] args) {
        final Baza dane = new Baza();
        final AdapterTableModel adapter = new AdapterTableModel(0);

        final JFrame frame = new JFrame("Zadanie 4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JSplitPane splitPane = new JSplitPane();

        final JList list = new JList(dane);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(BorderFactory.createTitledBorder(" Tablice: "));
        splitPane.setLeftComponent(scrollPane);

        JTable table = new JTable(adapter);
        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(" Zawartość: "));
        splitPane.setRightComponent(scrollPane);

        frame.getContentPane().add(splitPane);

        JMenuBar bar = new JMenuBar();
        JButton add = new JButton("Dodaj tablicę");
        JButton del = new JButton("Usuń tablicę");
        JButton cop = new JButton("Skopiuj tablicę");
        bar.add(add);
        bar.add(del);
        bar.add(cop);

        frame.setJMenuBar(bar);

        frame.setSize(600, 450);
        frame.setVisible(true);

        splitPane.setDividerLocation(0.5);

        add.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String value = JOptionPane.showInputDialog(frame,
                        "Podaj rozmiar tablicy",
                        "Dodaj",
                        JOptionPane.INFORMATION_MESSAGE);
                try {
                    int size = Integer.parseInt(value);
                    //Tworzenie RealData
                    dane.add(new ProxyVirtualData(size));
                } catch (Exception ex) {
                };
            }
        });
        del.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int idx = list.getSelectedIndex();
                try {
                    dane.remove(idx);
                    adapter.clean();
                } catch (Exception ex) {
                };
            }
        });
        cop.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = list.getSelectedIndex();
                try {
                    dane.add(((ProxyVirtualData) (dane.getElementAt(idx))).kopia());
                } catch (Exception ex) {
                    System.out.println("Nie mozna stworzyc kopii kopii, ex: " + ex.toString());
                };

            }
        });

        // zmiana wyboru na liście powoduje odświeżenie tabeli
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int idx = list.getSelectedIndex();
                if (idx >= 0) {
                    //obiekt adaptera staje sie tym na co klikam z lewej strony -> po prawej pojawia sie zawartosc
                    adapter.setData((Data) dane.getElementAt(idx));

                }
            }
        });
    }
}
