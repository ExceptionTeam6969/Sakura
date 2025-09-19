package team.exception.sakura.modules.impl;

import team.exception.sakura.modules.AbstractModule;
import team.exception.sakura.modules.Category;

public class TestModule extends AbstractModule {

    private static TestModule INSTANCE = null;

    public static TestModule getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TestModule();
        }
        return INSTANCE;
    }

    public TestModule() {
        super("TestModule", Category.COMBAT);
    }
}
