package pl.hycom.opl.ptp.sog.imports;

import javax.swing.text.JTextComponent;

/**
 * Klasa buduje String z podsumowaniem statusów importu poszczególnych formularzy. Ten String zostanie pokazany w
 * komponencie tekstowym w GUI. Importy uruchamiane są w wątkach. Stąd potrzeba zebrania wszystkiego w jednym obiekcie i
 * odświeżenia interfejsu po każdej zmianie.
 * 
 * @author krystian.gorecki@hycom.pl
 *
 */
public class ImportStatus {
    private String[] prefix = { "neo-ins", "neo-mod", "neo-ins-biz", "neo-mod-biz", "3p-ins", "3p-mod" };
    private String[] status = { "", "", "", "", "", "" };

    @Override
    public String toString() {
        return prefix[0] + "\t" + status[0] + "\n" + prefix[1] + "\t" + status[1] + "\n" + prefix[2] + "\t" + status[2]
                + "\n" + prefix[3] + "\t" + status[3] + "\n" + prefix[4] + "\t" + status[4] + "\n" + prefix[5] + "\t"
                + status[5] + "\n";
    }

    /**
     * Na pozycji i dopisuje string msg i pokazuje w GUI w komponencie log. Pomiędzy kolejnymi tekstami dopisuje "->".
     * 
     * @param i
     * @param msg
     * @param log
     */
    public synchronized void addStatusAndUpdate(int i, String msg, JTextComponent log) {
        this.status[i] = this.status[i] + " -> " + msg;
        log.setText(this.toString());
    }

    /**
     * Na pozycji i dopisuje string msg i pokazuje w GUI w komponencie log.
     * 
     * @param i
     * @param msg
     * @param log
     */
    public synchronized void addShortStatusAndUpdate(int i, String msg, JTextComponent log) {
        this.status[i] = this.status[i] + msg;
        log.setText(this.toString());
    }

    public void resetStatus() {
        status = new String[] { "", "", "", "", "", "" };
    }
}
