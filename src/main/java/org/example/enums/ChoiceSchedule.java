package org.example.enums;

public enum ChoiceSchedule{
    CHOICE_STANDARD_SHIFT((byte)1), CHOICE_EARLY_SHIFT((byte)2), CHOICE_LONG_BREAK_SHIFT((byte)3);

    private byte value;
    ChoiceSchedule(byte value){this.value = value;}
    public byte getValue() { return value;}
}
