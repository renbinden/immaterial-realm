package io.github.alyphen.immaterial_realm.common.hud;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.awt.*;
import java.io.Serializable;
import java.util.Map;

public class HUDComponent implements Serializable {

    private String name;
    private String renderCode;
    private String renderLang;
    private Map<String, Object> variables;

    public HUDComponent(String name, String renderCode, String renderLang, Map<String, Object> variables) {
        this.name = name;
        this.renderCode = renderCode;
        this.renderLang = renderLang;
        this.variables = variables;
    }

    public void paint(ScriptEngineManager scriptEngineManager, Graphics graphics, int screenWidth, int screenHeight) throws ScriptException {
        SimpleBindings bindings = new SimpleBindings();
        bindings.putAll(variables);
        bindings.put("graphics", graphics);
        bindings.put(renderLang.equalsIgnoreCase("js") ? "screenWidth" : "screen_width", screenWidth);
        bindings.put(renderLang.equalsIgnoreCase("js") ? "screenHeight" : "screen_height", screenHeight);
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByExtension(renderLang);
        scriptEngine.eval(renderCode, bindings);
    }

    public String getName() {
        return name;
    }

    public Object getVariable(String variable) {
        return variables.get(variable);
    }

    public void setVariable(String variable, Object value) {
        variables.put(variable, value);
    }

}
