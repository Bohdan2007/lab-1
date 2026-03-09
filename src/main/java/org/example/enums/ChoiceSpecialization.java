package org.example.enums;

public enum ChoiceSpecialization{
    CHOICE_TRAUMATOLOGIST((byte)1), CHOICE_SURGEON((byte)2), CHOICE_OCULIST((byte)3);

    private byte value;
    ChoiceSpecialization(byte value){this.value = value;}
    public byte getValue() { return value;}
}
