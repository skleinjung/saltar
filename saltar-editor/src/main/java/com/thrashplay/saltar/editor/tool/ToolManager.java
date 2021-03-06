package com.thrashplay.saltar.editor.tool;

import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectManager;
import com.thrashplay.luna.desktop.input.MouseTouchManager;
import com.thrashplay.saltar.editor.model.GameObjectFactory;
import com.thrashplay.saltar.editor.model.Project;
import com.thrashplay.saltar.editor.model.ProjectChangeListener;
import com.thrashplay.saltar.editor.ui.GameObjectGridSelectionManager;
import com.thrashplay.saltar.editor.ui.ToolType;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Registers and unregisteres navigation components such as a grid and mouse viewport.
 *
 * @author Sean Kleinjung
 */
public class ToolManager implements ProjectChangeListener {
    private MouseTouchManager leftMouseTouchManager;
    private GameObjectGridSelectionManager gameObjectGridSelectionManager;
    private GameObjectManager gameObjectManager;
    private GameObjectFactory gameObjectFactory;

    private GameObject tool;

    public ToolManager(GameObjectFactory gameObjectFactory, GameObjectManager gameObjectManager, GameObjectGridSelectionManager gameObjectGridSelectionManager, MouseTouchManager leftMouseTouchManager) {
        this.gameObjectFactory = gameObjectFactory;
        this.gameObjectManager = gameObjectManager;
        this.gameObjectGridSelectionManager = gameObjectGridSelectionManager;
        this.leftMouseTouchManager = leftMouseTouchManager;
    }

    @Override
    public void onProjectChanged(Project oldProject, Project newProject) {
        unregister(tool);

        if (newProject != null) {
            tool = new GameObject("saltar-editor-tool");
            gameObjectManager.register(tool);

            newProject.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent event) {
                    Project project = (Project) event.getSource();

                    // when the tool changes, update our tool component accordingly
                    if ("selectedTool".equals(event.getPropertyName()) && tool != null) {
                        tool.removeComponent(SelectionToolComponent.class);
                        tool.removeComponent(PaintbrushToolComponent.class);
                        tool.removeComponent(PlaceMonsterToolComponent.class);
                        tool.removeComponent(EraseToolComponent.class);
                        tool.removeComponent(StartPositionToolComponent.class);

                        ToolType toolType = (ToolType) event.getNewValue();
                        switch (toolType) {
                            case Select:
                                tool.addComponent(new SelectionToolComponent(project, leftMouseTouchManager, gameObjectManager));
                                break;
                            case Paint:
                                tool.addComponent(new PaintbrushToolComponent(project, leftMouseTouchManager, gameObjectManager, gameObjectFactory));
                                break;
                            case Erase:
                                tool.addComponent(new EraseToolComponent(project, gameObjectGridSelectionManager, leftMouseTouchManager, gameObjectManager));
                                break;
                            case Monster:
                                tool.addComponent(new PlaceMonsterToolComponent(project, leftMouseTouchManager, gameObjectManager, gameObjectFactory));
                                break;
                            case StartPosition:
                                tool.addComponent(new StartPositionToolComponent(project, leftMouseTouchManager, gameObjectManager));
                                break;
                        }
                    }
                }
            });
        } else {
            tool = null;
        }
    }

    private void unregister(GameObject gameObject) {
        if (gameObject != null) {
            gameObjectManager.unregister(gameObject);
        }
    }
}
