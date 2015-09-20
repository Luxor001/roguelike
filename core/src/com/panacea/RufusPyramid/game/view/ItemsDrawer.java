package com.panacea.RufusPyramid.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.XmlReader;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.items.Item;

import java.util.HashMap;
import java.util.List;

import javax.rmi.CORBA.Util;

/**
 * Created by Lux on 15/09/2015.
 */
public class ItemsDrawer extends ViewObject {

    private List<Item> items;

    public ItemsDrawer(List<Item> items) {
        this.items = items;
    }
    private HashMap<String,TextureRegion> textures;

    @Override
    public void create() {
        super.create();
        textures = new HashMap<String, TextureRegion>();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        SpriteBatch batch = GameBatch.get();
        batch.begin();
        for (Item item : items) {
            if(item.getPosition() != null) { //if item it's hidden do not show him..
                GridPoint2 absolutePos = Utilities.convertToAbsolutePos(item.getPosition());
                batch.draw(getTexture(item), absolutePos.x, absolutePos.y , 0, 0, Utilities.DEFAULT_BLOCK_WIDTH, Utilities.DEFAULT_BLOCK_HEIGHT, 0.7f, 0.7f, 0); //FIXME:reduce the with of the image by a 30%
            }
        }
        batch.end();
    }

    public TextureRegion getTexture(Item item) { //get the item Texture. if it's not in the cache, load it
        String key;
        key = getKey(item);
        try {
            if (!textures.containsKey(key)) { //if the element it's NOT in the cache, put it
                TextureRegion toAdd = loadTexture(item, key);
                if (toAdd != null)
                    textures.put(key, toAdd);
                else
                    throw new Exception("Texture vuota in ItemsDrawer!"); //texture not found!! DAMMIT!
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return textures.get(key);
    }

    private String getKey(Item item){
        String type = item.getItemType();
        String className = item.getClass().getSimpleName();
        return className+type;
    }

    public TextureRegion loadTexture(Item item,String key){
        TextureRegion itemText=null;
        String type = item.getItemType();
        try {

            XmlReader xml = new XmlReader();
            XmlReader.Element xml_element = xml.parse(Gdx.files.internal(Utilities.ITEMS_XML_PATH));
            String baseClass= item.getClass().getSimpleName();

            XmlReader.Element baseRoot = xml_element.getChildByNameRecursive(baseClass);
            String basePath=baseRoot.get("path");
            int textRegionIndex = Integer.parseInt(baseRoot.getChildByName(type).get("TexturePosition"));
            Texture newTexture = new Texture(Gdx.files.internal("data/"+basePath));

            itemText = loadTextureRegion(newTexture)[textRegionIndex];

        } catch (Exception sxe) {
            sxe.printStackTrace();
        }

        return itemText;
    }

    private static TextureRegion[] loadTextureRegion(Texture text){
        int frameRows = text.getHeight() / Utilities.DEFAULT_BLOCK_HEIGHT;
        int frameCols = text.getWidth() / Utilities.DEFAULT_BLOCK_WIDTH;
        TextureRegion[][] tmp = TextureRegion.split(text, Utilities.DEFAULT_BLOCK_WIDTH, Utilities.DEFAULT_BLOCK_HEIGHT);
        TextureRegion[] newTextRegion = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                TextureRegion tr = tmp[i][j];
                newTextRegion[index++] = tr;
            }
        }
        return newTextRegion;
    }
}
