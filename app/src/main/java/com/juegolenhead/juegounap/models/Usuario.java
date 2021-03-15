package com.juegolenhead.juegounap.models;

public class Usuario {

    private String nick;
    private int pokemones;

    public Usuario() {

    }

    public Usuario( String nick, int pokemones ) {
        this.nick = nick;
        this.pokemones = pokemones;

    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getPokemones() {
        return pokemones;
    }

    public void setPokemones(int pokemones) {
        this.pokemones = pokemones;
    }
}
