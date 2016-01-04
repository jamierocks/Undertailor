package me.scarlet.undertailor.lua;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;

import java.util.Map;

/**
 * An implementation of a lua script file.
 * 
 * <p>This class is meant to be sent through an implementation of
 * {@link LuaImplementable} before being used, so as to set its creator variable
 * ({@link #getImplementable()}). Attempting to use instances of this class and
 * its subclasses will likely result in errors due to the absence of internal
 * variables set by a LuaImplementable.</p>
 */
public interface LuaImplementation {
    
    /**
     * Returns the {@link LuaImplementable} instance that this
     * {@link LuaImplementation} was processed through.
     * 
     * <p>This method returning null indicates that this LuaImplementation has
     * yet to be sent through a LuaImplementable instance to be given a script
     * implementation. Errors will occur during usage of this LuaImplementation
     * if attempts to use it while this method returns null.</p>
     */
    public LuaImplementable<?, ? extends LuaImplementation> getImplementable();
    
    /**
     * Sets the parent {@link LuaImplementable} for this implementation.
     * 
     * <p>This method solely exists to let a LuaImplementable set itself as the
     * parent implementable upon this implementation. It is not advised to
     * change the parent implementation due to the likely expectations of the
     * implementing class's functions.</p>
     * 
     * @param impl the parent implementable to set
     */
    void setImplementable(LuaImplementable<?, ? extends LuaImplementation> impl);
    
    /**
     * Returns a read-only mapping of functions currently assigned to this
     * {@link LuaImplementation}.
     * 
     * <p>This will only return functions of the names included within the
     * {@link LuaImplementable} ({@link LuaImplementable#getFunctions()}) that
     * loaded this implementation, assuming they existed within the script
     * loaded by the former. It is expected that a default
     * {@link LuaImplementable} will always make sure that functions specified
     * by {@link LuaImplementable#getRequiredFunctions()} are present upon
     * instantiation. It is up to whatever holds the implementation to ensure
     * those functions stay present, should they choose to let them be.</p>
     */
    public Map<String, LuaFunction> getFunctions();
    
    /**
     * Sets the function table for this implementation.
     * 
     * <p>This replaces all functions within this implementation with the given
     * map of functions. While it can be used for other purposes, this method is
     * intended to be used by the
     * {@link LuaImplementable#load(Object, Globals)}
     * method to load a script's functions into an implementation.</p>
     * 
     * @param functions
     */
    void setFunctions(Map<String, LuaFunction> functions);
    
    /**
     * Changes a function within this implementation.
     * 
     * <p>This method simply queries the method
     * {@link LuaImplementable#onFunctionChange(LuaImplementation, String, org.luaj.vm2.LuaValue)}
     * , then changes the method with {@link #setFunction(String, LuaFunction)}
     * (if the queried method returns true). A NullPointerException will be
     * thrown if {@link #getImplementable()} returns null.</p>
     * 
     * <p>It is possible to remove functions denoted required by this
     * implementation's parent {@link LuaImplementable}, though one should not
     * do so unless the system utilizing the implementation expects the
     * possibility of a missing required function.</p>
     * 
     * @param name the name of the function to change
     * @param function the new function, or null to remove
     */
    public default void changeFunction(String name, LuaFunction function) {
        if(this.getImplementable().onFunctionChange(this, name, function)) {
            this.setFunction(name, function);
        }
    }
    
    /**
     * Replaces a function within this implementation.
     * 
     * <p>This method will not trigger the change method. As with the
     * {@link #changeFunction(String, LuaFunction)} method, it is not advised to
     * replace a required function unless the system expects it.</p>
     * 
     * @param name the name of the function to replace
     * @param function the new function, or null to remove
     */
    public void setFunction(String name, LuaFunction function);
}
