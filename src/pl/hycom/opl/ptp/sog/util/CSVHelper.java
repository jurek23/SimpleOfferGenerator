package pl.hycom.opl.ptp.sog.util;

import java.util.ArrayList;
import java.util.List;

public class CSVHelper {
    private static final char SEPARATOR = ';';

    public List<String> parseLine(String r) {
        char[] chars = r.toCharArray();
        List<String> store = new ArrayList<String>();
        StringBuilder currentValue = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (ch == '\r') {
                continue;
            }
            if (ch < 0) {
                return null;
            }
            boolean inquotes = false;
            boolean started = false;
            while (ch >= 0) {
                if (inquotes) {
                    started = true;
                    if (ch == '"') {
                        inquotes = false;
                    } else {
                        currentValue.append(ch);
                    }
                } else {
                    if (ch == '"') {
                        inquotes = true;
                        if (started) {
                            // if this is the second quote in a value, add a
                            // quote
                            // this is for the double quote in the middle of a
                            // value
                            currentValue.append('"');
                        }
                    } else if (ch == SEPARATOR) {
                        store.add(currentValue.toString());
                        currentValue = new StringBuilder();
                        started = false;
                    } else if (ch == '\r') {
                        // ignore LF characters
                    } else if (ch == '\n') {
                        // end of a line, break out
                        break;
                    } else {
                        currentValue.append((char) ch);
                    }
                }
                if (i == chars.length - 1) {
                    break;
                }
                ch = chars[++i];
            }
            store.add(currentValue.toString());
        }
        return store;
    }
}