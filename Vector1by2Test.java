import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Black-box unit tests for the public interface of Vector1by2.
 *
 * Each test checks one public method against answers worked out by hand.
 *
 * @author Abhinav and Yash
 */
public class Vector1by2Test
{
    private static final double DELTA = 1e-9;

    // ---------------- constructor and getters ----------------

    @Test
    public void constructorStoresRowAndCol()
    {
        Vector1by2 v = new Vector1by2(3, -4);
        assertEquals(3.0, v.getRow(), DELTA);
        assertEquals(-4.0, v.getCol(), DELTA);
    }

    @Test
    public void indexGettersRoundToNearestWholeNumber()
    {
        Vector1by2 v = new Vector1by2(2.9999999, -0.0000001);
        assertEquals(3, v.getRowIndex());
        assertEquals(0, v.getColIndex());
    }

    // ---------------- dot ----------------

    @Test
    public void dotOfTwoVectors()
    {
        // [1, 2] . [3, 4] = 1*3 + 2*4 = 11
        assertEquals(11.0, Vector1by2.dot(new Vector1by2(1, 2), new Vector1by2(3, 4)), DELTA);
    }

    @Test
    public void dotWithNegativesAndDecimals()
    {
        // [-2, 0.5] . [3, 8] = -6 + 4 = -2
        assertEquals(-2.0, Vector1by2.dot(new Vector1by2(-2, 0.5), new Vector1by2(3, 8)), DELTA);
    }

    @Test
    public void dotOfPerpendicularVectorsIsZero()
    {
        // [1, 0] . [0, 1] = 0
        assertEquals(0.0, Vector1by2.dot(new Vector1by2(1, 0), new Vector1by2(0, 1)), DELTA);
    }

    @Test
    public void dotIsCommutative()
    {
        Vector1by2 a = new Vector1by2(5, -7);
        Vector1by2 b = new Vector1by2(2, 3);
        assertEquals(Vector1by2.dot(a, b), Vector1by2.dot(b, a), DELTA);
    }

    @Test
    public void dotWithZeroVectorIsZero()
    {
        assertEquals(0.0, Vector1by2.dot(new Vector1by2(9, 9), new Vector1by2(0, 0)), DELTA);
    }

    // ---------------- add and scale ----------------

    @Test
    public void addAddsEntryByEntry()
    {
        // [1, 2] + [3, -5] = [4, -3]
        assertEquals(new Vector1by2(4, -3), Vector1by2.add(new Vector1by2(1, 2), new Vector1by2(3, -5)));
    }

    @Test
    public void addDoesNotChangeInputs()
    {
        Vector1by2 a = new Vector1by2(1, 2);
        Vector1by2.add(a, new Vector1by2(10, 10));
        assertEquals(new Vector1by2(1, 2), a);
    }

    @Test
    public void scaleMultipliesBothEntries()
    {
        // -2 * [3, -1] = [-6, 2]
        assertEquals(new Vector1by2(-6, 2), Vector1by2.scale(new Vector1by2(3, -1), -2));
    }

    // ---------------- equals and toString ----------------

    @Test
    public void equalVectors()
    {
        assertEquals(new Vector1by2(1, 2), new Vector1by2(1.0, 2.0));
        assertEquals(new Vector1by2(1, 2).hashCode(), new Vector1by2(1.0, 2.0).hashCode());
    }

    @Test
    public void unequalVectors()
    {
        assertNotEquals(new Vector1by2(1, 2), new Vector1by2(2, 1));
        assertNotEquals(new Vector1by2(1, 2), "not a vector");
    }

    @Test
    public void toStringFormat()
    {
        assertEquals("[3.0, 4.0]", new Vector1by2(3, 4).toString());
    }
}
