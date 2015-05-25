package com.fadeserver.fadecandy.hardware

@Transactional
import grails.transaction.Transactional
import java.awt.*;

class OpcService {

    Socket socket;
    OutputStream output;
    String host = "127.0.0.1";;
    int port  = 7890;;

    int[] pixelLocations;
    byte[] packetData;
    byte firmwareConfig;
    String colorCorrection;
    boolean enableShowLocations;


    // Change the number of pixels in our output packet.
    // This is normally not needed; the output packet is automatically sized
    // by draw() and by setPixel().
    def setPixelCount(int numPixels){
        int numBytes = 3 * numPixels;
        int packetLen = 4 + numBytes;
        if (packetData == null || packetData.length != packetLen) {
            // Set up our packet buffer
            packetData = new byte[packetLen];
            packetData[0] = 0;  // Channel
            packetData[1] = 0;  // Command (Set pixel colors)
            packetData[2] = (byte) (numBytes >> 8);
            packetData[3] = (byte) (numBytes & 0xFF);
        }

    }


    // Directly manipulate a pixel in the output buffer. This isn't needed
    // for pixels that are mapped to the screen.
    def setPixel(int number, int colorHash){

        int offset = 4 + number * 3;
        if (packetData == null || packetData.length < offset + 3) {
            println "entering sexPixel packetData == null || packetData.length < offset + 3 case"
            setPixelCount(number + 1);
        }
        //byte[] color=ByteBuffer.allocate(4).putInt(myColor).array();

        byte myRed = (byte) (colorHash >> 16);
        byte myGreen = (byte) (colorHash >> 8);
        byte myBlue =   (byte) colorHash;

        //println "setPixel Red= " + myRed;
        //println "setPixel Green= " + myGreen;
        //println "setPixel Blue= " + myBlue;

        packetData[offset] = myRed;
        packetData[offset + 1] = myGreen;
        packetData[offset + 2] = myBlue;
    }

    // Directly manipulate a range of pixels in the output buffer. This isn't needed
    // for pixels that are mapped to the screen.
    def setPixel(Range<Integer> myRange, int colorHash){

        for (i in myRange) {

            int offset = 4 + i * 3;
            if (packetData == null || packetData.length < offset + 3) {
                println "entering sexPixel packetData == null || packetData.length < offset + 3 case"
                setPixelCount(i + 1);
            }

            //byte[] color=ByteBuffer.allocate(4).putInt(myColor).array();

            byte myRed = (byte) (colorHash >> 16);
            byte myGreen = (byte) (colorHash >> 8);
            byte myBlue =   (byte) colorHash;

            //println "setPixel Red= " + myRed;
            //println "setPixel Green= " + myGreen;
            //println "setPixel Blue= " + myBlue;

            packetData[offset] = myRed;
            packetData[offset + 1] = myGreen;
            packetData[offset + 2] = myBlue;
        }

    }

    // Transmit our current buffer of pixel values to the OPC server. This is handled
    // automatically in draw() if any pixels are mapped to the screen, but if you haven't
    // mapped any pixels to the screen you'll want to call this directly.
    def writePixels(){
        if (packetData == null || packetData.length == 0) {
            // No pixel buffer
            return;
        }
        if (output == null) {
            // Try to (re)connect
            connect();
        }
        if (output == null) {
            return;
        }

        try {
            output.write(packetData);
        } catch (Exception e) {
            dispose();
        }
    }

    def connect(){
        // Try to connect to the OPC server. This normally happens automatically in draw()
        try {
            socket = new Socket(host, port);
            socket.setTcpNoDelay(true);
            output = socket.getOutputStream();
            println("Connected to OPC server");
        } catch (ConnectException e) {
            dispose();
        } catch (IOException e) {
            dispose();
        }

        sendColorCorrectionPacket();
        sendFirmwareConfigPacket();
    }


    def dispose(){
        // Destroy the socket. Called internally when we've disconnected.
        if (output != null) {
            println("Disconnected from OPC server");
        }
        socket = null;
        output = null;
    }


    // Enable or disable dithering. Dithering avoids the "stair-stepping" artifact and increases color
    // resolution by quickly jittering between adjacent 8-bit brightness levels about 400 times a second.
    // Dithering is on by default.
    def setDithering(boolean enabled)
    {
        if (enabled)
            firmwareConfig &= ~0x01;
        else
            firmwareConfig |= 0x01;
        sendFirmwareConfigPacket();
    }

    // Enable or disable frame interpolation. Interpolation automatically blends between consecutive frames
    // in hardware, and it does so with 16-bit per channel resolution. Combined with dithering, this helps make
    // fades very smooth. Interpolation is on by default.
    def setInterpolation(boolean enabled)
    {
        if (enabled)
            firmwareConfig &= ~0x02;
        else
            firmwareConfig |= 0x02;
        sendFirmwareConfigPacket();
    }

    // Put the Fadecandy onboard LED under automatic control. It blinks any time the firmware processes a packet.
    // This is the default configuration for the LED.
    def statusLedAuto()
    {
        firmwareConfig &= 0x0C;
        sendFirmwareConfigPacket();
    }

    // Manually turn the Fadecandy onboard LED on or off. This disables automatic LED control.
    def setStatusLed(boolean on)
    {
        firmwareConfig |= 0x04;   // Manual LED control
        if (on)
            firmwareConfig |= 0x08;
        else
            firmwareConfig &= ~0x08;
        sendFirmwareConfigPacket();
    }

    // Set the color correction parameters
    def setColorCorrection(float gamma, float red, float green, float blue)
    {
        colorCorrection = "{ \"gamma\": " + gamma + ", \"whitepoint\": [" + red + "," + green + "," + blue + "]}";
        sendColorCorrectionPacket();
    }

    // Set custom color correction parameters from a string
    def  setColorCorrection(String s)
    {
        colorCorrection = s;
        sendColorCorrectionPacket();
    }


    // Send a packet with the current firmware configuration settings
    def sendFirmwareConfigPacket(){
        if (output == null) {
            // We'll do this when we reconnect
            return;
        }

        byte[] packet = new byte[9];
        packet[0] = 0;          // Channel (reserved)
        packet[1] = (byte)0xFF; // Command (System Exclusive)
        packet[2] = 0;          // Length high byte
        packet[3] = 5;          // Length low byte
        packet[4] = 0x00;       // System ID high byte
        packet[5] = 0x01;       // System ID low byte
        packet[6] = 0x00;       // Command ID high byte
        packet[7] = 0x02;       // Command ID low byte
        packet[8] = firmwareConfig;

        try {
            output.write(packet);
        } catch (Exception e) {
            dispose();
        }
    }


    // Send a packet with the current color correction settings
    def sendColorCorrectionPacket(){
        if (colorCorrection == null) {
            // No color correction defined
            return;
        }
        if (output == null) {
            // We'll do this when we reconnect
            return;
        }

        byte[] content = colorCorrection.getBytes();
        int packetLen = content.length + 4;
        byte[] header = new byte[8];
        header[0] = 0;          // Channel (reserved)
        header[1] = (byte)0xFF; // Command (System Exclusive)
        header[2] = (byte)(packetLen >> 8);
        header[3] = (byte)(packetLen & 0xFF);
        header[4] = 0x00;       // System ID high byte
        header[5] = 0x01;       // System ID low byte
        header[6] = 0x00;       // Command ID high byte
        header[7] = 0x01;       // Command ID low byte

        try {
            output.write(header);
            output.write(content);
        } catch (Exception e) {
            dispose();
        }
    }

    // Read a pixel from the output buffer. If the pixel was mapped to the display,
    // this returns the value we captured on the previous frame.
    def getPixel(int number)
    {
        int offset = 4 + number * 3;
        if (packetData == null || packetData.length < offset + 3) {
            return 0;
        }
        return (packetData[offset] << 16) | (packetData[offset + 1] << 8) | packetData[offset + 2];
    }



    //////////////////////////////////////////////////////// Grid Logic Below ////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    // Set the location of 64 LEDs arranged in a uniform 8x8 grid.
    // (x,y) is the center of the grid.
    def ledGrid8x8(int index, float x, float y, float spacing, float angle, boolean zigzag){
        ledGrid(index, 8, 8, x, y, spacing, spacing, angle, zigzag);
    }


    // Set the location of several LEDs arranged in a grid. The first strip is
    // at 'angle', measured in radians clockwise from +X.
    // (x,y) is the center of the grid.
    //index is which grid is it.
    def ledGrid(int index, int stripLength, int numStrips, float x, float y, float ledSpacing, float stripSpacing, float angle, boolean zigzag){
        float HALF_PI = Math.PI/2;
        float s = Math.sin(angle + HALF_PI);
        float c = Math.cos(angle + HALF_PI);
        for (int i = 0; i < numStrips; i++) {

            int endingLed = index + stripLength * i;
            // for 8x8 grid on index 0
            //i=0 = 0
            //i=1 = (0+ 8*1) = 8
            //i=2=  (0+ 8*2) = 16
            //etc

            float centerX = x + (i - (numStrips-1)/2.0) * stripSpacing * c;
            float centerY = y + (i - (numStrips-1)/2.0) * stripSpacing * s;
            boolean reversed = zigzag && (i % 2) == 1;

            ledStrip(endingLed, stripLength, centerX, centerY, ledSpacing, angle, reversed);

        }
    }


    // Set the location of several LEDs arranged in a strip.
    // Angle is in radians, measured clockwise from +X.
    // (x,y) is the center of the strip.
    void ledStrip(int index, int count, float x, float y, float spacing, float angle, boolean reversed)
    {
        float s = Math.sin(angle);
        float c = Math.cos(angle);

        for (int i = 0; i < count; i++) {

            int targetLed = reversed ? (index + count - 1 - i) : (index + i);
            int myX = (int)(x + (i - (count-1)/2.0) * spacing * c + 0.5);
            int myY = (int)(y + (i - (count-1)/2.0) * spacing * s + 0.5);

            led(targetLed,myX,myY);

        }
    }


    // Set the location of a single LED
    void led(int index, int x, int y)
    {
        // For convenience, automatically grow the pixelLocations array. We do want this to be an array,
        // instead of a HashMap, to keep draw() as fast as it can be.
        if (pixelLocations == null) {
            pixelLocations = new int[index + 1];
        } else if (index >= pixelLocations.length) {
            pixelLocations = Arrays.copyOf(pixelLocations, index + 1);
        }

        pixelLocations[index] = x + width * y;
    }



}


