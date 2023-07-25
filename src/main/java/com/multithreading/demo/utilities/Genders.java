package com.multithreading.demo.utilities;

import java.util.HashMap;
import java.util.Map;

public enum Genders {
    MALE("Male"),
    FEMALE("Female"),
    NON_BINARY("Non-binary"),
    POLYGENDER("Polygender"),
    AGENDER("Agender"),
    GENDERFLUID("Genderfluid"),
    BIGENDER("Bigender"),
    GENDERQUEER("Genderqueer");

    private final String genderType;
    private static final Map<String, Genders> BY_GenderType = new HashMap<>();
    Genders(String gender) {
        this.genderType=gender;
    }
    static {
        for(Genders g:values()) {
            BY_GenderType.put(g.genderType,g);
        }
    }
    public static Genders valueOfGenderType(String gender){
        Genders g=BY_GenderType.get(gender);
        if(g!=null)
            return g;
        else
            throw new IllegalArgumentException("Unexpected Value + "+gender);
    }
}
