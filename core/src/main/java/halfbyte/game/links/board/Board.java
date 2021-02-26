package halfbyte.game.links.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import halfbyte.game.links.Constants;
import halfbyte.game.links.Util;
import halfbyte.game.links.tileset.TilesetForBlock;

public class Board extends Group {
    public interface IListener{
        void onMoveComplete(int moves);
        void onGameComplete();
    }
    // variables
    private int m_width_in_blocks;
    private int m_height_in_blocks;
    private Block[][] m_blocks;
    private Block m_dragging_block;
    private int m_num_moves;
    private IListener m_listener;

    // methods
    public Board(AssetManager am, int width_in_blocks, int height_in_blocks, IListener listener){
        // save
        this.m_width_in_blocks = width_in_blocks;
        this.m_height_in_blocks = height_in_blocks;
        this.m_listener = listener;

        // size
        this.setSize(this.m_width_in_blocks * Constants.TILE_SIZE, this.m_width_in_blocks * Constants.TILE_SIZE);

        // background
        Image background = new Image(am.get("pixel.png", Texture.class));
        background.setSize(this.getWidth(), this.getHeight());
        background.setColor(1.0f, 1.0f, 0.0f, 0.50f);
        this.addActor(background);


        // create tileset
        TilesetForBlock tileset = new TilesetForBlock(am);

        // create the blocks
        this.m_blocks = new Block[this.m_width_in_blocks][this.m_height_in_blocks];
        for (int y = 0; y < this.m_height_in_blocks; ++y){
            for (int x = 0; x < this.m_width_in_blocks; ++x){
                this.m_blocks[x][y] = null;
                if (Util.getRandomIntInRange(0, 1000) < 200){
                    this.m_blocks[x][y] = new Block(Block.EType.getRandom(), tileset, x, y);
                    this.addActor(this.m_blocks[x][y]);
                }
            }
        }

        // horizontal links
        for (int y = 0; y < this.m_height_in_blocks; ++y) {
            for (int x = 0; x < this.m_width_in_blocks - 1; ++x) {
                if (this.m_blocks[x][y] != null && this.m_blocks[x + 1][y] != null) {
                    BlockLink link = new BlockLink(this.m_blocks[x][y], this.m_blocks[x + 1][y]);
                    this.m_blocks[x][y].getLinks().add(link);
                    this.m_blocks[x + 1][y].getLinks().add(link);
                    this.addActor(link);
                }
            }
        }
        // vertical links
        for (int y = 0; y < this.m_height_in_blocks - 1; ++y) {
            for (int x = 0; x < this.m_width_in_blocks; ++x) {
                if (this.m_blocks[x][y] != null && this.m_blocks[x][y + 1] != null){
                    BlockLink link = new BlockLink(this.m_blocks[x][y], this.m_blocks[x][y + 1]);
                    this.m_blocks[x][y].getLinks().add(link);
                    this.m_blocks[x][y + 1].getLinks().add(link);
                    this.addActor(link);
                }
            }
        }

        // no drag yet
        this.m_dragging_block = null;

        // listener
        this.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Board.this.onTouchDown(x, y);
            }

            @Override
            public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                Board.this.onFling(velocityX, velocityY);
            }
        });

        // moves
        this.m_num_moves = 0;
    }

    private void onTouchDown(float x, float y){
        // get the tile clicked on
        int tile_x = (int)(x / Constants.TILE_SIZE);
        int tile_y = (int)(y / Constants.TILE_SIZE);

        // get the block
        if (this.isValidTile(tile_x, tile_y) == false){
            this.m_dragging_block = null;
        }
        else{
            this.m_dragging_block = this.m_blocks[tile_x][tile_y];
        }
    }

    private void onFling(float vx, float vy){
        Gdx.app.log("FLING", vx + " " + vy);

        // doesnt matter if no block selected
        if (this.m_dragging_block == null){
            return;
        }

        // get the direction
        Util.EDirection dir = Util.EDirection.NONE;
        if (Math.abs(vx) > Math.abs(vy)){
            // more in the x direction
            if (vx < -500.0f){
                // move left
                dir = Util.EDirection.LEFT;
            }
            else if (vx > 500.0f){
                // move right
                dir = Util.EDirection.RIGHT;
            }
        }
        else if (Math.abs(vy) > Math.abs(vx)){
            // more in the y direction
            if (vy < -500.0f){
                // move down
                dir = Util.EDirection.DOWN;
            }
            else if (vy > 500.0f){
                // move up
                dir = Util.EDirection.UP;
            }
        }

        // move it
        this.moveBlock(this.m_dragging_block, dir);

        // no more drag
        this.m_dragging_block = null;
    }

    private void moveBlock(Block block, Util.EDirection dir){
        // nothing to do if no direction
        if (dir == Util.EDirection.NONE){
            return;
        }

        // get target tile
        int target_x = block.getTileX() + dir.toGridPoint2().x;
        int target_y = block.getTileY() + dir.toGridPoint2().y;

        // see if we can mvoe there
        if (this.canMoveBlock(block, target_x, target_y) == false){
            // cant do it
            return;
        }

        // we can move there
        // reassign the tile
        this.changeBlockTile(block, target_x, target_y);

        // animate
        block.addAction(Actions.sequence(
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        block.setAlternateFace(true);
                        return true;
                    }
                },
                Actions.moveTo(
                        block.getTileX() * Constants.TILE_SIZE,
                        block.getTileY() * Constants.TILE_SIZE,
                        0.15f
                ),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        block.setAlternateFace(false);
                        Board.this.checkAfterBlockMove();
                        return true;
                    }
                }
        ));
    }

    private boolean canMoveBlock(Block block, int tile_x, int tile_y){
        // see if that direction is empty
        if (this.isEmptyTile(tile_x, tile_y) == false) {
            // cant move
            return false;
        }

        // see if links allow us
        for (BlockLink link : block.getLinks()){
            Block other = link.getOtherBlock(block);
            if (this.isOneDistance(tile_x, tile_y, other.getTileX(), other.getTileY()) == false){
                // link restricted
                return false;
            }
        }

        // we can move
        return true;
    }

    private boolean isValidTile(int tile_x, int tile_y){
        if (tile_x < 0 || tile_x > this.m_width_in_blocks - 1 || tile_y < 0 || tile_y > this.m_height_in_blocks - 1){
            return false;
        }
        return true;
    }

    private boolean isEmptyTile(int tile_x, int tile_y){
        if (this.isValidTile(tile_x, tile_y) == false){
            return false;
        }
        return this.m_blocks[tile_x][tile_y] == null;
    }

    private void changeBlockTile(Block block, int tile_x, int tile_y){
        // clear current
        this.m_blocks[block.getTileX()][block.getTileY()] = null;

        // change in block
        block.setTile(tile_x, tile_y);

        // set new
        this.m_blocks[block.getTileX()][block.getTileY()] = block;
    }

    private boolean isOneDistance(int tile_x_1, int tile_y_1, int tile_x_2, int tile_y_2) {
        int dx = Math.abs(tile_x_2 - tile_x_1);
        int dy = Math.abs(tile_y_2 - tile_y_1);

        // if the total is 1 then true
        if (dx + dy == 1){
            return true;
        }

        // if both are 1 away then still considered 1
        if (dx == 1 && dy == 1){
            return true;
        }

        // all other is false
        return false;
    }

    private void checkAfterBlockMove(){
        // increment moves
        this.m_num_moves += 1;
        this.m_listener.onMoveComplete(this.m_num_moves);

        // todo check for game over

        // temp ai do a move
        int tile_x = -1;
        int tile_y = -1;
        while (true){
            // block x, y
            tile_x = Util.getRandomIntInRange(0, this.m_width_in_blocks - 1);
            tile_y = Util.getRandomIntInRange(0, this.m_height_in_blocks - 1);

            // make sure not empty
            if (this.isEmptyTile(tile_x, tile_y)){
                continue;
            }

            // handle to the block
            Block block = this.m_blocks[tile_x][tile_y];

            // direction
            Util.EDirection dir = Util.EDirection.getRandom();

            // target
            int target_x = block.getTileX() + dir.toGridPoint2().x;
            int target_y = block.getTileY() + dir.toGridPoint2().y;

            // check if we can move to target
            if (this.canMoveBlock(block, target_x, target_y)){
                this.moveBlock(block, dir);
                break;
            }
        }
    }
}
