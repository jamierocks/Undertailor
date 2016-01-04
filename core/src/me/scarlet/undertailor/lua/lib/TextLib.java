package me.scarlet.undertailor.lua.lib;

import com.badlogic.gdx.graphics.Color;
import me.scarlet.undertailor.Undertailor;
import me.scarlet.undertailor.audio.SoundWrapper;
import me.scarlet.undertailor.lua.Lua;
import me.scarlet.undertailor.lua.LuaLibrary;
import me.scarlet.undertailor.lua.LuaLibraryComponent;
import me.scarlet.undertailor.lua.LuaObjectValue;
import me.scarlet.undertailor.lua.lib.text.TextComponentLib;
import me.scarlet.undertailor.texts.Font;
import me.scarlet.undertailor.texts.Style;
import me.scarlet.undertailor.texts.TextComponent;
import me.scarlet.undertailor.texts.TextComponent.DisplayMeta;
import me.scarlet.undertailor.texts.TextComponent.Text;
import me.scarlet.undertailor.util.LuaUtil;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

public class TextLib extends LuaLibrary {
    
    public static LuaObjectValue<Text> check(LuaValue value) {
        return LuaUtil.checkType(value, Lua.TYPENAME_TEXT);
    }
    
    public static LuaObjectValue<Text> create(Text text) {
        return LuaObjectValue.of(text, Lua.TYPENAME_TEXT, LuaLibrary.asMetatable(Lua.LIB_TEXT));
    }
    
    public static final LuaLibraryComponent[] COMPONENTS = {
            new newDisplayMeta(),
            new newText(),
            new addComponent(),
            new addComponents(),
            new getComponentAtCharacter(),
            new getMembers(),
            new drawText(),
            new TextComponentLib()
    };
    
    public TextLib() {
        super("text", COMPONENTS);
    }
    
    static class newDisplayMeta extends LibraryFunction {
        @Override
        public Varargs execute(Varargs args) {
            LuaUtil.checkArguments(args, 0, 5);
            
            float offX = new Float(args.optdouble(1, 0F));
            float offY = new Float(args.optdouble(2, 0F));
            float scaleX = new Float(args.optdouble(1, 1F));
            float scaleY = new Float(args.optdouble(1, 1F));
            Color color = args.arg(5).isnil() ? Color.WHITE : ColorsLib.check(args.arg(5)).getObject();
            
            if(scaleX < 0F) {
                scaleX = 0F;
            }
            
            if(scaleY < 0F) {
                scaleY = 0F;
            }
            
            return LuaObjectValue.of(new DisplayMeta(offX, offY, scaleX, scaleY, color), Lua.TYPENAME_DISPLAYMETA);
        }
    }
    
    static class newText extends LibraryFunction {
        @Override
        public Varargs execute(Varargs args) {
            LuaUtil.checkArguments(args, 1, 7);
            
            Font font = Undertailor.getFontManager().getRoomObject(args.checkjstring(1));
            Style style = args.isnil(2) ? null : Undertailor.getStyleManager().getRoomObject(args.arg(2).checkjstring());
            Color color = args.isnil(3) ? null : ColorsLib.check(args.arg(3)).getObject();
            SoundWrapper sound = args.arg(4).isnil() ? null : Undertailor.getAudioManager().getSoundManager().getResource(args.arg(4).checkstring().tojstring());
            int speed = args.optint(5, TextComponent.DEFAULT_SPEED);
            int segsize = args.optint(6, 1);
            float delay = new Float(args.optdouble(7, 0F));
            
            return TextLib.create(new Text(font, style, color, sound, speed, segsize, delay));
        }
    }
    
    static class addComponent extends LibraryFunction {
        @Override
        public Varargs execute(Varargs args) {
            LuaUtil.checkArguments(args, 2, 2);
            Text text = TextLib.check(args.arg(1)).getObject();
            TextComponent added = TextComponentLib.check(args.arg(2)).getObject();
            
            text.addComponents(added);
            return args.arg(1);
        }
    }
    
    static class addComponents extends LibraryFunction {
        @Override
        public Varargs execute(Varargs args) {
            LuaUtil.checkArguments(args, 2, -1);
            
            Text text = TextLib.check(args.arg(1)).getObject();
            TextComponent[] components = new TextComponent[args.narg() - 1];
            for(int i = 2; i <= args.narg(); i++) {
                components[i - 2] = TextComponentLib.check(args.arg(i)).getObject();
            }
            
            text.addComponents(components);
            return args.arg(1);
        }
    }
    
    static class getComponentAtCharacter extends LibraryFunction {
        @Override
        public Varargs execute(Varargs args) {
            LuaUtil.checkArguments(args, 2, 2);
            
            Text text = TextLib.check(args.arg(1)).getObject();
            int index = args.checkint(2);
            try {
                TextComponent component = text.getComponentAtCharacter(index);
                if(component == null) {
                    return LuaValue.NIL;
                }
                
                return TextComponentLib.create(component);
            } catch(Exception e) {
                throw new LuaError("failed to execute internal method \"getComponentAtCharacter\": " + e.getMessage());
            }
        }
    }
    
    static class getMembers extends LibraryFunction {
        @Override
        public Varargs execute(Varargs args) {
            LuaUtil.checkArguments(args, 1, 1);
            LuaTable returned = new LuaTable();
            Text text = TextLib.check(args.arg(1)).getObject();
            for(int i = 0; i < text.getMembers().size(); i++) {
                returned.set(i, TextComponentLib.create(text.getMembers().get(i)));
            }
            
            return returned;
        }
    }
    
    static class drawText extends LibraryFunction {
        @Override
        public Varargs execute(Varargs args) {
            LuaUtil.checkArguments(args, 3, 6);
            
            Text text = TextLib.check(args.arg(1)).getObject();
            float posX = new Float(args.arg(2).checkdouble());
            float posY = new Float(args.arg(3).checkdouble());
            float scaleX = new Float(args.optdouble(4, 1.0));
            float scaleY = new Float(args.optdouble(5, scaleX));
            float alpha = new Float(args.optdouble(6, 1.0));
            Undertailor.getFontManager().write(text, posX, posY, scaleX, scaleY, alpha);
            return LuaValue.NIL;
        }
    }
}
