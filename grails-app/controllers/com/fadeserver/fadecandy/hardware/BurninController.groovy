package com.fadeserver.fadecandy.hardware

import grails.converters.JSON

import java.awt.Color

class BurninController {


    def burninService;


    def index() {}

    def turnOn(){

        //look at params, if set use them, otherwise use 8x8
        def myWidth = params?.width ?: 8;
        def myHeight = params?.height ?: 8;
        int totalLEDs = myWidth.toInteger() * myHeight.toInteger();

        burninService.burnIn(totalLEDs);

        def results = [rgb:"#FFF"]
        render results as JSON
    }

    def turnOff(){

        def myWidth = params?.width ?: 8;
        def myHeight = params?.height ?: 8;
        int totalLEDs = myWidth.toInteger() * myHeight.toInteger();
        def myDefaultColor = params?.hexColor ? Color.decode("#" + params.hexColor) : new Color(0,0,0);

        burninService.allOff(totalLEDs, myDefaultColor)

        String rgb = Integer.toHexString(myDefaultColor.getRGB());
        rgb = rgb.substring(2, rgb.length());

        def results = [rgb:rgb]
        render results as JSON
    }
}
