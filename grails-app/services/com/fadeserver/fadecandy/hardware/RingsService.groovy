package com.fadeserver.fadecandy.hardware




class RingsService {

    def opcService

    def rings() {
        opcService.setStatusLed(false);
    }
}
