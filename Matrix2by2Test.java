import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Black-box unit tests for the public interface of Matrix2by2,
 * including multiply(Vector1by2, Matrix2by2).
 *
 * Expected answers were worked out by hand.
 *
 * @author Abhinav and Yash
 */
public class Matrix2by2Test
{
    private static final double DELTA = 1e-9;

    /** | 1 2 |
     *  | 3 4 |  */
    private static final Matrix2by2 M = new Matrix2by2(1, 2, 3, 4);

    // ---------------- constructor, get, getRow, getColumn ----------------

    @Test
    public void getReturnsEntriesRowByRow()
    {
        assertEquals(1.0, M.get(0, 0), DELTA);
        assertEquals(2.0, M.get(0, 1), DELTA);
        assertEquals(3.0, M.get(1, 0), DELTA);
        assertEquals(4.0, M.get(1, 1), DELTA);
    }

    @Test
    public void getRowAndGetColumn()
    {
        assertEquals(new Vector1by2(1, 2), M.getRow(0));
        assertEquals(new Vector1by2(3, 4), M.getRow(1));
        assertEquals(new Vector1by2(1, 3), M.getColumn(0));
        assertEquals(new Vector1by2(2, 4), M.getColumn(1));
    }

    @Test
    public void badIndexThrows()
    {
        assertThrows(IndexOutOfBoundsException.class, () -> M.get(2, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> M.getRow(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> M.getColumn(5));
    }

    // ---------------- multiply(Vector1by2, Matrix2by2) ----------------

    @Test
    public void multiplyVectorByMatrix()
    {
        // [1, 2] * | 1 2 | = [1*1 + 2*3, 1*2 + 2*4] = [7, 10]
        //          | 3 4 |
        assertEquals(new Vector1by2(7, 10), Matrix2by2.multiply(new Vector1by2(1, 2), M));
    }

    @Test
    public void multiplyByIdentityChangesNothing()
    {
        Vector1by2 v = new Vector1by2(-3, 8);
        assertEquals(v, Matrix2by2.multiply(v, Matrix2by2.identity()));
    }

    @Test
    public void multiplyZeroVectorGivesZero()
    {
        assertEquals(new Vector1by2(0, 0), Matrix2by2.multiply(new Vector1by2(0, 0), M));
    }

    @Test
    public void multiplyUsesDotWithColumns()
    {
        Vector1by2 v = new Vector1by2(5, -2);
        Vector1by2 result = Matrix2by2.multiply(v, M);
        assertEquals(Vector1by2.dot(v, M.getColumn(0)), result.getRow(), DELTA);
        assertEquals(Vector1by2.dot(v, M.getColumn(1)), result.getCol(), DELTA);
    }

    // ---------------- multiply(Matrix2by2, Matrix2by2) ----------------

    @Test
    public void multiplyTwoMatrices()
    {
        // | 1 2 | * | 1 2 | = | 7  10 |
        // | 3 4 |   | 3 4 |   | 15 22 |
        assertEquals(new Matrix2by2(7, 10, 15, 22), Matrix2by2.multiply(M, M));
    }

    @Test
    public void twoQuarterTurnsMakeAHalfTurn()
    {
        Matrix2by2 ccw = Matrix2by2.rotate90CounterClockwise();
        assertEquals(Matrix2by2.rotate180(), Matrix2by2.multiply(ccw, ccw));
    }

    @Test
    public void clockwiseUndoesCounterClockwise()
    {
        assertEquals(Matrix2by2.identity(),
            Matrix2by2.multiply(Matrix2by2.rotate90Clockwise(), Matrix2by2.rotate90CounterClockwise()));
    }

    // ---------------- rotation matrices ----------------

    @Test
    public void rotate180Matrix()
    {
        assertEquals(new Matrix2by2(-1, 0, 0, -1), Matrix2by2.rotate180());
    }

    @Test
    public void rotate90CounterClockwiseMatrix()
    {
        assertEquals(new Matrix2by2(0, 1, -1, 0), Matrix2by2.rotate90CounterClockwise());
    }

    @Test
    public void rotate90ClockwiseMatrix()
    {
        assertEquals(new Matrix2by2(0, -1, 1, 0), Matrix2by2.rotate90Clockwise());
    }

    @Test
    public void rotationOfZeroIsIdentity()
    {
        assertEquals(Matrix2by2.identity(), Matrix2by2.rotation(0));
    }

    @Test
    public void rotatingAPixelLocation()
    {
        // pixel at row 2, col 5
        Vector1by2 p = new Vector1by2(2, 5);
        assertEquals(new Vector1by2(-2, -5), Matrix2by2.multiply(p, Matrix2by2.rotate180()));
        assertEquals(new Vector1by2(-5, 2), Matrix2by2.multiply(p, Matrix2by2.rotate90CounterClockwise()));
        assertEquals(new Vector1by2(5, -2), Matrix2by2.multiply(p, Matrix2by2.rotate90Clockwise()));
    }

    // ---------------- equals and toString ----------------

    @Test
    public void equalsAndNotEquals()
    {
        assertEquals(new Matrix2by2(1, 2, 3, 4), M);
        assertNotEquals(new Matrix2by2(4, 3, 2, 1), M);
        assertNotEquals(M, "not a matrix");
    }

    @Test
    public void toStringFormat()
    {
        assertEquals("[[1.0, 2.0], [3.0, 4.0]]", M.toString());
    }
}
