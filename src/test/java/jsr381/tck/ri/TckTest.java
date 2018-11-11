package jsr381.tck.ri;

import jsr381.tck.TCKRunner;
import org.testng.annotations.Test;

/**
 * @author Kevin Berendsen
 */
public class TckTest {

    /**
     * Run the TCK tests
     */
    @Test
    public void startRunner(){
        TCKRunner.main(new String[0]);
    }
}

