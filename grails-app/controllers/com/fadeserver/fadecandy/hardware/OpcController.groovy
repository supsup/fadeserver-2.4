package com.fadeserver.fadecandy.hardware
import java.awt.Color;


import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional


class OpcController {


    def lightAllService;
    def chaserService;

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {

        //look at params, if set use them, otherwise use 8x8
        def myWidth = params?.width ? params.width.toInteger() : 8;
        def myHeight = params?.height ? params.height.toInteger() : 8;
        def totalLEDs = myWidth * myHeight;
        def myDefaultColor = new Color((int)(Math.random() * 0x1000000));
        def toggleOff = params?.toggleOff ? params.toggleOff : false;

        if(params?.hexColor){
            myDefaultColor =  Color.decode("#" + params.hexColor);
        }

        String rgb = Integer.toHexString(myDefaultColor.getRGB());
        rgb = rgb.substring(2, rgb.length());

        lightAllService.lightAll(myWidth,myHeight,totalLEDs,myDefaultColor,toggleOff)

        params.rgb = rgb;
        params.max = Math.min(max ?: 10, 100)
        respond Opc.list(params), model:[opcInstanceCount: Opc.count()]
    }

    def show(Opc opcInstance) {
        respond opcInstance
    }

    def create() {
        respond new Opc(params)
    }


    def save(Opc opcInstance) {
        if (opcInstance == null) {
            notFound()
            return
        }

        if (opcInstance.hasErrors()) {
            respond opcInstance.errors, view:'create'
            return
        }

        opcInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'opc.label', default: 'Opc'), opcInstance.id])
                redirect opcInstance
            }
            '*' { respond opcInstance, [status: CREATED] }
        }
    }

    def edit(Opc opcInstance) {
        respond opcInstance
    }


    def update(Opc opcInstance) {
        if (opcInstance == null) {
            notFound()
            return
        }

        if (opcInstance.hasErrors()) {
            respond opcInstance.errors, view:'edit'
            return
        }

        opcInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Opc.label', default: 'Opc'), opcInstance.id])
                redirect opcInstance
            }
            '*'{ respond opcInstance, [status: OK] }
        }
    }

    def endTask(){
        println "yo"
      //  lightAllService.endTask()
        lightAllService.allOff()
    }



    def chaser(){
        chaserService.chase(512)
    }


    def delete(Opc opcInstance) {

        if (opcInstance == null) {
            notFound()
            return
        }

        opcInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Opc.label', default: 'Opc'), opcInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'opc.label', default: 'Opc'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
