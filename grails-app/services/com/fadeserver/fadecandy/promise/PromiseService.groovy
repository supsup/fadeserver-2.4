package com.fadeserver.fadecandy.promise

import com.fadeserver.fadecandy.promiseMe.PromiseWrangler
import grails.transaction.Transactional

@Transactional
class PromiseService {

    def persistPromiseMethod(code) {
        def pattern = new PromiseWrangler(code);
        pattern.save();
    }

    def destroyPromiseMethod(id) {
        //def pattern = new PromiseWrangler();
        //   pattern.save();
    }
}
