package com.thrashplay.saltar.editor.ui;

import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectIds;
import com.thrashplay.luna.api.engine.GameObjectManager;
import com.thrashplay.luna.api.input.InputManager;
import com.thrashplay.luna.desktop.input.MouseTouchManager;
import com.thrashplay.saltar.Saltar;
import com.thrashplay.saltar.editor.model.IdGenerator;
import com.thrashplay.saltar.editor.model.Project;
import com.thrashplay.saltar.editor.model.ProjectChangeListener;
import com.thrashplay.saltar.editor.screen.GridRenderer;
import com.thrashplay.saltar.editor.tool.PaintbrushToolComponent;

/**
 * Registers and unregisteres navigation components such as a grid and mouse viewport.
 *
 * @author Sean Kleinjung
 */
public class UiGameObjectManager implements ProjectChangeListener {
    private InputManager inputManager;
    private MouseTouchManager leftMouseTouchManager;
    private MouseTouchManager middleMouseTouchManager;
    private GameObjectManager gameObjectManager;
    private IdGenerator idGenerator;

    private GameObject tool;
    private GameObject grid;
    private GameObject viewport;

    public UiGameObjectManager(InputManager inputManager, MouseTouchManager leftMouseTouchManager, MouseTouchManager middleMouseTouchManager, GameObjectManager gameObjectManager, IdGenerator idGenerator) {
        this.inputManager = inputManager;
        this.leftMouseTouchManager = leftMouseTouchManager;
        this.middleMouseTouchManager = middleMouseTouchManager;
        this.gameObjectManager = gameObjectManager;
        this.idGenerator = idGenerator;
    }

    @Override
    public void onProjectChanged(Project oldProject, Project newProject) {
        unregister(tool);
        unregister(grid);
        unregister(viewport);

        if (newProject != null) {
            GameObject tool = new GameObject("saltar-editor-tool");
            tool.addComponent(new PaintbrushToolComponent(newProject, leftMouseTouchManager, gameObjectManager));
            gameObjectManager.register(tool);

            grid = new GameObject(idGenerator.getId("saltar-level-editor-grid"));
            grid.addComponent(new GridRenderer(newProject, newProject.getLevel()));
//            grid.addComponent(new MouseSelectionController(newProject, leftMouseTouchManager, gameObjectManager));
            grid.addComponent(new KeyboardGridNavigationController(inputManager, newProject));
            gameObjectManager.register(grid);

            viewport = new GameObject(GameObjectIds.ID_VIEWPORT);
            viewport.addComponent(new Position(0, 0, Saltar.SCENE_WIDTH, Saltar.SCENE_HEIGHT));
            viewport.addComponent(new MouseViewportController(middleMouseTouchManager, gameObjectManager));
            viewport.addComponent(new SelectedTileTrackingViewportController(newProject, gameObjectManager));
            gameObjectManager.register(viewport);
        } else {
            tool = null;
            grid = null;
            viewport = null;
        }
    }

    private void unregister(GameObject gameObject) {
        if (gameObject != null) {
            gameObjectManager.unregister(gameObject);
        }
    }
}