package com.serotonin.m2m2.rt.script;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import com.serotonin.ShouldNeverHappenException;
import com.serotonin.m2m2.Common;
import com.serotonin.m2m2.DataTypes;
import com.serotonin.m2m2.i18n.TranslatableMessage;
import com.serotonin.m2m2.module.ModuleRegistry;
import com.serotonin.m2m2.module.ScriptSourceDefinition;
import com.serotonin.m2m2.rt.dataImage.DataPointRT;
import com.serotonin.m2m2.rt.dataImage.IDataPointValueSource;
import com.serotonin.m2m2.rt.dataImage.types.AlphanumericValue;
import com.serotonin.m2m2.rt.dataImage.types.BinaryValue;
import com.serotonin.m2m2.rt.dataImage.types.DataValue;
import com.serotonin.m2m2.rt.dataImage.types.MultistateValue;
import com.serotonin.m2m2.rt.dataImage.types.NumericValue;

public class ScriptUtils {
    public static final String WRAPPER_CONTEXT_KEY = "CONTEXT";
    public static final String POINTS_CONTEXT_KEY = "POINTS";
    public static final String TIMESTAMP_CONTEXT_KEY = "TIMESTAMP";
    
    public static ScriptEngine newEngine() {
        ScriptEngineManager manager = new ScriptEngineManager();
        return manager.getEngineByName("js");
    }

    public static Object execute(String script) throws ScriptError {
        return execute(newEngine(), script, null);
    }

    public static Object execute(String script, ScriptContext sctx) throws ScriptError {
        return execute(newEngine(), script, sctx);
    }

    public static Object execute(ScriptEngine engine, String script, ScriptContext sctx) throws ScriptError {
        try {
            Object result;
            if (sctx == null)
                result = engine.eval(script);
            else
                result = engine.eval(script, sctx);
            return result;
        }
        catch (ScriptException e) {
            throw ScriptError.create(e);
        }
    }

    public static Object execute(CompiledScript script) throws ScriptError {
        return execute(script, null);
    }

    public static Object execute(CompiledScript script, ScriptContext sctx) throws ScriptError {
        try {
            if (sctx == null)
                return script.eval();
            return script.eval(sctx);
        }
        catch (ScriptException e) {
            throw ScriptError.create(e);
        }
    }

    public static CompiledScript compile(String script) throws ScriptError {
        return compile(newEngine(), script);
    }

    public static CompiledScript compile(ScriptEngine engine, String script) throws ScriptError {
        if (engine instanceof Compilable) {
            try {
                return ((Compilable) engine).compile(script);
            }
            catch (ScriptException e) {
                throw ScriptError.create(e);
            }
        }
        throw new RuntimeException("Engine is not Compilable: " + engine.getClass().getName());
    }

    public static void executeGlobalScripts(ScriptEngine engine) throws ScriptError {
        execute(engine, getGlobalFunctions(), null);
    }

    /**
     * Prepare the Engine by adding all Global Bindings
     * @param engine
     */
    public static void prepareEngine(ScriptEngine engine) {
        Bindings globalBindings = new SimpleBindings();
        // Add constants to the context.
        globalBindings.put("SECOND", Common.TimePeriods.SECONDS);
        globalBindings.put("MINUTE", Common.TimePeriods.MINUTES);
        globalBindings.put("HOUR", Common.TimePeriods.HOURS);
        globalBindings.put("DAY", Common.TimePeriods.DAYS);
        globalBindings.put("WEEK", Common.TimePeriods.WEEKS);
        globalBindings.put("MONTH", Common.TimePeriods.MONTHS);
        globalBindings.put("YEAR", Common.TimePeriods.YEARS);
        globalBindings.put(POINTS_CONTEXT_KEY, new ArrayList<String>());
        
        //Add in Additional Utilities with Global Scope
        globalBindings.put(DateTimeUtility.CONTEXT_KEY, new DateTimeUtility());
        globalBindings.put(UnitUtility.CONTEXT_KEY, new UnitUtility());
        
        //Holder for modifying timestamps of meta points, in Engine Scope so it can be modified by all
        engine.getBindings(ScriptContext.ENGINE_SCOPE).put(TIMESTAMP_CONTEXT_KEY, null);
        
        engine.setBindings(globalBindings, ScriptContext.GLOBAL_SCOPE);
    }
    

    /**
     * Add all Utilities that require permissions 
     * @param permissions
     * @param engine
     * @param engineScope
     */
    public static void prepareUtilities(ScriptPermissions permissions, ScriptEngine engine, Bindings engineScope, 
    		ScriptPointValueSetter setter, List<JsonImportExclusion> importExclusions, boolean testRun){
    	if(testRun) {
    		engineScope.put(RuntimeManagerScriptUtility.CONTEXT_KEY, new RuntimeManagerScriptTestUtility(permissions));
    		engineScope.put(JsonEmportScriptUtility.CONTEXT_KEY, new JsonEmportScriptTestUtility(permissions, importExclusions));
    	} else {
    		engineScope.put(RuntimeManagerScriptUtility.CONTEXT_KEY, new RuntimeManagerScriptUtility(permissions));
    		engineScope.put(JsonEmportScriptUtility.CONTEXT_KEY, new JsonEmportScriptUtility(permissions, importExclusions));
    	}
    	engineScope.put(DataPointQuery.CONTEXT_KEY, new DataPointQuery(permissions, engine, setter));
    	engineScope.put(DataSourceQuery.CONTEXT_KEY, new DataSourceQuery(permissions, engine, setter));
    	engineScope.put(CompiledScriptExecutor.UNCHANGED_KEY, CompiledScriptExecutor.UNCHANGED);
    }

    public static void wrapperContext(ScriptEngine engine, WrapperContext wrapperContext) {
        engine.put(WRAPPER_CONTEXT_KEY, wrapperContext);
    }

    public static AbstractPointWrapper wrapPoint(ScriptEngine engine, IDataPointValueSource point) {
        return wrapPoint(engine, point, null);
    }

    public static AbstractPointWrapper wrapPoint(ScriptEngine engine, IDataPointValueSource point,
            ScriptPointValueSetter setter) {
        int dt = point.getDataTypeId();
        if (dt == DataTypes.BINARY)
            return new BinaryPointWrapper(point, engine, setter);
        if (dt == DataTypes.MULTISTATE)
            return new MultistatePointWrapper(point, engine, setter);
        if (dt == DataTypes.NUMERIC)
            return new NumericPointWrapper(point, engine, setter);
        if (dt == DataTypes.ALPHANUMERIC)
            return new AlphanumericPointWrapper(point, engine, setter);
        throw new ShouldNeverHappenException("Unknown data type id: " + point.getDataTypeId());
    }

    /**
     * Coerce an object into a DataValue
     * @param input
     * @param toDataTypeId
     * @return
     * @throws ResultTypeException
     */
    public static DataValue coerce(Object input, int toDataTypeId) throws ResultTypeException {
        DataValue value;
        
        if(input instanceof DataValue)
        	return (DataValue)input;
        
        if (input == null) {
            if (toDataTypeId == DataTypes.BINARY)
                value = new BinaryValue(false);
            else if (toDataTypeId == DataTypes.MULTISTATE)
                value = new MultistateValue(0);
            else if (toDataTypeId == DataTypes.NUMERIC)
                value = new NumericValue(0);
            else if (toDataTypeId == DataTypes.ALPHANUMERIC)
                value = new AlphanumericValue("");
            else
                value = null;
        }
        else if (input instanceof AbstractPointWrapper) {
            value = ((AbstractPointWrapper) input).getValueImpl();
            if ((value != null)&&(value.getDataType() != toDataTypeId))
                return throwResultTypeException(value, toDataTypeId);
        }
        // See if the type matches.
        else if (toDataTypeId == DataTypes.BINARY && input instanceof Boolean)
            value = new BinaryValue((Boolean) input);
        else if (toDataTypeId == DataTypes.MULTISTATE) {
            if (input instanceof Number)
                value = new MultistateValue(((Number) input).intValue());
            else if (input instanceof String) {
                try {
                    value = new MultistateValue(Integer.parseInt((String) input));
                }
                catch (NumberFormatException e) {
                    return throwResultTypeException(input, toDataTypeId);
                }
            }
            else
                return throwResultTypeException(input, toDataTypeId);
        }
        else if (toDataTypeId == DataTypes.NUMERIC) {
            if (input instanceof Number)
                value = new NumericValue(((Number) input).doubleValue());
            else if (input instanceof NumericValue)
                value = (NumericValue) input;
            else if (input instanceof String) {
                try {
                    value = new NumericValue(Double.parseDouble((String) input));
                }
                catch (NumberFormatException e) {
                    return throwResultTypeException(input, toDataTypeId);
                }
            }
            else
                return throwResultTypeException(input, toDataTypeId);
        }
        else if (toDataTypeId == DataTypes.ALPHANUMERIC)
            value = new AlphanumericValue(input.toString());
        else
            // If not, ditch it.
            return throwResultTypeException(input, toDataTypeId);

        return value;
    }

    private static DataValue throwResultTypeException(Object input, int toDataTypeId) throws ResultTypeException {
        throw new ResultTypeException(new TranslatableMessage("event.script.convertError", input,
                DataTypes.getDataTypeMessage(toDataTypeId)));
    }

    public static void addToContext(ScriptEngine engine, String varName, DataPointRT dprt, ScriptPointValueSetter setCallback) {
        engine.put(varName, wrapPoint(engine, dprt, setCallback));
        List<String> points = getPointList(engine);
        if (points != null)
            points.add(varName);
    }

    public static void removeFromContext(ScriptEngine engine, String varName) {
        List<String> points = getPointList(engine);
        if (points != null)
            points.remove(varName);
        engine.getBindings(ScriptContext.ENGINE_SCOPE).remove(varName);
    }

    @SuppressWarnings("unchecked")
    private static List<String> getPointList(ScriptEngine engine) {
        return (List<String>) engine.get(POINTS_CONTEXT_KEY);
    }

    //
    // Global functions
    private static String FUNCTIONS;

    public static String getGlobalFunctions() {
        if (FUNCTIONS == null) {
            StringWriter sw = new StringWriter();
            List<ScriptSourceDefinition> defs = ModuleRegistry.getDefinitions(ScriptSourceDefinition.class);
            for (ScriptSourceDefinition def : defs) {
                for (String s : def.getScripts())
                    sw.append(s).append("\r\n");
            }
            FUNCTIONS = sw.toString();
        }
        return FUNCTIONS;
    }

    public static void clearGlobalFunctions() {
        FUNCTIONS = null;
    }
}
