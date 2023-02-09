package com.chess.engine.driver;

public class javaChess {
    public static void main(String[] _args) {
        new table();
        System.out.println("Init window, main of ( ");

        for (final String arg: _args) {
            System.out.print("\""+arg+"\" ");
        }

        System.out.println(")");
    }
}
