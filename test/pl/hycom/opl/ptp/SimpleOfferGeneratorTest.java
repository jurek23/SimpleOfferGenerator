package pl.hycom.opl.ptp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import pl.hycom.opl.ptp.sog.model.SimpleOffer;
import pl.hycom.opl.ptp.sog.util.CSVHelper;
import pl.hycom.opl.ptp.sog.model.Process;

public class SimpleOfferGeneratorTest {

    /**
     * Test sprawdzania poprawnego formatu linii CSV.
     */
    @Test
    public void testParseLine() {
        List<String> parsedLine = new CSVHelper()
                .parseLine("110;n/d;NIE;Neostrada;akwizycja;xDSL;oba;80;PNEOITG;TABLET;n/d;n/d;n/d;NEOGEOEDF-PNEOITG24,NEOIINST-PNEOITG24,NEOIWOFB1-NEO_GENERIC_NO_PROMOTION,NEOZNWUU-NEO_GENERIC_NO_PROMOTION");
        assertEquals(parsedLine.size(), 14);
        assertEquals("110", parsedLine.get(0));
        assertEquals("n/d", parsedLine.get(1));
        assertEquals("NIE", parsedLine.get(2));
        assertEquals("Neostrada", parsedLine.get(3));
        assertEquals(Process.AKWIZYCJA.name().toLowerCase(), parsedLine.get(4));
        assertEquals("xDSL", parsedLine.get(5));
        assertEquals("oba", parsedLine.get(6));
        assertEquals("80", parsedLine.get(7));
        assertEquals("PNEOITG", parsedLine.get(8));
        assertEquals("TABLET", parsedLine.get(9));
        assertEquals("n/d", parsedLine.get(10));
        assertEquals("n/d", parsedLine.get(11));
        assertEquals("n/d", parsedLine.get(12));
        assertEquals(
                "NEOGEOEDF-PNEOITG24,NEOIINST-PNEOITG24,NEOIWOFB1-NEO_GENERIC_NO_PROMOTION,NEOZNWUU-NEO_GENERIC_NO_PROMOTION",
                parsedLine.get(13));

    }

    /**
     * Test parsowania błędnej linii z CSV.
     */
    @Test
    public void testParseLineInvalidProcess() {
        List<String> parsedLine = new CSVHelper()
                .parseLine("110;n/d;NIE;Neostrada;akwizycjaXXX;xDSL;oba;80;PNEOITG;TABLET;n/d;n/d;n/d;NEOGEOEDF-PNEOITG24,NEOIINST-PNEOITG24,NEOIWOFB1-NEO_GENERIC_NO_PROMOTION,NEOZNWUU-NEO_GENERIC_NO_PROMOTION");
        SimpleOffer s = new SimpleOffer(parsedLine);
        assertEquals(null, s.getProcess());
    }

    /**
     * Test konstruktora SimpleOffer.
     */
    @Test
    public void testSimpleOfferConstructor() {
        List<String> parsedLine;
        try {
            parsedLine = new CSVHelper()
                    .parseLine("110;n/d;NIE;Neostrada;akwizycja;xDSL;oba;80;PNEOITG;TABLET;n/d;n/d;n/d;NEOGEOEDF-PNEOITG24,NEOIINST-PNEOITG24,NEOIWOFB1-NEO_GENERIC_NO_PROMOTION,NEOZNWUU-NEO_GENERIC_NO_PROMOTION");
            new SimpleOffer(parsedLine);
        } catch (Exception e) {
            e.printStackTrace();
            fail("testSimpleOfferConstructor failed.");
        }
    }

}
