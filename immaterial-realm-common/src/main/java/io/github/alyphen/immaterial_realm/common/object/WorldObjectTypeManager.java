package io.github.alyphen.immaterial_realm.common.object;

import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;

import javax.script.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.github.alyphen.immaterial_realm.common.util.FileUtils.loadMetadata;
import static io.github.alyphen.immaterial_realm.common.util.FileUtils.read;
import static java.util.logging.Level.SEVERE;

public class WorldObjectTypeManager {

    private ImmaterialRealm immaterialRealm;
    private Map<String, WorldObjectType> types;

    public WorldObjectTypeManager(ImmaterialRealm immaterialRealm) {
        this.immaterialRealm = immaterialRealm;
        types = new HashMap<>();
    }

    public void loadObjectTypes() throws IOException {
        File objectTypesDirectory = new File("./objects");
        for (File objectDirectory : objectTypesDirectory.listFiles(File::isDirectory)) {
            File propertiesFile = new File(objectDirectory, "object.json");
            Map<String, Object> properties = loadMetadata(propertiesFile);
            types.put((String) properties.get("name"), new WorldObjectType() {

                private CompiledScript script;

                {
                    setObjectName((String) properties.get("name"));
                    String spriteName = (String) properties.get("sprite");
                    setObjectSprite(spriteName.equals("none") ? null : immaterialRealm.getSpriteManager().getSprite(spriteName));
                    setObjectBounds(new Rectangle((int) ((double) properties.get("bounds_offset_x")), (int) ((double) properties.get("bounds_offset_y")), (int) ((double) properties.get("bounds_width")), (int) ((double) properties.get("bounds_height"))));
                    File jsFile = new File(objectDirectory, "object.js");
                    File rbFile = new File(objectDirectory, "object.rb");
                    File pyFile = new File(objectDirectory, "object.py");
                    if (jsFile.exists()) {
                        try {
                            ScriptEngine engine = immaterialRealm.getScriptEngineManager().getEngineByExtension("js");
                            script = ((Compilable) engine).compile(read(jsFile));
                            script.eval();
                        } catch (ScriptException | FileNotFoundException exception) {
                            immaterialRealm.getLogger().log(SEVERE, "Failed to compile script", exception);
                        }
                    } else if (rbFile.exists()) {
                        try {
                            ScriptEngine engine = immaterialRealm.getScriptEngineManager().getEngineByExtension("rb");
                            script = ((Compilable) engine).compile(read(rbFile));
                            script.eval();
                        } catch (ScriptException | FileNotFoundException exception) {
                            immaterialRealm.getLogger().log(SEVERE, "Failed to compile script", exception);
                        }
                    } else if (pyFile.exists()) {
                        try {
                            ScriptEngine engine = immaterialRealm.getScriptEngineManager().getEngineByExtension("py");
                            script = ((Compilable) engine).compile(read(pyFile));
                            script.eval();
                        } catch (ScriptException | FileNotFoundException exception) {
                            immaterialRealm.getLogger().log(SEVERE, "Failed to compile script", exception);
                        }
                    }
                }

                @Override
                public WorldObject initialize(UUID uuid) {
                    return new WorldObject(uuid, getObjectName(), getObjectSprite(), getObjectBounds()) {

                        {
                            File jsFile = new File(objectDirectory, "object.js");
                            File rbFile = new File(objectDirectory, "object.rb");
                            File pyFile = new File(objectDirectory, "object.py");
                            if (jsFile.exists()) {
                                try {
                                    ScriptEngine engine = immaterialRealm.getScriptEngineManager().getEngineByExtension("js");
                                    ((Invocable) engine).invokeFunction("create");
                                } catch (ScriptException exception) {
                                    immaterialRealm.getLogger().log(SEVERE, "Failed to invoke object creation function", exception);
                                } catch (NoSuchMethodException ignored) {
                                }
                            } else if (rbFile.exists()) {
                                try {
                                    ScriptEngine engine = immaterialRealm.getScriptEngineManager().getEngineByExtension("rb");
                                    ((Invocable) engine).invokeFunction("create");
                                } catch (ScriptException exception) {
                                    immaterialRealm.getLogger().log(SEVERE, "Failed to invoke object creation function", exception);
                                } catch (NoSuchMethodException ignored) {
                                }
                            } else if (pyFile.exists()) {
                                try {
                                    ScriptEngine engine = immaterialRealm.getScriptEngineManager().getEngineByExtension("py");
                                    ((Invocable) engine).invokeFunction("create");
                                } catch (ScriptException exception) {
                                    immaterialRealm.getLogger().log(SEVERE, "Failed to invoke object creation function", exception);
                                } catch (NoSuchMethodException ignored) {
                                }
                            }
                        }

                        @Override
                        public void onInteract() {
                            File jsFile = new File(objectDirectory, "object.js");
                            File rbFile = new File(objectDirectory, "object.rb");
                            File pyFile = new File(objectDirectory, "object.py");
                            if (jsFile.exists()) {
                                try {
                                    ScriptEngine engine = immaterialRealm.getScriptEngineManager().getEngineByExtension("js");
                                    ((Invocable) engine).invokeFunction("interact");
                                } catch (ScriptException exception) {
                                    immaterialRealm.getLogger().log(SEVERE, "Failed to invoke object interaction function", exception);
                                } catch (NoSuchMethodException ignored) {
                                }
                            } else if (rbFile.exists()) {
                                try {
                                    ScriptEngine engine = immaterialRealm.getScriptEngineManager().getEngineByExtension("rb");
                                    ((Invocable) engine).invokeFunction("interact");
                                } catch (ScriptException exception) {
                                    immaterialRealm.getLogger().log(SEVERE, "Failed to invoke object interaction function", exception);
                                } catch (NoSuchMethodException ignored) {
                                }
                            } else if (pyFile.exists()) {
                                try {
                                    ScriptEngine engine = immaterialRealm.getScriptEngineManager().getEngineByExtension("py");
                                    ((Invocable) engine).invokeFunction("interact");
                                } catch (ScriptException exception) {
                                    immaterialRealm.getLogger().log(SEVERE, "Failed to invoke object interaction function");
                                } catch (NoSuchMethodException ignored) {
                                }
                            }
                        }

                        @Override
                        public void onTick() {
                            super.onTick();
                            File jsFile = new File(objectDirectory, "object.js");
                            File rbFile = new File(objectDirectory, "object.rb");
                            File pyFile = new File(objectDirectory, "object.py");
                            if (jsFile.exists()) {
                                try {
                                    ScriptEngine engine = immaterialRealm.getScriptEngineManager().getEngineByExtension("js");
                                    ((Invocable) engine).invokeFunction("tick");
                                } catch (ScriptException exception) {
                                    immaterialRealm.getLogger().log(SEVERE, "Failed to invoke object tick function", exception);
                                } catch (NoSuchMethodException ignored) {
                                }
                            } else if (rbFile.exists()) {
                                try {
                                    ScriptEngine engine = immaterialRealm.getScriptEngineManager().getEngineByExtension("rb");
                                    ((Invocable) engine).invokeFunction("tick");
                                } catch (ScriptException exception) {
                                    immaterialRealm.getLogger().log(SEVERE, "Failed to invoke object tick function", exception);
                                } catch (NoSuchMethodException ignored) {
                                }
                            } else if (pyFile.exists()) {
                                try {
                                    ScriptEngine engine = immaterialRealm.getScriptEngineManager().getEngineByExtension("py");
                                    ((Invocable) engine).invokeFunction("tick");
                                } catch (ScriptException exception) {
                                    immaterialRealm.getLogger().log(SEVERE, "Failed to invoke object tick function", exception);
                                } catch (NoSuchMethodException ignored) {
                                }
                            }
                        }

                    };
                }

            });
        }
    }

    public void addObjectType(WorldObjectType type) {
        types.put(type.getObjectName(), type);
    }

    public Collection<WorldObjectType> getObjectTypes() {
        return types.values();
    }

    public WorldObjectType getObjectType(String type) {
        return types.get(type);
    }

    public void removeObjectType(String type) {
        types.remove(type);
    }

    public void clearObjectTypes() {
        types.clear();
    }

    public void saveDefaultObjectTypes() throws IOException {
        File objectsDirectory = new File("./objects");
        if (!objectsDirectory.isDirectory()) {
            objectsDirectory.delete();
        }
        if (!objectsDirectory.exists()) {
            objectsDirectory.mkdirs();
        }
    }
    
}
