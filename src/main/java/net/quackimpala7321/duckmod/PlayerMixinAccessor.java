package net.quackimpala7321.duckmod;

public interface PlayerMixinAccessor {
    DuckBarManager getDuckBarManager();

    boolean isGliding();
    void setGliding(boolean set);
}
