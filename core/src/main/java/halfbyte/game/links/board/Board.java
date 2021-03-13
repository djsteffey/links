package halfbyte.game.links.board;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import halfbyte.game.links.Constants;
import halfbyte.game.links.Util;

public class Board extends Group {
    private static final int SHUFFLE_MOVES = 1;

    public enum EType{
        SIZE_4x4,
        SIZE_5x5,
        SIZE_6x6;
        public int getSize(){
            int size = 0;
            switch (this){
                case SIZE_4x4: size = 4; break;
                case SIZE_5x5: size = 5; break;
                case SIZE_6x6: size = 6; break;
            }
            return size;
        }
    }

    public interface IListener{
        void onMoveComplete();
        void onGameComplete();
    }

    // variables
    private AssetManager m_asset_manager;
    private int m_width_in_blocks;
    private int m_height_in_blocks;
    private List<Block> m_blocks;
    private Block.EType[][] m_solution;
    private Block m_selected_block;
    private IListener m_listener;

    // methods
    public Board(AssetManager am, int width_in_blocks, int height_in_blocks, int seed, IListener listener){
        // save
        this.m_asset_manager = am;
        this.m_width_in_blocks = width_in_blocks;
        this.m_height_in_blocks = height_in_blocks;
        this.m_listener = listener;

        // size
        this.setSize(this.m_width_in_blocks * Constants.TILE_SIZE, this.m_width_in_blocks * Constants.TILE_SIZE);

        // background
        this.makeBackground(am);

        // random
        Random rand = new Random(seed);

        // create the blocks
        this.createBlocks(rand);

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

        // randomize
        this.randomize(rand, SHUFFLE_MOVES);

        // no selected block
        this.m_selected_block = null;
    }

    public void reset(){
        // put all tiles back to original
        for (Block block : this.m_blocks){
            block.getActions().clear();
            block.restoreToOriginal();
        }
    }

    public int getWidthInBlocks(){
        return this.m_width_in_blocks;
    }

    public int getHeightInBlocks(){
        return this.m_height_in_blocks;
    }

    public Block.EType[][] getSolution(){
        return this.m_solution;
    }

    private void makeBackground(AssetManager am){
        // create a background image and set it
        Image background = new Image(new NinePatch(am.get("panel_brown.png", Texture.class), 32, 32, 32, 32));
        background.setSize(this.getWidth(), this.getHeight());
        this.addActor(background);
        background.toBack();
    }

    private Block getBlockAtTile(int tile_x, int tile_y){
        // make sure valid
        if (this.isValidTile(tile_x, tile_y) == false){
            // not valid
            return null;
        }

        // loop through all blocks looking for it
        for (Block block : this.m_blocks){
            if (block.getCurrentTileX() == tile_x && block.getCurrentTileY() == tile_y){
                // found
                return block;
            }
        }

        // not found
        return null;
    }

    private void onTouchDown(float x, float y){
        // get the tile clicked on
        int tile_x = (int)(x / Constants.TILE_SIZE);
        int tile_y = (int)(y / Constants.TILE_SIZE);

        // get the block
        this.m_selected_block = this.getBlockAtTile(tile_x, tile_y);
    }

    private void onFling(float vx, float vy){
        // doesnt matter if no block selected
        if (this.m_selected_block == null){
            return;
        }

        // get the direction
        Util.EDirection dir = Util.EDirection.NONE;
        if (Math.abs(vx) > Math.abs(vy)){
            // more in the x direction
            if (vx < -250.0f){
                // move left
                dir = Util.EDirection.LEFT;
            }
            else if (vx > 250.0f){
                // move right
                dir = Util.EDirection.RIGHT;
            }
        }
        else if (Math.abs(vy) > Math.abs(vx)){
            // more in the y direction
            if (vy < -250.0f){
                // move down
                dir = Util.EDirection.DOWN;
            }
            else if (vy > 250.0f){
                // move up
                dir = Util.EDirection.UP;
            }
        }

        // move it
        this.moveBlock(this.m_selected_block, dir);

        // no more selected
        this.m_selected_block = null;
    }

    private void moveBlock(Block block, Util.EDirection dir){
        // nothing to do if no direction
        if (dir == Util.EDirection.NONE){
            return;
        }

        // get target tile
        int target_x = block.getCurrentTileX() + dir.toGridPoint2().x;
        int target_y = block.getCurrentTileY() + dir.toGridPoint2().y;

        // see if we can move there
        if (this.canMoveBlock(block, target_x, target_y) == false){
            // cant do it
            return;
        }

        // we can move there
        // reassign the tile
        block.setCurrentTile(target_x, target_y);

        // animate
        block.addAction(Actions.sequence(
                Actions.moveTo(
                        block.getCurrentTileX() * Constants.TILE_SIZE,
                        block.getCurrentTileY() * Constants.TILE_SIZE,
                        0.15f
                ),
                new Action() {
                    @Override
                    public boolean act(float delta) {
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
            if (this.isOneDistance(tile_x, tile_y, other.getCurrentTileX(), other.getCurrentTileY()) == false){
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
        return this.getBlockAtTile(tile_x, tile_y) == null;
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

    private boolean isSolutionMatched(){
        // check if matches solution
        for (int y = 0; y < this.m_height_in_blocks; ++y) {
            for (int x = 0; x < this.m_width_in_blocks; ++x) {
                // handle to the block
                Block block = this.getBlockAtTile(x, y);

                // check if we have a block
                if (block != null){
                    // make sure block matches solution
                    if (block.getType() != this.m_solution[x][y]){
                        // no match
                        return false;
                    }
                }
                else{
                    // no block so make sure solution is NONE
                    if (this.m_solution[x][y] != Block.EType.NONE){
                        // no match
                        return false;
                    }
                }
            }
        }

        // all match
        return true;
    }

    private void checkAfterBlockMove(){
        this.m_listener.onMoveComplete();

        // check for solution
        if (this.isSolutionMatched()){
            this.m_listener.onGameComplete();
        }
    }

    private void randomize(Random rand, int iterations){
        // loop number of iterations
        while (iterations > 0) {
            // decrement iterations
            iterations -= 1;

            // try 10000 times to find a move
            for (int i = 0; i < 10000; ++i){
                // block x, y that we want to move
                int tile_x = Util.getRandomIntInRange(rand, 0, this.m_width_in_blocks - 1);
                int tile_y = Util.getRandomIntInRange(rand, 0, this.m_height_in_blocks - 1);

                // handle to the block there
                Block block = this.getBlockAtTile(tile_x, tile_y);

                // check if we got a block
                if (block == null){
                    // no block there
                    continue;
                }

                // random direction
                Util.EDirection dir = Util.EDirection.getRandom(rand);

                // target tile
                int target_x = block.getCurrentTileX() + dir.toGridPoint2().x;
                int target_y = block.getCurrentTileY() + dir.toGridPoint2().y;

                // check if we can move to target
                if (this.canMoveBlock(block, target_x, target_y)) {
                    block.setCurrentTile(target_x, target_y);
                    break;
                }
            }
        }

        // shuffling is done; set all blocks as their original at current
        for (Block block : this.m_blocks){
            block.setCurrentAsOriginal();
            block.restoreToOriginal();
        }
    }

    private void createBlocks(Random rand){
        // two iterations at half length
        // create array
        this.m_blocks = new ArrayList<>();

        // init the solution
        this.m_solution = new Block.EType[this.m_width_in_blocks][this.m_height_in_blocks];
        for (int y = 0; y < this.m_height_in_blocks; ++y){
            for (int x = 0; x < this.m_width_in_blocks; ++x){
                this.m_solution[x][y] = Block.EType.NONE;
            }
        }

        // num to chain together
        int total_tiles = this.m_width_in_blocks * this.m_height_in_blocks;
        int length = Util.getRandomIntInRange(rand, (int)(total_tiles * 0.35f), (int)(total_tiles * 0.45f));
        this.createBlocksChain(rand, length / 2);
        this.createBlocksChain(rand, length / 2);
    }

    private void createBlocksChain(Random rand, int length){
        // find an empty for initial block
        int tile_x = -1;
        int tile_y = -1;
        boolean found = false;
        for (int i = 0; i < 10000; ++i){
            tile_x = Util.getRandomIntInRange(rand, 0, this.m_width_in_blocks - 1);
            tile_y = Util.getRandomIntInRange(rand, 0, this.m_height_in_blocks - 1);
            if (this.isValidTile(tile_x, tile_y) && this.isEmptyTile(tile_x, tile_y)){
                found = true;
                break;
            }
        }

        // make sure we got one
        if (found == false){
            // out of luck
            return;
        }

        // create initial block
        Block block = new Block(
                Block.EType.getRandom(rand),
                this.m_asset_manager,
                tile_x,
                tile_y
        );
        this.m_solution[block.getCurrentTileX()][block.getCurrentTileY()] = block.getType();
        this.m_blocks.add(block);
        this.addActor(block);

        // one block down
        length -= 1;


        // loop while keep making length
        while (length > 0){
            // decrement length
            length -= 1;

            // find a neighbor of block
            Block other = this.createRandomNeighbor(rand, block);

            // make sure we got one
            if (other != null){
                BlockLink link = new BlockLink(block, other);
                block.getLinks().add(link);
                other.getLinks().add(link);
                this.addActor(link);
            }
            if (other == null){
                // couldnt make a neighbor so bail?
                break;
            }

            // next
            block = other;
        }
    }

    private Block createRandomNeighbor(Random rand, Block block){
        // build candidate target coords
        List<GridPoint2> coords = new ArrayList<>();
        for (int y = -1; y <= 1; ++y) {
            for (int x = -1; x <= 1; ++x) {
                // calculate target
                int target_x = block.getCurrentTileX() + x;
                int target_y = block.getCurrentTileY() + y;

                // check if valid
                if (this.isValidTile(target_x, target_y) == false){
                    // nope
                    continue;
                }

                // check if empty
                if (this.isEmptyTile(target_x, target_y) == false){
                    // nope
                    continue;
                }

                // offset from block is x, y
                if ((x == -1 && y == -1) || (x == -1 && y == 1) || (x == 1 && y == -1) || (x == 1 && y == 1)) {
                    // lower left
                    if (this.isEmptyTile(target_x, block.getCurrentTileY()) == false && this.isEmptyTile(block.getCurrentTileX(), target_y) == false){
                        // cross diagonal
                        continue;
                    }
                }

                // candidate target
                coords.add(new GridPoint2(target_x, target_y));
            }
        }

        // make sure we got some coords
        if (coords.size() == 0){
            // couldnt make one
            return null;
        }

        // pick a random coord and make block there
        GridPoint2 gp = coords.get(Util.getRandomIntInRange(rand, 0, coords.size() - 1));
        Block other = new Block(Block.EType.getRandom(rand), this.m_asset_manager, gp.x, gp.y);
        this.m_solution[other.getCurrentTileX()][other.getCurrentTileY()] = other.getType();
        this.m_blocks.add(other);
        this.addActor(other);

        // done
        return other;
    }
}
