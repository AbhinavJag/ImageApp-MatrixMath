import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import org.junit.jupiter.api.Test;

/**
 * Black-box unit tests for the ImageApp image methods.
 * Uses tiny pictures (2 rows x 3 columns) so every pixel can be checked by hand.
 *
 * @author Abhinav and Yash
 */
public class ImageAppTest
{
    /**
     * Builds a 2 x 3 test picture:
     *     row 0:  A  B  C
     *     row 1:  D  E  F
     * where each letter is a different color.
     */
    private static final Color A = new Color(10, 20, 30);
    private static final Color B = new Color(40, 50, 60);
    private static final Color C = new Color(70, 80, 90);
    private static final Color D = new Color(100, 110, 120);
    private static final Color E = new Color(130, 140, 150);
    private static final Color F = new Color(160, 170, 180);

    private static Picture smallPicture()
    {
        Picture pic = new Picture(2, 3);   // Picture(height, width)
        Color[][] colors = { { A, B, C }, { D, E, F } };
        Pixel[][] px = pic.getPixels2D();
        for (int r = 0; r < 2; r++)
        {
            for (int c = 0; c < 3; c++)
            {
                px[r][c].setColor(colors[r][c]);
            }
        }
        return pic;
    }

    // ---------------- recolor ----------------

    @Test
    public void negativeFlipsEachColor()
    {
        Picture pic = smallPicture();
        ImageApp.negate(pic.getPixels2D());
        assertEquals(new Color(245, 235, 225), pic.getPixels2D()[0][0].getColor());
    }

    @Test
    public void grayscaleUsesTheAverage()
    {
        Picture pic = smallPicture();
        ImageApp.grayscale(pic.getPixels2D());
        assertEquals(new Color(20, 20, 20), pic.getPixels2D()[0][0].getColor()); // (10+20+30)/3
    }

    @Test
    public void recolorAddsRedRemovesBlueAndStaysInRange()
    {
        Picture pic = smallPicture();
        ImageApp.recolor(pic.getPixels2D());
        Color c = pic.getPixels2D()[0][0].getColor();
        assertEquals(70, c.getRed());    // 10 + 60
        assertEquals(35, c.getGreen());  // 20 + 15
        assertEquals(0, c.getBlue());    // 30 - 60 is clamped to 0
    }

    // ---------------- rotations ----------------

    @Test
    public void rotate180()
    {
        // A B C        F E D
        // D E F   ->   C B A
        Pixel[][] px = ImageApp.rotate(smallPicture(), Matrix2by2.rotate180()).getPixels2D();
        assertEquals(2, px.length);
        assertEquals(3, px[0].length);
        assertEquals(F, px[0][0].getColor());
        assertEquals(A, px[1][2].getColor());
    }

    @Test
    public void rotate90CounterClockwiseSwapsSize()
    {
        // A B C        C F
        // D E F   ->   B E
        //              A D
        Pixel[][] px = ImageApp.rotate(smallPicture(), Matrix2by2.rotate90CounterClockwise()).getPixels2D();
        assertEquals(3, px.length);
        assertEquals(2, px[0].length);
        assertEquals(C, px[0][0].getColor());
        assertEquals(D, px[2][1].getColor());
    }

    @Test
    public void rotate90Clockwise()
    {
        // A B C        D A
        // D E F   ->   E B
        //              F C
        Pixel[][] px = ImageApp.rotate(smallPicture(), Matrix2by2.rotate90Clockwise()).getPixels2D();
        assertEquals(3, px.length);
        assertEquals(D, px[0][0].getColor());
        assertEquals(C, px[2][1].getColor());
    }

    @Test
    public void rotateIntArray()
    {
        int[][] grid = { { 1, 2, 3 }, { 4, 5, 6 } };
        int[][] expected = { { 4, 1 }, { 5, 2 }, { 6, 3 } };
        assertArrayEquals(expected, ImageApp.rotate(grid, Matrix2by2.rotate90Clockwise()));
    }

    @Test
    public void fourQuarterTurnsGiveTheOriginal()
    {
        int[][] grid = { { 1, 2, 3 }, { 4, 5, 6 } };
        int[][] result = grid;
        for (int i = 0; i < 4; i++)
        {
            result = ImageApp.rotate(result, Matrix2by2.rotate90CounterClockwise());
        }
        assertArrayEquals(grid, result);
    }

    // ---------------- paste ----------------

    /** A 2 x 2 "sticker": white background with one red pixel at (1, 1). */
    private static Picture sticker()
    {
        Picture s = new Picture(2, 2);   // starts all white
        s.getPixels2D()[1][1].setColor(Color.RED);
        return s;
    }

    @Test
    public void pasteWithNewColorsSkipsBackgroundAndChangesColor()
    {
        Picture big = smallPicture();
        ImageApp.pasteWithNewColors(big, sticker(), 0, 0);
        Pixel[][] px = big.getPixels2D();
        assertEquals(A, px[0][0].getColor());                    // white background removed
        assertEquals(new Color(0, 255, 255), px[1][1].getColor()); // red became its negative (cyan)
    }

    @Test
    public void pasteBlendedAveragesColors()
    {
        Picture big = smallPicture();
        ImageApp.pasteBlended(big, sticker(), 0, 0);
        // average of red (255,0,0) and E (130,140,150) = (192,70,75)
        assertEquals(new Color(192, 70, 75), big.getPixels2D()[1][1].getColor());
        assertEquals(A, big.getPixels2D()[0][0].getColor());
    }

    @Test
    public void pasteOffTheEdgeDoesNotCrash()
    {
        Picture big = smallPicture();
        assertDoesNotThrow(() -> ImageApp.pasteBlended(big, sticker(), 1, 2));
    }
}
