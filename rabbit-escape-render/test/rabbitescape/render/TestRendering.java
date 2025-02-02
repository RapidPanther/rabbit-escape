package rabbitescape.render;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.List;

import rabbitescape.render.androidlike.Bitmap;
import rabbitescape.render.androidlike.Canvas;
import rabbitescape.render.androidlike.Paint;

import org.junit.*;

public class TestRendering
{
    @Test
    public void Bitmaps_are_not_rescaled_every_render()
    {
        TrackingBitmapScaler scaler = new TrackingBitmapScaler();

        ScaledBitmap<FakeBitmap> bitmap = newBitmap( scaler );

        List<Sprite<FakeBitmap>> sprites1 = sprites( bitmap, 1, 1, 6, 4 );
        List<Sprite<FakeBitmap>> sprites2 = sprites( bitmap, 1, 1, 6, 4 );

        TrackingCanvas output = new TrackingCanvas( 200, 200 );

        Renderer<FakeBitmap, FakePaint> renderer =
            new Renderer<FakeBitmap, FakePaint>( 0, 0, 16 );

        // Sanity: no calls to scale yet
        assertThat( scaler.scaleCalls.size(), equalTo( 0 ) );

        renderer.render( output, sprites1, null );

        // Sanity: we had to scale the bitmap
        assertThat( scaler.scaleCalls.size(), equalTo( 1 ) );
        assertThat( scaler.scaleCalls.get( 0 ), equalTo( 0.5 ) );

        // This is what we are testing: render new sprite, same bitmap
        renderer.render( output, sprites2, null );

        // No new scale call was made
        assertThat( scaler.scaleCalls.size(), equalTo( 1 ) );
    }

    @Test
    public void Bitmaps_are_drawn_if_they_overlap_the_canvas()
    {
        // Exact top left
        assertDrawnAt( 0f, 0f, 0, 0,   0,   0,   0,   0, 64, 64, 32, 32, 32 );
        assertDrawnAt( 0f, 0f, 1, 1,   0,   0, -32, -32, 64, 64, 32, 32, 32 );
        assertDrawnAt( 0f, 0f, 3, 3,   0,   0, -48, -48, 64, 64, 32, 32, 16 );
        assertDrawnAt( 0f, 0f, 1, 1, -32, -32,   0,   0, 64, 64, 32, 32, 32 );
        assertDrawnAt( 0f, 0f, 3, 2, -96, -64,   0,   0, 64, 64, 32, 32, 48 );
        assertDrawnAt( 0f, 0f, 4, 2, -96, -64, -48,   0, 64, 64, 32, 32, 48 );

        // Overlap from top left
        assertDrawnAt( -6f, -6f, 0, 0, -6, -6, 0, 0, 64, 64, 32, 32, 32 );
        assertDrawnAt( -6f, -6f, 0, 0, -4, -4, 0, 0, 64, 64, 32, 32, 48 );

        // Overlap from left
        assertDrawnAt( -6f, 0f, 0, 0,   0, 0, -6, 0, 64, 64, 32, 32, 32 );
        assertDrawnAt( -6f, 0f, 0, 0, -12, 0,  0, 0, 64, 64, 32, 32, 16 );

        // Overlap from top
        assertDrawnAt( 0f, -6f, 0, 0,   0, 0,  0, -6, 64, 64, 32, 32, 32 );
        assertDrawnAt( 0f, -6f, 0, 0, -0, -12, 0,  0, 64, 64, 32, 32, 16 );

        // Overlap to bottom right
        assertDrawnAt( 0f,   0f, 0, 0, 0, 0,  0,  0, 64, 64, 96, 96, 32 );
        assertDrawnAt( 32f, 32f, 1, 1, 0, 0,  0,  0, 64, 64, 64, 64, 32 );
        assertDrawnAt( 62f, 62f, 0, 0, 0, 0, 62, 62, 64, 64, 64, 64, 64 );
    }

    @Test
    public void Bitmaps_are_not_drawn_if_they_do_not_overlap_the_canvas()
    {
        // Just on top left, then off top left
        assertDrawnAt( -31f, -31f, 0, 0, 0, 0, -31, -31, 64, 64, 32, 32, 32 );
        assertNotDrawn(            0, 0, 0, 0, -32, -32, 64, 64, 32, 32, 32 );

        // Again, larger image
        assertDrawnAt( -63f, -63f, 0, 0, -32, -32, -31, -31, 64, 64, 64, 64, 32 );
        assertNotDrawn(            0, 0, -32, -32, -32, -32, 64, 64, 64, 64, 32 );

        // Just on left, then off left
        assertDrawnAt( -31f, 0f, 0, 0, 0, 0, -31, 0, 64, 64, 32, 32, 32 );
        assertNotDrawn(          0, 0, 0, 0, -32, 0, 64, 64, 32, 32, 32 );

        // Just on top, then off top
        assertDrawnAt(  0f, -31f, 0, 0, 0, -31, 0, 0, 64, 64, 32, 32, 32 );
        assertNotDrawn(           0, 0, 0, -32, 0, 0, 64, 64, 32, 32, 32 );

        // Just on bottom right, then off botton right
        assertDrawnAt( 63f, 63f, 0, 0, 0, 0, 63, 63, 64, 64, 32, 32, 32 );
        assertNotDrawn(          0, 0, 0, 0, 64, 64, 64, 64, 32, 32, 32 );

        // Just on bottom right, then off botton right (again)
        assertDrawnAt( 63f, 63f, 0, 0, 15, 15, 48, 48, 64, 64, 32, 32, 32 );
        assertNotDrawn(          0, 0, 16, 16, 48, 48, 64, 64, 32, 32, 32 );

        // Just on bottom right, then off botton right (again)
        assertDrawnAt( 63f, 63f, 3, 3, 16, 16, 7, 7, 64, 64, 32, 32, 16 );
        assertNotDrawn(          3, 3, 16, 16, 8, 8, 64, 64, 32, 32, 16 );
    }

    // ---

    private void assertNotDrawn(
        int tileX,
        int tileY,
        int spriteOffset32X,
        int spriteOffset32Y,
        int rendererOffsetX,
        int rendererOffsetY,
        int canvasSizeX,
        int canvasSizeY,
        int bitmapWidth,
        int bitmapHeight,
        int tileSize
    )
    {
        TrackingCanvas output = draw( tileX, tileY, spriteOffset32X,
            spriteOffset32Y, rendererOffsetX, rendererOffsetY, canvasSizeX,
            canvasSizeY, bitmapWidth, bitmapHeight, tileSize );

        assertThat( output.drawCalls.size(), equalTo( 0 ) );
    }

    private void assertDrawnAt(
        float expectedDrawX,
        float expectedDrawY,
        int tileX,
        int tileY,
        int spriteOffset32X,
        int spriteOffset32Y,
        int rendererOffsetX,
        int rendererOffsetY,
        int canvasSizeX,
        int canvasSizeY,
        int bitmapWidth,
        int bitmapHeight,
        int tileSize
    )
    {
        TrackingCanvas output = draw( tileX, tileY, spriteOffset32X,
            spriteOffset32Y, rendererOffsetX, rendererOffsetY, canvasSizeX,
            canvasSizeY, bitmapWidth, bitmapHeight, tileSize );

        assertThat( output.drawCalls.get( 0 ).left, equalTo( expectedDrawX ) );
        assertThat( output.drawCalls.get( 0 ).top,  equalTo( expectedDrawY ) );
        assertThat( output.drawCalls.size(), equalTo( 1 ) );
    }

    private TrackingCanvas draw(
        int tileX,
        int tileY,
        int spriteOffset32X,
        int spriteOffset32Y,
        int rendererOffsetX,
        int rendererOffsetY,
        int canvasSizeX,
        int canvasSizeY,
        int bitmapWidth,
        int bitmapHeight,
        int tileSize )
    {
        TrackingCanvas output = new TrackingCanvas( canvasSizeX, canvasSizeY );

        new Renderer<FakeBitmap, FakePaint>(
            rendererOffsetX, rendererOffsetY, tileSize
        ).render(
            output,
            sprites(
                newBitmap( bitmapWidth, bitmapHeight ),
                tileX,
                tileY,
                spriteOffset32X,
                spriteOffset32Y
            ),
            null
        );

        return output;
    }

    private ScaledBitmap<FakeBitmap> newBitmap( int width, int height )
    {
        return newBitmap( new TrackingBitmapScaler(), width, height );
    }


    private ScaledBitmap<FakeBitmap> newBitmap(
        TrackingBitmapScaler scaler )
    {
        return newBitmap( scaler, 32, 32 );
    }

    private ScaledBitmap<FakeBitmap> newBitmap(
        TrackingBitmapScaler scaler, int width, int height )
    {
        return new ScaledBitmap<FakeBitmap>(
            scaler, new FakeBitmapLoader( width, height ), "x" );
    }

    private List<Sprite<FakeBitmap>> sprites(
        ScaledBitmap<FakeBitmap> bitmap,
        int tileX,
        int tileY,
        int offset32X,
        int offset32Y
    )
    {
        List<Sprite<FakeBitmap>> ret = new ArrayList<Sprite<FakeBitmap>>();

        ret.add( new Sprite<FakeBitmap>(
            bitmap, null, tileX, tileY, offset32X, offset32Y ) );

        return ret;
    }

    private static class FakeBitmap implements Bitmap
    {
        private final int width;
        private final int height;

        public FakeBitmap( int width, int height )
        {
            this.width = width;
            this.height = height;
        }

        @Override
        public String name()
        {
            return null;
        }

        @Override
        public int width()
        {
            return width;
        }

        @Override
        public int height()
        {
            return height;
        }

        @Override
        public void recycle()
        {
        }
    }

    private static class FakePaint implements Paint
    {
    }

    private static class FakeBitmapLoader implements BitmapLoader<FakeBitmap>
    {
        private final int width;
        private final int height;

        public FakeBitmapLoader( int width, int height )
        {
            this.width = width;
            this.height = height;
        }

        @Override
        public FakeBitmap load( String fileName, int tileSize )
        {
            return new FakeBitmap( width, height );
        }

        @Override
        public int sizeFor( int tileSize )
        {
            return 32;
        }
    }

    private static class TrackingBitmapScaler
    implements BitmapScaler<FakeBitmap>
    {
        public List<Double> scaleCalls = new ArrayList<Double>();

        @Override
        public FakeBitmap scale( FakeBitmap originalBitmap, double scale )
        {
            scaleCalls.add( scale );
            return originalBitmap;
        }
    }

    public static class TrackingCanvas implements Canvas<FakeBitmap, FakePaint>
    {
        private static class DrawCall
        {
            public final float left;
            public final float top;

            public DrawCall( float left, float top )
            {
                this.left = left;
                this.top = top;
            }
        }

        public List<DrawCall> drawCalls = new ArrayList<DrawCall>();

        private final int width;
        private final int height;

        public TrackingCanvas( int width, int height )
        {
            this.width = width;
            this.height = height;
        }

        @Override
        public void drawBitmap(
            FakeBitmap bitmap,
            float left,
            float top,
            FakePaint paint
        )
        {
            drawCalls.add( new DrawCall( left, top ) );
        }

        @Override
        public int width()
        {
            return width;
        }

        @Override
        public int height()
        {
            return height;
        }

        @Override
        public void drawColor( FakePaint paint )
        {
        }

        @Override
        public void drawLine(
            float startX,
            float startY,
            float stopX,
            float stopY,
            FakePaint paint
        )
        {
        }
    }
}
