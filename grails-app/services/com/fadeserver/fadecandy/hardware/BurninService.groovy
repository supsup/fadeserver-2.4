package com.fadeserver.fadecandy.hardware
import grails.transaction.Transactional

import java.awt.Color
import java.util.concurrent.atomic.AtomicBoolean

import static grails.async.Promises.task

@Transactional
class BurninService {

    def p1;
    def opcService
    static AtomicBoolean PromiseStoppedBurnin = new AtomicBoolean(false);

    def burnIn(totalLEDs) {

            totalLEDs = totalLEDs ?: 64;
            def t = 0;
            p1 = task {

                PromiseStoppedBurnin.set(false);

                opcService.setPixelCount(totalLEDs);

                while(!PromiseStoppedBurnin.get()) {

                    println "-------------"
                    t += 0.4;
                    def brightness = (int)(Math.min(1, 1.25 + Math.sin(t)) * 95);
                    def myHash = new Color(brightness,brightness,brightness).hashCode();

                    opcService.setPixel(0..totalLEDs-1, myHash);
                    opcService.writePixels();

                    sleep(400);

                }

            }

    }

    def allOff(totalLEDs, myDefaultColor){

        PromiseStoppedBurnin.set(true);

        sleep(1500);

        def myHash = myDefaultColor.hashCode();
        opcService.setPixel(0..totalLEDs-1, myHash);
        opcService.writePixels();



    }
}
