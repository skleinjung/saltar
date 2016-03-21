package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.component.Movement;
import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.input.InputManager;
import com.thrashplay.luna.api.input.KeyCode;
import com.thrashplay.luna.api.math.MathUtils;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class KeyboardMovementController implements UpdateableComponent {

    private InputManager inputManager;

    public KeyboardMovementController(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public void update(GameObject gameObject, long delta) {
        Player player = gameObject.getComponent(Player.class);
        Position position = gameObject.getComponent(Position.class);
        Movement movement = gameObject.getComponent(Movement.class);

        boolean horizontalKeyDown = false;
        if (inputManager.isKeyDown(KeyCode.KEY_LEFT_ARROW) || inputManager.isKeyDown(KeyCode.KEY_S)) {
//            position.setX(position.getX() - 5);
            //movement.setAccelerationX(-1);
            movement.setVelocityX(-5);
            horizontalKeyDown = true;

            player.onLeftPressed();
        }

        if (inputManager.isKeyDown(KeyCode.KEY_RIGHT_ARROW) || inputManager.isKeyDown(KeyCode.KEY_F)) {
//            position.setX(position.getX() + 5);
//            movement.setAccelerationX(1);
            movement.setVelocityX(5);
            horizontalKeyDown = true;

            player.onRightPressed();
        }

        if (!horizontalKeyDown) {
//            movement.setVelocityX(0);
            movement.setAccelerationX(-MathUtils.sign(movement.getVelocityX() / 5));

            player.onHorizontalKeyRelased();
        }

        if (inputManager.isKeyDown(KeyCode.KEY_SPACE)) {
            movement.setVelocityY(-10);
        }
    }
}
