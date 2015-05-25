package com.fadeserver.fadecandy.promiseMe

import java.util.concurrent.atomic.AtomicBoolean

class PromiseWrangler {
    String runCode;
    AtomicBoolean active = new AtomicBoolean(false);
    Date created = new Date();



    static constraints = {
    }
}
