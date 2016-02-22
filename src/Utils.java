import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

/**
 * Created by alvarpq on 2/22/2016.
 */
public class Utils {
        public Utils()
        {}

        public Polygon rotateRect(Rectangle r, int x, int y, double rotate) {
            Point check = new Point(x, y); // clearly outside

            System.out.println("first: " + r.contains(check));

            AffineTransform at = AffineTransform.getRotateInstance(
                    Math.toRadians(rotate), r.getCenterX(), r.getCenterY());

            Polygon p = new Polygon();

            PathIterator i = r.getPathIterator(at);
            int i2 = 0;
            while (!i.isDone() && i2 < 4) {
                double[] xy = new double[2];
                i.currentSegment(xy);
                p.addPoint((int) xy[0], (int) xy[1]);
                System.out.println(Arrays.toString(xy));
                i2++;
                i.next();
            }

            // should now be inside :)
            System.out.println("second: " + p.contains(check));
            return p;
        }

}
