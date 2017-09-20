package com.goods.game;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
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
    private boolean stickToTarget;


    public boolean isStickToTarget() {
        return stickToTarget;
    }

    public void setStickToTarget(boolean stickToTarget) {
        this.stickToTarget = stickToTarget;
    }

    public void moveToTarget(float deltaTime, float speed){
        tmpV1 = new Vector3();
        // rotate to target
        this.lookAt(target.getPosition());
        this.update();

        // move to Target with speed...
        this.translate(tmpV1.set(this.direction).scl(speed*deltaTime));

        if (target.getPosition().dst(this.position) <= target.getOrbitDistance() +50) {
            stickToTarget = true;
        }
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


    public void zoom(int direction, float zoomSpeed){
        tmpV1 = new Vector3(this.position);
        if (direction < 0)
            this.translate(tmpV1.set(this.direction).scl(direction + zoomSpeed));
        else
            this.translate(tmpV1.set(this.direction).scl(direction - zoomSpeed));
        update();
    }

    public void followTarget(float deltaTime) {
        // Move Camera with target Movements
        // Allow rotation, zoom

    }
}
