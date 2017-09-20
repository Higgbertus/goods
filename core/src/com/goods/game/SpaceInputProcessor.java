package com.goods.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.goods.game.Space.GameObjectModelInstance;
import com.goods.game.Space.ObjectType;
import com.goods.game.Space.Ships.ShipObjectModelInstance;

import java.util.ArrayList;

/**
 * Created by Higgy on 18.09.2017.
 */

public class SpaceInputProcessor implements InputProcessor {

    // Camera Settings
    private final int zoomSpeed = 15;
    private final int rotateAngle = 90;
    private final float translateUnitsMouse = 400f;
    private final float translateUnitsKey = 10f;
    SpacePerspectiveCamera sPerCam;
    private float startX, startY;
    Vector3 tmpV1 = new Vector3();
    Vector3 tmpV2 = new Vector3();
    private int selected = -1;
    private ObjectType selectedType;
    private boolean isZoominActive = false;
    private GameObjectModelInstance selctedShip, selectedObject, focusedObject;
    private ArrayList<GameObjectModelInstance> staticObjects;
    private ArrayList<ShipObjectModelInstance> dynamicObjects;


    public void setObjects(ArrayList<GameObjectModelInstance> staticObjects, ArrayList<ShipObjectModelInstance> dynamicObjects) {
        this.staticObjects = staticObjects;
        this.dynamicObjects = dynamicObjects;
    }


    // Mouse Buttons
    public boolean leftMB, rightMB, middleMB, fwMB, bwMB;
    public boolean keyW = false, keyS = false, keyA = false, keyD = false;
    /**
     * The target to rotate around.
     */
    public Vector3 target = new Vector3();

    public SpaceInputProcessor(SpacePerspectiveCamera sPerCam) {
        this.sPerCam = sPerCam;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W: {
                //zoom(-1);
                keyW = true;
                return true;
            }
            case Input.Keys.S: {
                //zoom(1);
                keyS = true;
                return true;
            }
            case Input.Keys.A: {
                keyA = true;
                sPerCam.setStickToTarget(false);
                return true;
            }
            case Input.Keys.D: {
                keyD = true;
                sPerCam.setStickToTarget(false);
                return true;
            }
            case Input.Keys.LEFT: {

            }
            case Input.Keys.RIGHT: {

            }
            case Input.Keys.UP: {

            }
            case Input.Keys.DOWN: {

            }
            case Input.Keys.SPACE: {
                sPerCam.position.set(500, 500, 500);
                sPerCam.lookAt(500, 500, 0);
                sPerCam.near = 1f;
                sPerCam.far = 10000f;
                sPerCam.update();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.W: {
                //zoom(-1);
                keyW = false;
                return true;
            }
            case Input.Keys.S: {
                //zoom(1);
                keyS = false;
                return true;
            }
            case Input.Keys.A: {
                keyA = false;
                return true;
            }
            case Input.Keys.D: {
                keyD = false;
                return true;
            }
            case Input.Keys.LEFT: {

            }
            case Input.Keys.RIGHT: {

            }
            case Input.Keys.UP: {

            }
            case Input.Keys.DOWN: {

            }
            case Input.Keys.SPACE: {

            }
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        startX = screenX;
        startY = screenY;
        // Is a Object clicked?
        //selected = getObject(screenX, screenY);
        selected = getObject2(screenX, screenY);

        if (selected < 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // wenn während dem zoomen etwas geklickt wird erstmal stoppen
        isZoominActive = false;
        if (selected >= 0) {
            switch (button) {
                case Input.Buttons.LEFT: {
                    setSelected(getObject2(screenX, screenY));
                    return true;
                }
                case Input.Buttons.RIGHT: {

                    return true;
                }
                case Input.Buttons.MIDDLE: {
                    // zoom button
                    isZoominActive = true;
                    setSelected(getObject2(screenX, screenY));
                    return true;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        isZoominActive = false;
        final float deltaX = (screenX - startX) / Gdx.graphics.getWidth();
        final float deltaY = (startY - screenY) / Gdx.graphics.getHeight();
        startX = screenX;
        startY = screenY;
        tmpV1 = new Vector3();
        tmpV2 = new Vector3();

        // so is es genau die kamera (kopf drehen)
        target = new Vector3(sPerCam.position);

        if (Gdx.input.isButtonPressed(0)) {
//            tmpV1.set(sPerCam.direction).crs(sPerCam.up).y = 0f;
//            sPerCam.rotateAround(target, tmpV1.nor(), deltaY * rotateAngle);
//            sPerCam.rotateAround(target, Vector3.Y, deltaX * -rotateAngle);
            sPerCam.rotateCam(deltaX, deltaY, rotateAngle);
        } else if (Gdx.input.isButtonPressed(1)) {
//            sPerCam.translate(tmpV1.set(sPerCam.direction).crs(sPerCam.up).nor().scl(-deltaX * translateUnitsMouse));
//            sPerCam.translate(tmpV2.set(sPerCam.up).scl(-deltaY * translateUnitsMouse));
            // if (translateTarget) target.add(tmpV1).add(tmpV2);
            sPerCam.setStickToTarget(false);
            sPerCam.moveCam(deltaX, deltaY, translateUnitsMouse);
        }
//        sPerCam.update();
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        isZoominActive = false;
        sPerCam.zoom(amount, zoomSpeed);
        return true;
    }


    public void setSelected(int value) {
        if (selected == value) {
            switch (selectedType) {
                case Ship: {
                    if (isZoominActive) {
                        focusedObject = dynamicObjects.get(value);
                        sPerCam.setTarget(focusedObject);
                    } else {
                        selctedShip = dynamicObjects.get(value);
                    }
                    break;
                }
                case Planet:
                case Star: {
                    if (isZoominActive) {
                        focusedObject = staticObjects.get(value);
                        sPerCam.setTarget(focusedObject);
                    } else {
                        selectedObject = staticObjects.get(value);
                        break;
                    }
                }
            }
        }
    }


    // TODO: 12.09.2017 auch dynamicobjects durchlaufen

    /**
     * @param screenX
     * @param screenY
     * @return gibt den Index zurück oder -1 wenn kein object angeklickt!
     */
    public int getObject2(int screenX, int screenY) {
        Ray ray = sPerCam.getPickRay(screenX, screenY);
        int result = -1;
        float distance = -1;
        for (int i = 0; i < staticObjects.size(); ++i) {
            final float dist2 = staticObjects.get(i).intersects(ray);
            if (dist2 >= 0f && (distance < 0f || dist2 <= distance)) {
                result = i;
                distance = dist2;
                selectedType = staticObjects.get(i).getType();
            }
        }
        for (int i = 0; i < dynamicObjects.size(); ++i) {
            final float dist2 = dynamicObjects.get(i).intersects(ray);
            if (dist2 >= 0f && (distance < 0f || dist2 <= distance)) {
                result = i;
                distance = dist2;
                selectedType = dynamicObjects.get(i).getType();
            }
        }
        return result;
    }

    public void actToPressedKeys( float deltaTime) {
        if (keyW) {
            sPerCam.zoom(-1, zoomSpeed);
        }
        if (keyS) {
            sPerCam.zoom(1, zoomSpeed);
        }
        if (keyA) {
            sPerCam.moveCam(1f, 0, translateUnitsKey);
        }
        if (keyD) {
            sPerCam.moveCam(-1f, 0, translateUnitsKey);
        }
        // move Cam to Position if middle Mousce clicked and Target valid, as long as not distance reached
        if (isZoominActive && focusedObject != null) {
            // TODO: 20.09.2017 beste stop position je Object bestimmen da ship und target unterschiedliche größen haben können
            if (focusedObject.getPosition().dst(sPerCam.position) > focusedObject.getOrbitDistance()+50) {
                sPerCam.moveToTarget(deltaTime, translateUnitsMouse);
            }else{
                isZoominActive = false;
            }
        }
        if (sPerCam.isStickToTarget()) {
           // Auto rotate/move actual Cam Pos to Target
            // TODO: 20.09.2017 umsetzen
            sPerCam.followTarget(deltaTime);

        }
    }
}
