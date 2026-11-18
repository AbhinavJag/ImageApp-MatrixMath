/**
 * A 1-by-2 row vector [row, col].
 *
 * In the Image App every pixel location is stored as a Vector1by2 so that
 * it can be multiplied by a 2-by-2 transformation matrix (see Matrix2by2).
 *
 * Vector1by2 objects are immutable: once created, their values never change.
 * Every static "math" method returns a brand new vector.
 *
 * @author Abhinav and Yash
 */
public class Vector1by2
{
    /** Two doubles closer than this are treated as equal (avoids round-off trouble). */
    public static final double EPSILON = 1e-9;

    /** The first entry of the vector (the row). */
    private final double row;

    /** The second entry of the vector (the column). */
    private final double col;

    /**
     * Creates the vector [row, col].
     *
     * @param row the first entry (row)
     * @param col the second entry (column)
     */
    public Vector1by2(double row, double col)
    {
        this.row = row;
        this.col = col;
    }

    /**
     * Returns the first entry of the vector.
     *
     * @return the row value
     */
    public double getRow()
    {
        return row;
    }

    /**
     * Returns the second entry of the vector.
     *
     * @return the column value
     */
    public double getCol()
    {
        return col;
    }

    /**
     * Returns the row value rounded to the nearest whole number,
     * which is handy for turning a vector back into a pixel index.
     *
     * @return the rounded row value
     */
    public int getRowIndex()
    {
        return (int) Math.round(row);
    }

    /**
     * Returns the column value rounded to the nearest whole number.
     *
     * @return the rounded column value
     */
    public int getColIndex()
    {
        return (int) Math.round(col);
    }

    // ------------------------------------------------------------------
    //  Static vector functions
    // ------------------------------------------------------------------

    /**
     * Computes the dot product of two vectors:
     * [a, b] . [c, d] = a*c + b*d
     *
     * This is the building block of matrix multiplication.
     *
     * @param v1 the first vector
     * @param v2 the second vector
     * @return the dot product of v1 and v2
     */
    public static double dot(Vector1by2 v1, Vector1by2 v2)
    {
        return v1.getRow() * v2.getRow() + v1.getCol() * v2.getCol();
    }

    /**
     * Adds two vectors entry by entry:
     * [a, b] + [c, d] = [a + c, b + d]
     *
     * Used to shift (translate) a rotated pixel back onto the canvas.
     *
     * @param v1 the first vector
     * @param v2 the second vector
     * @return a new vector that is the sum
     */
    public static Vector1by2 add(Vector1by2 v1, Vector1by2 v2)
    {
        return new Vector1by2(v1.getRow() + v2.getRow(), v1.getCol() + v2.getCol());
    }

    /**
     * Multiplies every entry of a vector by a number:
     * k * [a, b] = [k*a, k*b]
     *
     * @param v the vector
     * @param k the scale factor
     * @return a new, scaled vector
     */
    public static Vector1by2 scale(Vector1by2 v, double k)
    {
        return new Vector1by2(k * v.getRow(), k * v.getCol());
    }

    // ------------------------------------------------------------------
    //  Object methods
    // ------------------------------------------------------------------

    /**
     * Two vectors are equal when both entries match (within EPSILON).
     *
     * @param other the object to compare to
     * @return true if other is a Vector1by2 with the same entries
     */
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof Vector1by2))
        {
            return false;
        }
        Vector1by2 v = (Vector1by2) other;
        return Math.abs(row - v.row) < EPSILON && Math.abs(col - v.col) < EPSILON;
    }

    /**
     * Hash code that agrees with equals for whole-number vectors.
     *
     * @return a hash code for this vector
     */
    @Override
    public int hashCode()
    {
        return 31 * Long.hashCode(Math.round(row)) + Long.hashCode(Math.round(col));
    }

    /**
     * Returns the vector as text, for example "[3.0, 4.0]".
     *
     * @return a string version of the vector
     */
    @Override
    public String toString()
    {
        return "[" + row + ", " + col + "]";
    }
}
