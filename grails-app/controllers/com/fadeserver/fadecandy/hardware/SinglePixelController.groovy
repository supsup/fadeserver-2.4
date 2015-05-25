package com.fadeserver.fadecandy.hardware

import grails.converters.JSON

import java.awt.Color

class SinglePixelController {

    def singlePixelService;
    def opcService;


    def index() {}

    def turnOn(){

        //look at params, if set use them, otherwise use 8x8
        def myWidth = params?.width ? params.width.toInteger() : 8;
        def myHeight = params?.height ? params.height.toInteger() : 8;
        def index =  params?.index ? params.index.toInteger() : 0;
        def totalLEDs = myWidth*myHeight;
        def myDefaultColor = params?.hexColor ? Color.decode("#" + params.hexColor) : new Color((int)(Math.random() * 0x1000000));
        def brightLevel = (params?.brightLevel)?.toDouble() ?: 0.35;

        opcService.setPixelCount(totalLEDs);
        opcService.setColorCorrection(2.5, brightLevel, brightLevel, brightLevel); //don't blind a coder.

        String rgb = Integer.toHexString(myDefaultColor.getRGB());
        rgb = rgb.substring(2, rgb.length());

        opcService.setPixel(index, myDefaultColor.hashCode());
        opcService.writePixels();

        def results = [rgb:rgb]
        render results as JSON

    }

    def turnOff(){

        def myWidth = params?.width ?: 8;
        def myHeight = params?.height ?: 8;
        def index =  params?.index ? params.index.toInteger() : 0;
        def totalLEDs = myWidth.toInteger() * myHeight.toInteger();
        def myDefaultColor = params?.hexColor ? Color.decode("#" + params.hexColor) : new Color(0,0,0);

        opcService.setColorCorrection(2.5, 0, 0, 0);

        opcService.setPixel(index, myDefaultColor.hashCode());
        opcService.writePixels();

        render(contentType: "text/json") {
            turnOff(rgb: "#00000")
        }
    }
}
