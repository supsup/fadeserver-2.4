package com.fadeserver.fadecandy.hardware

class Opc {

    Date dateCreated;
    int width;
    int height;
    int[] hashArray;
    String hexValue;


    static constraints = {

    }

    static mapping = {
        hashArray column: 'hashArray', sqlType: 'VARBINARY(10000)'
        // hexArray  column: 'hexArray', sqlType: 'VARBINARY(10000)'
    }
}
