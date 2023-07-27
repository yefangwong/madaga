package dhf.system.chat;

import com.dhf.hrsys.service.impl.Term;
import com.dhf.hrsys.service.impl.WordSegmentationService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CKIPTest {
    private WordSegmentationService service;

    @Before
    public void init() {

        //TODO You should change this before you run a test.
        this.service = new CKIPServiceImpl("140.109.20.151", 1501, "", "");

        String testString = "台新金控12月3日將召開股東臨時會進行董監改選。";

        this.service.setRawText(testString);

        System.out.println("=== Getting start test ===");
    }

    @Test
    public void testGetTerm() throws Exception {
        System.out.println("=== testGetTerm() ===");
        this.service.send();
        assertNotNull(this.service.getReturnText());
        for (Term t : this.service.getTerm()) {
            System.out.println(t.getTerm() + "\t" + t.getTag());
        }

    }

    @Test
    public void testSend() throws Exception {
        System.out.println("=== testSend() ===");
        this.service.send();
        assertNotNull(this.service.getReturnText());
        System.out.println("send OK");
    }

    @Test
    public void testgetReturnText() throws Exception {
        System.out.println("=== testgetReturnText() ===");
        this.service.send();
        assertNotNull(this.service.getReturnText());
        System.out.println(this.service.getReturnText());
    }
}
