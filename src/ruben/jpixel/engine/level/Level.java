package ruben.jpixel.engine.level;

import org.joml.AABBf;
import org.joml.Intersectionf;
import org.joml.Rectanglef;
import ruben.jpixel.engine.core.IGameObject;
import ruben.jpixel.engine.entity.Entity;
import ruben.jpixel.engine.graphics.Bitmap;
import ruben.jpixel.engine.graphics.Screen;
import ruben.jpixel.engine.graphics.Sprite;
import ruben.jpixel.engine.math.Vec2;
import ruben.jpixel.engine.tile.Tile;
import ruben.jpixel.engine.tile.TilePosition;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Level implements IGameObject {

    private String name;
    private Bitmap level_tile;
    private Bitmap level_data;

    private Tile[] tiles;

    private List<IGameObject> level_objects;
    public Vec2 spawnLocation;

    public Level(String name){
        this.name = name;
        this.level_objects = new ArrayList<>();
        this.spawnLocation = new Vec2();
        loadLevel();
    }

    private void loadLevel(){
        level_tile = new Bitmap("levels/"+name+"_tile.png");
        level_data = new Bitmap("levels/"+name+"_data.png");

        tiles = new Tile[level_tile.getWidth() * level_tile.getHeight()];

        for (int y = 0; y < level_tile.getHeight(); y++) {
            for (int x = 0; x < level_tile.getWidth(); x++) {
                int col = level_tile.getPixel()[x + y * level_tile.getWidth()];
                tiles[x + y * level_tile.getWidth()] = getTile(col, new Vec2(x, y));
            }
        }

        for (int y = 0; y < level_data.getHeight(); y++) {
            for (int x = 0; x < level_data.getWidth(); x++) {
                int col = level_data.getPixel()[x + y * level_data.getWidth()];
                if (col == 0xFFFF0000) spawnLocation.set(x * Tile.SIZE, y * Tile.SIZE);
                if (col == 0xFFFFD800) {
                    Entity coin = new Entity(new Vec2(x * Tile.SIZE, y * Tile.SIZE), Sprite.coin, "coin");
                    add(coin);
                }
            }
        }
    }

    public Entity getEntity(Rectanglef boundingBox) {
        for (int i = 0; i < level_objects.size(); i++) {
            if (Intersectionf.testAarAar(
                    ((Entity)level_objects.get(i)).getBoundingBox().minX, ((Entity)level_objects.get(i)).getBoundingBox().minY,
                    ((Entity)level_objects.get(i)).getBoundingBox().maxX, ((Entity)level_objects.get(i)).getBoundingBox().maxY,
                    boundingBox.minX, boundingBox.minY,
                    boundingBox.maxX, boundingBox.maxY))
                return (Entity) level_objects.get(i);
        }

        return null;
    }

    private Tile getTile(int colour, Vec2 position){
        if (colour == 0xFF4CFF00) return new Tile(new TilePosition(position), Sprite.grass, "grass");
        if (colour == 0xFF7F3300) return new Tile(new TilePosition(position), Sprite.stone, "stone").setSolid(true);
        if (colour == 0xFFFF6A00) return new Tile(new TilePosition(position), Sprite.wood, "wood");

        return new Tile(new TilePosition(position), new Sprite(0xff00ff, Tile.SIZE, Tile.SIZE), "void");
    }

    @Override
    public void update() {
        for (int i = 0; i < tiles.length; i++) {
            tiles[i].update();
        }

        for (int i = 0; i < level_objects.size(); i++) {
            level_objects.get(i).update();
        }
    }

    @Override
    public void render(Screen screen) {
        for (int i = 0; i < tiles.length; i++) {
            screen.draw(tiles[i]);
        }

        for (int i = 0; i < level_objects.size(); i++) {
            level_objects.get(i).render(screen);
        }
    }

    @Override
    public void setLevel(Level level) {}

    @Override
    public Vec2 getPosition() {
        return null;
    }

    @Override
    public TilePosition getTilePosition() {
        return null;
    }

    public void add(IGameObject object){
        if (!(object instanceof Level)) {
            this.level_objects.add(object);
            object.setLevel(this);
        }
    }

    public Vec2 getSpawnLocation() {
        return spawnLocation;
    }

    public Tile getTile(Rectanglef boundingBox) {
        for (int i = 0; i < tiles.length; i++) {
            if (Intersectionf.testAarAar(
                    tiles[i].getBoundingBox().minX, tiles[i].getBoundingBox().minY,
                    tiles[i].getBoundingBox().maxX, tiles[i].getBoundingBox().maxY,
                    boundingBox.minX, boundingBox.minY,
                    boundingBox.maxX, boundingBox.maxY))
            return tiles[i];
        }
        return  null;
    }
}
