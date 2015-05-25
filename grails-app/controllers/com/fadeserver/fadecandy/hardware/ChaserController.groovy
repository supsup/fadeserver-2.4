package com.fadeserver.fadecandy.hardware

import grails.converters.JSON

import java.awt.Color

class ChaserController {
    def chaserService;
    def opcService;

    def index() {
        //render simple html page to control turnOn and turnOff via ajax.

    }

    def turnOn(){

        //look at params, if set use them, otherwise use 8x8
        def myWidth = params?.width ?: 8;
        def myHeight = params?.height ?: 8;

        def brightLevel = (params?.brightLevel)?.toDouble() ?: 0.55;

        def totalLEDs = myWidth.toInteger() * myHeight.toInteger();
        def myDefaultColor = params?.hexColor ? Color.decode("#" + params.hexColor) : null;

        opcService.setPixelCount(totalLEDs);
        opcService.setColorCorrection(2.5, brightLevel, brightLevel, brightLevel);

        chaserService.chase(totalLEDs,myDefaultColor);

        def results = [rgb:myDefaultColor]
        render results as JSON


    }

    def turnOff(){


        def myWidth = params?.width ?: 8;
        def myHeight = params?.height ?: 8;
        def totalLEDs = myWidth.toInteger() * myHeight.toInteger();
        def myDefaultColor = params?.hexColor ? Color.decode("#" + params.hexColor) : new Color(0,0,0);

        opcService.setColorCorrection(2.5, 0, 0, 0);

        chaserService.allOff(totalLEDs, myDefaultColor);

        String rgb = Integer.toHexString(myDefaultColor.getRGB());
        rgb = rgb.substring(2, rgb.length());

        def results = [rgb:rgb]
        render results as JSON

    }

}
