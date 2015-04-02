/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ms_square.android.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * Utility class for managing input streams.
 */
public class StreamUtil {

    /**
     * Retrieves a {@link String} from a character stream.
     *
     * @param stream the {@link java.io.InputStream}
     * @return the {@link String} containing the stream contents
     * @throws java.io.IOException if failure occurred reading the stream
     */
    public static String getStringFromStream(InputStream stream) throws IOException {
        Reader ir = new BufferedReader(new InputStreamReader(stream));
        int irChar = -1;
        StringBuilder builder = new StringBuilder();
        while ((irChar = ir.read()) != -1) {
            builder.append((char)irChar);
        }
        return builder.toString();
    }

    /**
     * Copies contents of origStream to destStream.
     * <p/>
     * Recommended to provide a buffered stream for input and output
     *
     * @param inStream the {@link java.io.InputStream}
     * @param outStream the {@link java.io.OutputStream}
     * @throws java.io.IOException
     */
    public static void copyStreams(InputStream inStream, OutputStream outStream)
            throws IOException {
        int data = -1;
        while ((data = inStream.read()) != -1) {
            outStream.write(data);
        }
    }

    /**
     * Copies contents of inStream to writer.
     * <p/>
     * Recommended to provide a buffered stream for input and output
     *
     * @param inStream the {@link java.io.InputStream}
     * @param writer the {@link java.io.Writer} destination
     * @throws java.io.IOException
     */
    public static void copyStreamToWriter(InputStream inStream, Writer writer) throws IOException {
        int data = -1;
        while ((data = inStream.read()) != -1) {
            writer.write(data);
        }
    }

    /**
     * Closes given input stream.
     *
     * @param inStream the {@link java.io.InputStream}. No action taken if inStream is null.
     */
    public static void closeStream(InputStream inStream) {
        if (inStream != null) {
            try {
                inStream.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    /**
     * Closes given output stream.
     *
     * @param outStream the {@link java.io.OutputStream}. No action taken if outStream is null.
     */
    public static void closeStream(OutputStream outStream) {
        if (outStream != null) {
            try {
                outStream.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    /**
     * Attempts to flush the given output stream, and then closes it.
     *
     * @param outStream the {@link java.io.OutputStream}. No action taken if outStream is null.
     */
    public static void flushAndCloseStream(OutputStream outStream) {
        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                // ignore
            }
            try {
                outStream.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }
}