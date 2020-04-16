package com.linciping.androidutil.constant;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Definitions {

    public static final Map<String, String> BASE_TYPE = new HashMap<>();

    static {
        BASE_TYPE.put("String", "String");
        BASE_TYPE.put("int", "Int");
        BASE_TYPE.put("double", "Double");
        BASE_TYPE.put("boolean", "Boolean");
        BASE_TYPE.put("long", "Long");
        BASE_TYPE.put("float", "float");
        BASE_TYPE.put("short", "Short");
        BASE_TYPE.put("byte", "Byte");
        BASE_TYPE.put("char", "Char");
        BASE_TYPE.put("CharSequence", "CharSequence");
        BASE_TYPE.put("ArrayList<String>", "StringArrayList");
        BASE_TYPE.put("ArrayList<Integer>", "IntegerArrayList");
        BASE_TYPE.put("String[]", "StringArray");
        BASE_TYPE.put("int[]", "IntArray");
        BASE_TYPE.put("long[]", "LongArray");
        BASE_TYPE.put("float[]", "FloatArray");
        BASE_TYPE.put("double[]", "DoubleArray");
        BASE_TYPE.put("boolean[]", "BooleanArray");
        BASE_TYPE.put("short[]", "ShortArray");
        BASE_TYPE.put("char[]", "CharArray");
        BASE_TYPE.put("CharSequence[]", "CharSequenceArray");
    }

    @Nullable
    public static String getExtraTypeName(String key) {
        return BASE_TYPE.get(key);
    }
}
