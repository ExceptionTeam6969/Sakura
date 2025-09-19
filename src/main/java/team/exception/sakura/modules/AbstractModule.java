package team.exception.sakura.modules;

import lombok.Getter;

import java.util.List;

public abstract class AbstractModule {

    @Getter
    private String name;

    @Getter
    private Category category;

    @Getter
    private boolean enabled;

    // Method to handle the change in enabled status of the module.
    private List<Runnable> enableConsumers;
    private List<Runnable> disableConsumers;

    public AbstractModule(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public void addEnableConsumer(Runnable consumer) {
        enableConsumers.add(consumer);
    }

    public void addDisableConsumer(Runnable consumer) {
        disableConsumers.add(consumer);
    }

    /**
     * Toggle the enabled status of the module & Invoke method to handle the change.
     * @return the new enabled status of the module.
     */
    public boolean toggle() {
        enabled = !enabled;

        if (enabled) {
            enableConsumers.forEach(Runnable::run);
        } else {
            disableConsumers.forEach(Runnable::run);
        }

        return enabled;
    }

}
