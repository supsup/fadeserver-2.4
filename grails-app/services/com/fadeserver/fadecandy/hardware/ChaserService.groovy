package com.fadeserver.fadecandy.hardware

import java.awt.Color
import java.util.concurrent.atomic.AtomicBoolean

import static grails.async.Promises.task

class ChaserService {

    def p1;
    def opcService
    static AtomicBoolean PromiseStoppedChaser = new AtomicBoolean(false);

    def chase(totalLEDs,myColor) {

        PromiseStoppedChaser.set(false);

        p1 = task {

            while(!PromiseStoppedChaser.get()) { //outer exit condition, used to destroy Promise from the outside.

                def myDefaultColor;
                def myHash;
                def blackOut = new Color(0,0,0).hashCode();

                for (int i = 0; i < totalLEDs; i++) {
                    if(!PromiseStoppedChaser.get()) { //inner exit condition to stop pushing after Turn off is triggered.

                        myDefaultColor = myColor ?: new Color((int) (Math.random() * 0x1000000));
                        myHash = myDefaultColor.hashCode();

                        opcService.setPixel(0..totalLEDs - 1, blackOut);
                        opcService.setPixel(i, myHash);

                        opcService.writePixels();
                        sleep(100);
                    }
                }

            }

        }

        //don't know why this does not always trigger.
        p1.onComplete { println "Child Process Terminated."}



    }


    def allOff(totalLEDs, myDefaultColor){

        PromiseStoppedChaser.set(true);

        sleep(2500);

        def myHash = myDefaultColor.hashCode();
        opcService.setPixel(0..totalLEDs-1, myHash);
        opcService.writePixels();



    }


}
