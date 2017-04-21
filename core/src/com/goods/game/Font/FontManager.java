package com.goods.game.Font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontManager {
    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    public FontManager() {
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/myfont.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    }

    public void createFont(){
        parameter.size = 12;
        BitmapFont font12 = generator.generateFont(parameter); // font size 12 pixels
    }

    public void createText(){

    }


 // don't forget to dispose to avoid memory leaks!
}
