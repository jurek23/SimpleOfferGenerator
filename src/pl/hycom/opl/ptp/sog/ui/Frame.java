package pl.hycom.opl.ptp.sog.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.filechooser.FileNameExtensionFilter;

import pl.hycom.opl.ptp.sog.SimpleOfferGenerator;
import pl.hycom.opl.ptp.sog.imports.AlfrescoImporter;
import pl.hycom.opl.ptp.sog.model.ParsedOffersPack;

/**
 * GUI aplikacji.
 * 
 * @author krystian.gorecki@hycom.pl
 *
 */
public class Frame {

    private JFrame frame;
    private JLabel lblLoadedOffers;
    private SimpleOfferGenerator simpleOfferGenerator = new SimpleOfferGenerator();
    private ParsedOffersPack parsedOffersPack;
    private JTextArea importLog;
    private JComboBox environmentComboBox;
    private JButton btnImport;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Frame window = new Frame();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Frame() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame("Simple Offer Generator");
        frame.setBounds(100, 100, 923, 298);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lbl1 = new JLabel("1. Ręcznie zapisz arkusz excela jako plik CSV (tekst rozdzielany średnikami)");

        JLabel lbl2 = new JLabel("2. Wybierz plik CSV");

        JButton btnChooseCSV = new JButton("Wybierz...");
        btnChooseCSV.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chooseCSVFile();
            }
        });

        JLabel lbl3 = new JLabel("3. Wybierz środowisko do importu:");

        environmentComboBox = new JComboBox();
        environmentComboBox.setModel(new DefaultComboBoxModel(new String[] { "opl-dev1.hycom.pl", "opl-dev2.hycom.pl",
                "opl-dev3.hycom.pl", "opl-dev4.hycom.pl" }));
        environmentComboBox.setSelectedIndex(1);
        environmentComboBox.setEditable(true);

        btnImport = new JButton("importuj do Alfresco");
        btnImport.setEnabled(false);
        btnImport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runImport();
            }
        });

        JLabel lbl6 = new JLabel("4.");

        lblLoadedOffers = new JLabel("");
        lblLoadedOffers.setForeground(new Color(107, 142, 35));

        importLog = new JTextArea();
        importLog.setRows(7);

        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // layout utworzony wizualnie za pomocą dodatku WindowBuilder do Eclipse - lepiej nie edytować tego ręcznie bo
        // dodatek potem tego nie rozpozna
        GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
        groupLayout
                .setHorizontalGroup(groupLayout
                        .createParallelGroup(Alignment.LEADING)
                        .addGroup(
                                groupLayout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                groupLayout
                                                        .createParallelGroup(Alignment.LEADING)
                                                        .addGroup(
                                                                groupLayout
                                                                        .createSequentialGroup()
                                                                        .addGroup(
                                                                                groupLayout
                                                                                        .createParallelGroup(
                                                                                                Alignment.LEADING)
                                                                                        .addComponent(lbl1)
                                                                                        .addGroup(
                                                                                                groupLayout
                                                                                                        .createSequentialGroup()
                                                                                                        .addComponent(
                                                                                                                lbl2)
                                                                                                        .addPreferredGap(
                                                                                                                ComponentPlacement.UNRELATED)
                                                                                                        .addComponent(
                                                                                                                btnChooseCSV)
                                                                                                        .addPreferredGap(
                                                                                                                ComponentPlacement.RELATED)
                                                                                                        .addComponent(
                                                                                                                lblLoadedOffers))
                                                                                        .addGroup(
                                                                                                groupLayout
                                                                                                        .createSequentialGroup()
                                                                                                        .addComponent(
                                                                                                                lbl3)
                                                                                                        .addPreferredGap(
                                                                                                                ComponentPlacement.RELATED)
                                                                                                        .addComponent(
                                                                                                                environmentComboBox,
                                                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                                                GroupLayout.PREFERRED_SIZE)))
                                                                        .addGap(31))
                                                        .addGroup(
                                                                groupLayout
                                                                        .createSequentialGroup()
                                                                        .addComponent(lbl6)
                                                                        .addPreferredGap(ComponentPlacement.RELATED)
                                                                        .addGroup(
                                                                                groupLayout
                                                                                        .createParallelGroup(
                                                                                                Alignment.LEADING)
                                                                                        .addComponent(
                                                                                                importLog,
                                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                                855, Short.MAX_VALUE)
                                                                                        .addComponent(btnImport))
                                                                        .addContainerGap()))));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
                groupLayout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbl1)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(
                                groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lbl2)
                                        .addComponent(btnChooseCSV).addComponent(lblLoadedOffers))
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(
                                groupLayout
                                        .createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lbl3)
                                        .addComponent(environmentComboBox, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(
                                groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lbl6)
                                        .addComponent(btnImport))
                        .addPreferredGap(ComponentPlacement.UNRELATED)
                        .addComponent(importLog, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE).addContainerGap(87, Short.MAX_VALUE)));
        frame.getContentPane().setLayout(groupLayout);
    }

    /**
     * Obsługa kliknięcia przycisku "importuj do Alfresco".
     */
    public void runImport() {
        new AlfrescoImporter().importOffers(environmentComboBox.getSelectedItem().toString(), parsedOffersPack,
                importLog);
    }

    /**
     * Obsługa przycisku "Wybierz CSV"
     */
    protected void chooseCSVFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Pliki CSV", "csv"));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            String csvFilePath = fileChooser.getSelectedFile().getPath();
            parsedOffersPack = simpleOfferGenerator.execute(csvFilePath);
            if (parsedOffersPack.getAllOffers().size() > 0) {
                lblLoadedOffers.setForeground(new Color(107, 142, 35));
                lblLoadedOffers.setText("Załadowano " + parsedOffersPack.getAllOffers().size() + " ofert.");
                //btnImport.setEnabled(true);
            } else {
                lblLoadedOffers.setForeground(Color.RED);
                lblLoadedOffers.setText("Błąd lub brak ofert w pliku.");
                btnImport.setEnabled(false);
            }
        } else if (result == JFileChooser.CANCEL_OPTION) {
            btnImport.setEnabled(false);
        } else if (result == JFileChooser.ERROR_OPTION) {
            JOptionPane.showMessageDialog(null, "Houston, mamy problem.");
            btnImport.setEnabled(false);
        }
    }
}
