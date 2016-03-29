package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

//Sources:
//Orthographic Camera: https://www.youtube.com/watch?v=ikajOOa4EPI
//Tiled and Rendering TiledMap: http://gamedevelopment.tutsplus.com/tutorials/introduction-to-tiled-map-editor-a-great-platform-agnostic-tool-for-making-level-maps--gamedev-2838
//                              https://www.youtube.com/watch?v=MT5YwZsQnF8

public class GamTiledScratch extends ApplicationAdapter {
    private static final int nCols = 4;
    private static final int nRows = 4;

    SpriteBatch batch;
    Texture txSprite;
    //Texture BackGround;
    TextureRegion[] trFrames;
    TextureRegion CurrentFrame;
    float fSpriteX = 0;
    float fSpriteY = 0;
    float fSpriteSpeed = 45f;
    float fTime = 0f;
    Animation aniMain;

    //TiledMap GameMap= new TmxMapLoader().load("IntoTheWoodsRPGMap.tmx");
    TiledMap tmGameMap;
    OrthogonalTiledMapRenderer OrhtoTmrRenderer;
    OrthographicCamera OcCam;
    ArrayList<Rectangle> arlRectCollisionDetection;



    @Override
    public void create() {
        batch = new SpriteBatch();
        //BackGround = new Texture(Gdx.files.internal("lostwoods2.jpg"));
        txSprite = new Texture(Gdx.files.internal("CinderellaSpriteSheet.png"));
        TextureRegion[][] tmp = TextureRegion.split(txSprite, txSprite.getWidth() / nCols, txSprite.getHeight() / nRows);
        trFrames = new TextureRegion[nCols * nRows];
        int index = 0;
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                trFrames[index++] = tmp[i][j];
            }
        }
        aniMain = new Animation(1f, trFrames);

        //Setting Up TiledMap
        tmGameMap= new TmxMapLoader().load("IntoTheWoodsRPGMap.tmx");
        OrhtoTmrRenderer = new OrthogonalTiledMapRenderer(tmGameMap);
        arlRectCollisionDetection = new ArrayList<Rectangle>();

        //Setting Up Orthographic Camera
        OcCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render() {
        //Rendering Sprite
        if (fTime < 4) {
            fTime += Gdx.graphics.getDeltaTime();
        } else {
            fTime = 0;
        }

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        CurrentFrame = aniMain.getKeyFrame(0);

        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
            fSpriteX -= Gdx.graphics.getDeltaTime() * fSpriteSpeed;
            CurrentFrame = aniMain.getKeyFrame(4 + fTime);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
            fSpriteX += Gdx.graphics.getDeltaTime() * fSpriteSpeed;
            CurrentFrame = aniMain.getKeyFrame(8 + fTime);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
            fSpriteY += Gdx.graphics.getDeltaTime() * fSpriteSpeed;
            CurrentFrame = aniMain.getKeyFrame(12 + fTime);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) {
            fSpriteY -= Gdx.graphics.getDeltaTime() * fSpriteSpeed;
            CurrentFrame = aniMain.getKeyFrame(0 + fTime);
        }

        //Rendering Tiled Map
        OrhtoTmrRenderer.setView(OcCam);
        OrhtoTmrRenderer.render();

        //OrthoGraphic Camera
        OcCam.position.set(fSpriteX, fSpriteY, 0);
        batch.setProjectionMatrix(OcCam.combined);
        OcCam.update();

        //Draw Sprites
        batch.begin();
        //batch.draw(BackGround, 0, 0);
        batch.draw(CurrentFrame, (int) fSpriteX, (int) fSpriteY);
        batch.end();
    }

}

