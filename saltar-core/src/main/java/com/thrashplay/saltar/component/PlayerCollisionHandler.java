package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.Config;
import com.thrashplay.luna.api.component.CollisionHandler;
import com.thrashplay.luna.api.component.Movement;
import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.component.RenderableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.geom.Rectangle;
import com.thrashplay.luna.api.graphics.Graphics;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class PlayerCollisionHandler implements CollisionHandler, RenderableComponent {

    private boolean[] collisionDirections = new boolean[4];
    private boolean unknownCollision = false;

    @Override
    public void handleCollision(GameObject ourObject, GameObject otherObject, Rectangle ourBoundingBox, Rectangle otherBoundingBox, boolean[] directions) {
        Player player = ourObject.getComponent(Player.class);
        Position position = ourObject.getComponent(Position.class);
        Movement movement = ourObject.getComponent(Movement.class);

        int direction = getCollidingDirectionWithShortestResolutionDistance(ourBoundingBox, otherBoundingBox, position, directions);

        switch (direction) {
            case CollisionHandler.DIRECTION_LEFT:
                collisionDirections[DIRECTION_LEFT] = true;
                position.setX(otherBoundingBox.getRight() + 1);
                if (movement.getVelocityX() < 0) {
                    movement.setVelocityX(0);
                }
                player.setAnimationState(Player.AnimationState.IdleFacingLeft);
                break;

            case CollisionHandler.DIRECTION_TOP:
                collisionDirections[DIRECTION_TOP] = true;
                position.setY(otherBoundingBox.getBottom() + 1);
                if (movement.getVelocityY() < 0) {
                    movement.setVelocityY(0);
                }
                break;

            case CollisionHandler.DIRECTION_RIGHT:
                collisionDirections[DIRECTION_RIGHT] = true;
                position.setX(otherBoundingBox.getLeft() - ourBoundingBox.getWidth() - 1);
                if (movement.getVelocityX() > 0) {
                    movement.setVelocityX(0);
                }
                player.setAnimationState(Player.AnimationState.IdleFacingRight);
                break;

            case CollisionHandler.DIRECTION_BOTTOM:
                collisionDirections[DIRECTION_BOTTOM] = true;
                position.setY(otherBoundingBox.getY() - ourBoundingBox.getHeight());
                if (movement.getVelocityY() > 0) {
                    movement.setVelocityY(0);
                }
                break;

            case CollisionHandler.DIRECTION_UNKNOWN:   // default to treating it as if we are standing on top of the block    case Bottom:
                unknownCollision = true;
        }
    }

    private int getCollidingDirectionWithShortestResolutionDistance(Rectangle ourBoundingBox, Rectangle otherBoundingBox, Position position, boolean[] collisionDirections) {
        int shortestDistance = Integer.MAX_VALUE;
        int direction = CollisionHandler.DIRECTION_UNKNOWN;
        int distance;

        // check left
        if (collisionDirections[CollisionHandler.DIRECTION_LEFT]) {
            distance = Math.abs(position.getX() - (otherBoundingBox.getRight() + 1));
            if (distance < shortestDistance) {
                shortestDistance = distance;
                direction = CollisionHandler.DIRECTION_LEFT;
            }
        }

        // check top
        if (collisionDirections[CollisionHandler.DIRECTION_TOP]) {
            distance = Math.abs(position.getY() - (otherBoundingBox.getBottom() + 1));
            if (distance < shortestDistance) {
                shortestDistance = distance;
                direction = CollisionHandler.DIRECTION_TOP;
            }
        }

        // check right
        if (collisionDirections[CollisionHandler.DIRECTION_RIGHT]) {
            distance = Math.abs(position.getX() - (otherBoundingBox.getLeft() - ourBoundingBox.getWidth() - 1));
            if (distance < shortestDistance) {
                shortestDistance = distance;
                direction = CollisionHandler.DIRECTION_RIGHT;
            }
        }

        // check bottom
        if (collisionDirections[CollisionHandler.DIRECTION_BOTTOM]) {
            distance = Math.abs(position.getY() - (otherBoundingBox.getY() - ourBoundingBox.getHeight() - 1));
            if (distance < shortestDistance) {
                shortestDistance = distance;
                direction = CollisionHandler.DIRECTION_BOTTOM;
            }
        }

        return direction;
    }

    @Override
    public void render(Graphics graphics, GameObject gameObject) {
        if (Config.renderCollisions) {
            Position position = gameObject.getComponent(Position.class);

            if (collisionDirections[CollisionHandler.DIRECTION_LEFT]) {
                graphics.fillCircle(position.getLeft(), position.getCenterY(), 9, 0xffff0000);
            }
            if (collisionDirections[CollisionHandler.DIRECTION_RIGHT]) {
                graphics.fillCircle(position.getRight(), position.getCenterY(), 9, 0xffff0000);
            }
            if (collisionDirections[CollisionHandler.DIRECTION_TOP]) {
                graphics.fillCircle(position.getCenterX(), position.getTop(), 9, 0xffff0000);
            }
            if (collisionDirections[CollisionHandler.DIRECTION_BOTTOM]) {
                graphics.fillCircle(position.getCenterX(), position.getBottom(), 9, 0xffff0000);
            }
        }

        // reset collision directions for next frame
        collisionDirections[0] = collisionDirections[1] = collisionDirections[2] = collisionDirections[3] = unknownCollision = false;
    }
}
