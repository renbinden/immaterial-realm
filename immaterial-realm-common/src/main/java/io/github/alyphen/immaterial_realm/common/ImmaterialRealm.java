package io.github.alyphen.immaterial_realm.common;

import java.util.logging.Logger;

public class ImmaterialRealm {

    private static final ImmaterialRealm instance;

    static {
        instance = new ImmaterialRealm();
    }

    public static ImmaterialRealm getInstance() {
        return instance;
    }

    private ImmaterialRealm() {}

    private Logger logger;

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }

}
