import android.graphics.Bitmap
import com.xtremelabs.robolectric.shadows.ShadowBitmapFactory
import pl.polidea.robospock.RoboSpecification
import pl.polidea.webimageview.Bitmaps

class First extends RoboSpecification{

    def "da"(){
        given:
        final String name = "test/robol";
        ShadowBitmapFactory.provideWidthAndHeightHints(name, 36, 37);
        final Bitmaps bitmaps = new Bitmaps();

        when:
        final Bitmap generateBitmap = bitmaps.generateBitmap(name, 20, 20);

        then:
        20 == generateBitmap.getHeight()
    }

}