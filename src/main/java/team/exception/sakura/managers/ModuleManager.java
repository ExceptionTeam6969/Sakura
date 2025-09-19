package team.exception.sakura.managers;

import lombok.Getter;
import team.exception.sakura.modules.AbstractModule;
import team.exception.sakura.modules.impl.*;

import java.util.ArrayList;

public class ModuleManager {

    private static ModuleManager INSTANCE = null;

    @Getter
    private ArrayList<AbstractModule> modules = new ArrayList<>();

    public static ModuleManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ModuleManager();
        }
        return INSTANCE;
    }

    private ModuleManager() {
        loadModules();
    }

    private void loadModules() {
        modules.add(TestModule.getInstance());
    }


}
