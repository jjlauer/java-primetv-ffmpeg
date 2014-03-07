package com.mfizz.jne.ffmpeg;

/*
 * #%L
 * mfz-ffmpeg
 * %%
 * Copyright (C) 2012 - 2014 mfizz
 * %%
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
 * #L%
 */

import com.mfizz.jne.JNE;
import com.mfizz.jne.StreamGobbler;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author joelauer
 */
public class ScreenshotDemo {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotDemo.class);
    
    static public void main(String[] args) throws Exception {
        String exeName = "ffmpeg";
        File ffmpegExeFile = JNE.find(exeName, JNE.FindType.EXECUTABLE);
        
        if (ffmpegExeFile == null) {
            logger.error("Unable to find executable [" + exeName + "]");
            System.exit(1);
        }
        
        logger.info("java version: " + System.getProperty("java.version"));
        logger.info("java home: " + System.getProperty("java.home"));
        logger.info("using exe: " + ffmpegExeFile.getAbsolutePath());
        
        ProcessBuilder pb = new ProcessBuilder(
            ffmpegExeFile.getAbsolutePath(),
            "-version"
        );
        pb.redirectErrorStream(true);
        Process p = pb.start();
        StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream()) {
            @Override
            public void onLine(String line) {
                logger.debug(line);
            }
            @Override
            public void onException(Exception e) {
                logger.error("Unable to cleanly gobble process output", e);
            }
        };
        outputGobbler.start();
        
        int retVal = p.waitFor();
        logger.info("ret val: " + retVal);
    }
    
}
