package com.goods.game;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.goods.game.Space.GameObjectModelInstance;

/**
 * Created by Higgy on 19.09.2017.
 */

public class SpacePerspectiveCamera extends PerspectiveCamera {

    Vector3 tmpV1, tmpV2;

    public SpacePerspectiveCamera(float fieldOfViewY, float viewportWidth, float viewportHeight) {
        super(fieldOfViewY, viewportWidth, viewportHeight);
    }

    public void setTarget(GameObjectModelInstance target) {
        this.target = target;
    }

    private GameObjectModelInstance target;


    public void moveToTarget(){

    }

    public void rotateAroundTarget(){

    }



    public void rotateCam(float deltaX, float deltaY, int rotateAngle){
        tmpV1 = new Vector3();
        tmpV2 = new Vector3();
        tmpV2 = new Vector3(this.position);
        tmpV1.set(this.direction).crs(this.up).y = 0f;
        this.rotateAround(tmpV2, tmpV1.nor(), deltaY * rotateAngle);
        this.rotateAround(tmpV2, Vector3.Y, deltaX * -rotateAngle);
        update();
    }

    public void moveCam(float deltaX, float deltaY, float translateUnits){
        tmpV1 = new Vector3();
        tmpV2 = new Vector3();
        this.translate(tmpV1.set(this.direction).crs(this.up).nor().scl(-deltaX * translateUnits));
        this.translate(tmpV2.set(this.up).scl(-deltaY * translateUnits));
        update();
    }

    public void stickToTarget(){

    }

    public void zoom(int direction, float zoomSpeed){
        tmpV1 = new Vector3(this.position);
        if (direction < 0)
            this.translate(tmpV1.set(this.direction).scl(direction + zoomSpeed));
        else
            this.translate(tmpV1.set(this.direction).scl(direction - zoomSpeed));
        update();
    }
}
