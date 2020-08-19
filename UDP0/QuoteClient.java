/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.ByteBuffer;

import java.util.logging.Level; 
import java.util.logging.Logger;

public class QuoteClient {

    static int countSetBits(byte[] arr) {
        // System.out.println(Integer.toBinaryString(a));
        int count = 0;
        for (int i = 0; i < arr.length; i+=4) {
            int x = ByteBuffer.wrap(Arrays.copyOfRange(arr, i, i+4)).getInt();
            // System.out.println(Integer.toBinaryString(x));
            count += Integer.bitCount(x);
        }
        return count;
    }

    public static void setSeq(byte[] arr, int seq) {
        byte[] b = ByteBuffer.allocate(4).putInt(seq).array();
        arr[0] = b[0];
        arr[1] = b[1];
        arr[2] = b[2];
        arr[3] = b[3];
    }

    /* globals */
    public static Logger logger = Logger.getLogger(QuoteClient.class.getName());

    public static void main(String[] args) throws IOException {
        logger.setLevel(Level.INFO);
 
        if (args.length != 1) {
             System.out.println("Usage: java QuoteClient <hostname>");
             return;
        }
 
        /* get a datagram socket */
        DatagramSocket socket = new DatagramSocket();
 
        /* new rame */
        byte[] buf = new byte[512];
        byte[] body;

        /* clear frame */
        for (int i = 0; i < buf.length; i++) {
            buf[i] = 0;
        }

        /* frame sequence */
        setSeq(buf, 17);

        /* log sequence number */
        int x = ByteBuffer.wrap(buf).getInt();
        logger.log(Level.INFO, String.format("seq input: %d\n", x)); 
        
        /* set body */
        body = ("Hello friend!").getBytes();
        int j = 4;
        for (byte b:body) {
            buf[j] = b;
            j++;
        }
        
        /* setting parity */
        // if (false) {
        //     int parity = countSetBits(buf);
        //     if (parity%2 == 1) {
        //         buf[511] = 1;
        //     }
        //     logger.log(Level.INFO, String.format("parity: %d\n", parity));
        // }

        /* creating the packet */
        InetAddress address = InetAddress.getByName(args[0]);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);

        /* sending the packet */
        socket.send(packet);
     
        /* get response */
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
 
        /* display response */
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Quote of the Moment: " + received);
     
        socket.close();
    }
}
