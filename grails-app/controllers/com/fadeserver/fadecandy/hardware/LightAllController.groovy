package com.fadeserver.fadecandy.hardware

import grails.converters.JSON

import java.awt.Color


class LightAllController {

    def lightAllService;
    def opcService;


    def index() {}

    def turnOn(){

        //look at params, if set use them, otherwise use 8x8
        def myWidth = params?.width ? params.width.toInteger() : 8;
        def myHeight = params?.height ? params.height.toInteger() : 8;
        def totalLEDs = myWidth * myHeight;
        def myDefaultColor = params?.hexColor ? Color.decode("#" + params.hexColor) : new Color((int)(Math.random() * 0x1000000));
        def brightLevel = (params?.brightLevel)?.toDouble() ?: 0.35;

        opcService.setPixelCount(totalLEDs);
        opcService.setColorCorrection(2.5, brightLevel, brightLevel, brightLevel); //don't blind a coder.

        String rgb = Integer.toHexString(myDefaultColor.getRGB());
        rgb = rgb.substring(2, rgb.length());

        lightAllService.lightAll(totalLEDs,myDefaultColor)

        def results = [rgb:rgb]
        render results as JSON

    }

    def turnOff(){

        def myWidth = params?.width ?: 8;
        def myHeight = params?.height ?: 8;
        def totalLEDs = myWidth.toInteger() * myHeight.toInteger();
        def myDefaultColor = params?.hexColor ? Color.decode("#" + params.hexColor) : new Color(0,0,0);

        opcService.setColorCorrection(2.5, 0, 0, 0);

        lightAllService.allOff(totalLEDs, myDefaultColor);

        render(contentType: "text/json") {
            turnOff(rgb: "#00000")
        }
    }
}
