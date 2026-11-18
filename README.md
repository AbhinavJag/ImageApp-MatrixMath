# Image App + Matrix Math (PLTW CSA 3.3.4)

**Team:** Abhinav and Yash

A Java app that edits images using 2D arrays of pixels, with all rotations done using
**matrix multiplication**.

| # | Requirement | Method | Output |
|---|---|---|---|
| 1 | Recolor: change color | `ImageApp.recolor` | `output/1-recolor.jpg` |
| 2 | Recolor: negative | `ImageApp.negate` | `output/2-negative.jpg` |
| 3 | Recolor: grayscale | `ImageApp.grayscale` | `output/3-grayscale.jpg` |
| 4 | Rotate 180 | `ImageApp.rotate(pic, Matrix2by2.rotate180())` | `output/4-rotate180.jpg` |
| 5 | Rotate 90 counterclockwise | `ImageApp.rotate(pic, Matrix2by2.rotate90CounterClockwise())` | `output/5-rotate90ccw.jpg` |
| 6 | Rotate 90 clockwise | `ImageApp.rotate(pic, Matrix2by2.rotate90Clockwise())` | `output/6-rotate90cw.jpg` |
| 7 | Paste small image, colors completely changed, background removed | `ImageApp.pasteWithNewColors` | `output/7-paste-changed-colors.jpg` |
| 8 | Paste small image, blended with large image, background removed | `ImageApp.pasteBlended` | `output/8-paste-blended.jpg` |

## Matrix math

* `Vector1by2` – a row vector `[row, col]` with static `dot`, `add`, `scale`.
* `Matrix2by2` – a 2x2 matrix with static `multiply(Vector1by2, Matrix2by2)`,
  `multiply(Matrix2by2, Matrix2by2)`, `rotation(degrees)`, `rotate180()`,
  `rotate90CounterClockwise()`, `rotate90Clockwise()`, `identity()`.

Each pixel location is rotated with

```
[row, col] * | cos t   sin t |  + shift
             | -sin t  cos t |
```

where each entry of the product is a **dot product** of the vector with a column of the matrix.

| Rotation | Matrix |
|---|---|
| 180 | `[[-1, 0], [0, -1]]` |
| 90 counterclockwise | `[[0, 1], [-1, 0]]` |
| 90 clockwise | `[[0, -1], [1, 0]]` |

## Running

1. Open the folder in VS Code (or your IDE) and run `ImageApp.java`.
   Each image opens in the Picture Explorer and is saved in `output/`.
2. Run the unit tests (JUnit 5): `Vector1by2Test`, `Matrix2by2Test`, `ImageAppTest`
   (41 tests, all passing). In VS Code, open the Testing panel and click Run All.

## Project management

See the `docs` folder: team roles/norms/consequences, Gantt chart, project log,
bug list and unit test table.
