package com.mercadopagos.enums;

public enum TipoPuesto {
    FRUTAS_VERDURAS("Frutas y Verduras"),
    CARNE_POLLO("Carnes y Pollo"),
    PESCADOS_MARISCOS("Pescados y Mariscos"),
    ABARROTES("Puesto de Abarrotes"),
    JUGUERIA_REPOSTERIA("Jugeria y Repostería"),
    COMIDA_GENERAL("Puesto de Comida General"),
    ROPA("Puesto de Ropa"),
    OTROS("Otros");

    private final String nombre;

    TipoPuesto(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
