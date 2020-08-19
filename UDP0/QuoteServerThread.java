
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
import java.util.logging.Level;
import java.nio.ByteBuffer;

import java.util.logging.Level; 
import java.util.logging.Logger;

/* globals */
 
public class QuoteServerThread extends Thread {
 
    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    protected boolean moreQuotes = true;
 
    public QuoteServerThread() throws IOException {
        this("QuoteServerThread");
    }
 
    public QuoteServerThread(String name) throws IOException {
        super(name);
        socket = new DatagramSocket(4445);
 
        try {
            in = new BufferedReader(new FileReader("one-liners.txt"));
        } catch (FileNotFoundException e) {
            System.err.println("Could not open quote file. Serving time instead.");
        }
    }

    public Logger logger = Logger.getLogger(QuoteServerThread.class.getName());
 
    public void run() {
        logger.setLevel(Level.INFO);
 
        while (moreQuotes) {
            try {
                byte[] buf = new byte[512];
 
                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);


                /* waits until recv - can wait indefinately */
                socket.receive(packet);
                
                /* get sequence number */
                byte[] bseq = Arrays.copyOfRange(buf, 0, 4);
                int seq = ByteBuffer.wrap(bseq).getInt();
                /* log sequence number */
                logger.log(Level.INFO, String.format("seq: %d\n", seq));

                /* get message */
                byte[] bmsg = Arrays.copyOfRange(buf, 4, 512);
                String msg = new String(bmsg);
                /* log message */
                logger.log(Level.INFO, String.format("msg: %s\n", msg));
 
                /* get parity */
                // byte[] bparity = Arrays.copyOfRange(buf, 508, 512);
                // int parity = ByteBuffer.wrap(bseq).getInt();
                // System.out.printf("parity: %d\n", parity);

                /* figure out response */
                String dString = null;
                if (in == null)
                    dString = new Date().toString();
                else
                    dString = getNextQuote();
 
                buf = dString.getBytes();
 
                /* send the response to the client at "address" and "port" */
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);

                /* waits until sent */
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
                moreQuotes = false;
            }
        }
        socket.close();
    }
 
    protected String getNextQuote() {
        String returnValue = null;
        try {
            if ((returnValue = in.readLine()) == null) {
                in.close();
        moreQuotes = false;
                returnValue = "No more quotes. Goodbye.";
            }
        } catch (IOException e) {
            returnValue = "IOException occurred in server.";
        }
        return returnValue;
    }
}