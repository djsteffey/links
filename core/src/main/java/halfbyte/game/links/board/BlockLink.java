package halfbyte.game.links.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import halfbyte.game.links.Constants;

public class BlockLink extends Group {
    // variables
    private static ShapeRenderer s_shape_renderer;
    private Block m_block_a;
    private Block m_block_b;


    // methods
    public BlockLink(Block a, Block b){
        // save
        this.m_block_a = a;
        this.m_block_b = b;

        // renderer
        if (BlockLink.s_shape_renderer == null){
            BlockLink.s_shape_renderer = new ShapeRenderer();
        }

        this.setPosition(this.m_block_a.getX(Align.center), this.m_block_b.getY(Align.center));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // end batch
        batch.end();

        // enable blending
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // setup transforms
        BlockLink.s_shape_renderer.setProjectionMatrix(batch.getProjectionMatrix());
        BlockLink.s_shape_renderer.setTransformMatrix(batch.getTransformMatrix());

        // begin
        BlockLink.s_shape_renderer.begin(ShapeRenderer.ShapeType.Filled);

        // circle and lines for the link
        BlockLink.s_shape_renderer.setColor(1.0f, 1.0f, 1.0f, 0.75f);
        BlockLink.s_shape_renderer.circle(this.m_block_a.getX(Align.center), this.m_block_a.getY(Align.center), Constants.TILE_SIZE / 8.0f);
        BlockLink.s_shape_renderer.circle(this.m_block_b.getX(Align.center), this.m_block_b.getY(Align.center), Constants.TILE_SIZE / 8.0f);
        BlockLink.s_shape_renderer.rectLine(
                this.m_block_a.getX(Align.center),
                this.m_block_a.getY(Align.center),
                this.m_block_b.getX(Align.center),
                this.m_block_b.getY(Align.center),
                8.0f
        );

        // end
        BlockLink.s_shape_renderer.end();

        // start batch again
        batch.begin();
    }

    public Block getOtherBlock(Block block){
        if (block == this.m_block_a){
            return this.m_block_b;
        }
        else if (block == this.m_block_b){
            return this.m_block_a;
        }
        return null;
    }
}
