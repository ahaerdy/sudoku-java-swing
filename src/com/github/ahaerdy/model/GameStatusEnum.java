package com.github.ahaerdy.model;

public enum GameStatusEnum {

    NON_STARTED("não iniciado"),
    INCOMPLETE("incompleto"),
    COMPLETE("completo");

    private final String label;

    GameStatusEnum(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}