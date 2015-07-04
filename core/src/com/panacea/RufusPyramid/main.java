package com.panacea.RufusPyramid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;


public class main extends ApplicationAdapter {
	Animator animator;
    GridPoint2 screenLimits;

	@Override
	public void create () {

        TextureRegion region =new TextureRegion();
        animator=new Animator(0.33f,region);
        animator.create();

        screenLimits=new GridPoint2(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(new InputAdapter() { /*Intercettazione degli input dell'utente*/
            @Override
            public boolean touchDown (int x, int y, int pointer, int button) {
                // your touch down code here
                main.x=x;
                main.y= Math.abs(y-screenLimits.y); /*l'origine, secondo lo spritebatch, è in basso a sinistra, pertanto bisogna "aggiustare" come viene interpretato l'input da android*/
                return true;
            }

            @Override
            public boolean touchUp (int x, int y, int pointer, int button) {
                return true;
            }
        });
	}

    int counter=0;
    public static int x,y;
	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        x++;//movimento di prova..
        animator.updateLocation(new GridPoint2(x,y)); //aggiorno la posizione da disegnare della "tile"
        animator.render();

        if(x >= screenLimits.x) //se esci fuori dallo schermo, resetta a 0
            x=0;
	}

}
