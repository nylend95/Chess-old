package test.java;

import main.java.model.Square;
import org.junit.Assert;

import java.util.ArrayList;

/**
 * Created by mikke on 16-Feb-17.
 */
class Utils {

    static void testMoves(ArrayList<Square> validMoves, ArrayList<Square> generatedValidMoves){
        for (Square gen_square : generatedValidMoves) {
            boolean exists = false;
            for (Square valid_square : validMoves) {
                if (gen_square.getColumn() == valid_square.getColumn() && gen_square.getRow() == valid_square.getRow()) {
                    exists = true;
                }
            }
            if(!exists){
                System.out.println(gen_square);
            }
            Assert.assertTrue(exists);
        }

        Assert.assertEquals(validMoves.size(), generatedValidMoves.size());
    }
}
