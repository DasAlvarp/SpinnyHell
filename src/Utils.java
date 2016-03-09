import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.Arrays;

/**
 * Created by Copied and Pasted from StackExchange on 2/22/2016.
 *
 */
public class Utils {
        public Utils()
        {}

        public Polygon rotateRect(Rectangle r, int x, int y, double rotate) {
            AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(rotate), x, y);
            Polygon p = new Polygon();
            PathIterator i = r.getPathIterator(at);
            int i2 = 0;
            while (!i.isDone() && i2 < 4) {
                double[] xy = new double[2];
                i.currentSegment(xy);
                p.addPoint((int) xy[0], (int) xy[1]);
                i2++;
                i.next();
            }

            // should now be inside :)
            return p;
        }

}
