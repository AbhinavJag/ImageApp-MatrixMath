/**
 * A 2-by-2 matrix
 *
 *     | a  b |
 *     | c  d |
 *
 * used as a transformation matrix for rotating images.
 *
 * Pixel locations are ROW vectors [row, col], so a location is transformed by
 * multiplying the vector on the LEFT of the matrix:
 *
 *     [row, col] * | a  b |  =  [row*a + col*c,  row*b + col*d]
 *                  | c  d |
 *
 * Each entry of the answer is the dot product of the vector with one COLUMN
 * of the matrix. That is exactly how multiply(Vector1by2, Matrix2by2) works.
 *
 * Matrix2by2 objects are immutable.
 *
 * @author Abhinav and Yash
 */
public class Matrix2by2
{
    /** Top-left entry. */
    private final double a;

    /** Top-right entry. */
    private final double b;

    /** Bottom-left entry. */
    private final double c;

    /** Bottom-right entry. */
    private final double d;

    /**
     * Creates the matrix | a b | / | c d | (entries given row by row).
     *
     * @param a top-left
     * @param b top-right
     * @param c bottom-left
     * @param d bottom-right
     */
    public Matrix2by2(double a, double b, double c, double d)
    {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    /**
     * Returns one entry of the matrix.
     *
     * @param r the row (0 or 1)
     * @param k the column (0 or 1)
     * @return the entry at (r, k)
     * @throws IndexOutOfBoundsException if r or k is not 0 or 1
     */
    public double get(int r, int k)
    {
        checkIndex(r);
        checkIndex(k);
        if (r == 0)
        {
            return (k == 0) ? a : b;
        }
        return (k == 0) ? c : d;
    }

    /**
     * Returns one row of the matrix as a vector.
     *
     * @param r the row (0 or 1)
     * @return row r as a Vector1by2
     */
    public Vector1by2 getRow(int r)
    {
        checkIndex(r);
        return (r == 0) ? new Vector1by2(a, b) : new Vector1by2(c, d);
    }

    /**
     * Returns one column of the matrix as a vector.
     *
     * @param k the column (0 or 1)
     * @return column k as a Vector1by2
     */
    public Vector1by2 getColumn(int k)
    {
        checkIndex(k);
        return (k == 0) ? new Vector1by2(a, c) : new Vector1by2(b, d);
    }

    /**
     * Makes sure an index is 0 or 1.
     *
     * @param i the index to check
     */
    private static void checkIndex(int i)
    {
        if (i != 0 && i != 1)
        {
            throw new IndexOutOfBoundsException("Matrix2by2 index must be 0 or 1, was " + i);
        }
    }

    // ------------------------------------------------------------------
    //  Static matrix functions
    // ------------------------------------------------------------------

    /**
     * Multiplies a row vector by a matrix: v * m.
     *
     * Each entry of the result is a dot product of v with a column of m:
     *     result = [ v . column0 ,  v . column1 ]
     *
     * @param v the row vector [row, col]
     * @param m the transformation matrix
     * @return the transformed vector
     */
    public static Vector1by2 multiply(Vector1by2 v, Matrix2by2 m)
    {
        double newRow = Vector1by2.dot(v, m.getColumn(0));
        double newCol = Vector1by2.dot(v, m.getColumn(1));
        return new Vector1by2(newRow, newCol);
    }

    /**
     * Multiplies two matrices: m1 * m2.
     * Entry (r, k) of the answer is (row r of m1) . (column k of m2).
     *
     * Doing two transformations in a row is the same as multiplying
     * their matrices (for example, two 90 degree turns = one 180 degree turn).
     *
     * @param m1 the left matrix
     * @param m2 the right matrix
     * @return the product m1 * m2
     */
    public static Matrix2by2 multiply(Matrix2by2 m1, Matrix2by2 m2)
    {
        return new Matrix2by2(
            Vector1by2.dot(m1.getRow(0), m2.getColumn(0)),
            Vector1by2.dot(m1.getRow(0), m2.getColumn(1)),
            Vector1by2.dot(m1.getRow(1), m2.getColumn(0)),
            Vector1by2.dot(m1.getRow(1), m2.getColumn(1)));
    }

    /**
     * Returns the identity matrix | 1 0 | / | 0 1 |, which leaves every vector unchanged.
     *
     * @return the identity matrix
     */
    public static Matrix2by2 identity()
    {
        return new Matrix2by2(1, 0, 0, 1);
    }

    /**
     * Returns the matrix that rotates an image counterclockwise by the given angle.
     *
     * Images use (row, col) with rows going DOWN the screen, so the
     * counterclockwise rotation matrix for a row vector is
     *
     *     |  cos t   sin t |
     *     | -sin t   cos t |
     *
     * Tiny round-off values (like cos 90 = 6e-17) are snapped to 0.
     *
     * @param degrees the angle; positive is counterclockwise, negative is clockwise
     * @return the rotation matrix
     */
    public static Matrix2by2 rotation(double degrees)
    {
        double t = Math.toRadians(degrees);
        double cos = snap(Math.cos(t));
        double sin = snap(Math.sin(t));
        return new Matrix2by2(cos, sin, -sin, cos);
    }

    /**
     * Rotates an image 180 degrees (upside down): | -1 0 | / | 0 -1 |.
     *
     * @return the 180 degree rotation matrix
     */
    public static Matrix2by2 rotate180()
    {
        return rotation(180);
    }

    /**
     * Rotates an image 90 degrees counterclockwise: | 0 1 | / | -1 0 |.
     *
     * @return the 90 degree counterclockwise rotation matrix
     */
    public static Matrix2by2 rotate90CounterClockwise()
    {
        return rotation(90);
    }

    /**
     * Rotates an image 90 degrees clockwise: | 0 -1 | / | 1 0 |.
     *
     * @return the 90 degree clockwise rotation matrix
     */
    public static Matrix2by2 rotate90Clockwise()
    {
        return rotation(-90);
    }

    /**
     * Rounds values that are almost 0, 1 or -1 to exactly that value.
     *
     * @param x the value to clean up
     * @return the cleaned-up value
     */
    private static double snap(double x)
    {
        double rounded = Math.rint(x);
        return (Math.abs(x - rounded) < 1e-12) ? rounded + 0.0 : x;
    }

    // ------------------------------------------------------------------
    //  Object methods
    // ------------------------------------------------------------------

    /**
     * Two matrices are equal when all four entries match (within Vector1by2.EPSILON).
     *
     * @param other the object to compare to
     * @return true if other is a Matrix2by2 with the same entries
     */
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof Matrix2by2))
        {
            return false;
        }
        Matrix2by2 m = (Matrix2by2) other;
        return getRow(0).equals(m.getRow(0)) && getRow(1).equals(m.getRow(1));
    }

    /**
     * Hash code that agrees with equals for whole-number matrices.
     *
     * @return a hash code for this matrix
     */
    @Override
    public int hashCode()
    {
        return 31 * getRow(0).hashCode() + getRow(1).hashCode();
    }

    /**
     * Returns the matrix as text, for example "[[0.0, 1.0], [-1.0, 0.0]]".
     *
     * @return a string version of the matrix
     */
    @Override
    public String toString()
    {
        return "[" + getRow(0) + ", " + getRow(1) + "]";
    }
}
