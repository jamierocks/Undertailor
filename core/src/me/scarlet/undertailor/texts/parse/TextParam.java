/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Tellerva, Marc Lawrence
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.scarlet.undertailor.texts.parse;

public enum TextParam {

    FONT("FONT", ""), //TODO: Have Xemiru fill in default values.
    STYLE("STYLE", ""), //TODO: Have Xemiru fill in default values.
    COLOR("COLOR", ""), //TODO: Have Xemiru fill in default values.
    SOUND("SOUND", ""), //TODO: Have Xemiru fill in default values.
    SPEED("SPEED", ""), //TODO: Have Xemiru fill in default values.
    DELAY("DELAY", ""), //TODO: Have Xemiru fill in default values.
    BOLD("BOLD", ""), //TODO: Have Xemiru fill in default values.
    ITALIC("ITALIC", ""), //TODO: Have Xemiru fill in default values.
    UNDERLINE("UNDERLINE", ""), //TODO: Have Xemiru fill in default values.
    UNDEFINED("UNDEFINED", ""), //TODO: Have Xemiru fill in default values.
    ;

    private final String name;
    private final String defaultValue;

    TextParam(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return this.name;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public String toString() {
        return String.format("[%s:%s]", this.getName(), this.getDefaultValue());
    }

    public static TextParam of(String string) {
        for (TextParam param : values()) {
            if (param.getName().equalsIgnoreCase(string)) {
                return param;
            }
        }

        return UNDEFINED;
    }
}