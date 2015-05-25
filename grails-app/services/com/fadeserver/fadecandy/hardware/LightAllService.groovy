package com.fadeserver.fadecandy.hardware

import java.util.concurrent.atomic.AtomicBoolean

import static grails.async.Promises.*
import java.awt.Color;

class LightAllService {

    def p1;
    def opcService

    //Default State of all promises is OFF.
    static AtomicBoolean PromiseStoppedLightAll = new AtomicBoolean(false);


    def lightAll(totalLEDs, color) {

           PromiseStoppedLightAll.set(false);

           p1 = task {
               def myHash = color.hashCode();
               opcService.setPixel(0..totalLEDs-1, myHash);
               opcService.writePixels();
           }

    }


    def allOff(totalLEDs, color){

        PromiseStoppedLightAll.set(true)
        sleep(500);

        def myHash = color.hashCode();
        opcService.setPixel(0..totalLEDs-1, myHash);
        opcService.writePixels();



    }
}
