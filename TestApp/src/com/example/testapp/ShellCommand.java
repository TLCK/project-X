package com.example.testapp;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.lang.Process;
import java.lang.StringBuilder;

import android.util.Log;

/**
 * @hide
 *
 * Utility class for executing shell command, created due to the
 * various pitfalls of using exec() and waitFor() directly.
 */

public class ShellCommand {
    private String mCommand;
    private int mRetval;
    private Process mProcess;
    private StreamChomper mStdoutChomper;
    private StreamChomper mStderrChomper;

    private class StreamChomper extends Thread {
        private StringBuilder mOutput = new StringBuilder();
        private InputStream mIs;

        public StreamChomper(InputStream is) {
            mIs = is;
        }

        @Override
        public void run() {
            InputStreamReader isr = new InputStreamReader(mIs);
            String line;
            try {
                BufferedReader br = new BufferedReader(isr, 8192);
                try {
                    while ( (line = br.readLine()) != null) {
                        synchronized (mOutput) {
                            mOutput.append(line);
                            mOutput.append('\n');
                        }
                    }
                } finally {
                    br.close();
                }
            } catch (IOException e) {
                synchronized (mOutput) {
                    mOutput.append(e.toString());
                }
            }
        }

        public String getContents() {
            String retval;
            synchronized (mOutput) {
                retval = mOutput.toString();
            }
            return retval;
        }
    }

    /** Upon instantiation, execute a certain shell command.
     *
     * @param command Shell command to execute
     * @param inSeparateShell If true, execute in its own subshell (i.e. sh -c)
     * @throws IOException If a problem occurred when trying to spawn the process
     * @throws InterruptedException If interrupted while waiting for the process
     * to terminate
     */
    public ShellCommand(String command, boolean inSeparateShell)
                throws IOException {
        mCommand = command;

        if (inSeparateShell) {
            String [] shellCmd = new String[] {"/system/bin/input", "-c", command};   
            mProcess = Runtime.getRuntime().exec(shellCmd);
        } else {
        	int ch;
        	String memoryUsage = null;
        	mProcess = Runtime.getRuntime().exec(command);  
        	InputStream in = mProcess.getInputStream();
        	StringBuffer sb = new StringBuffer(512);
            while((ch=in.read())!=-1){
            	sb.append((char) ch);
            }
            memoryUsage = sb.toString();
            Log.i("TAG", " "+memoryUsage);
        }

        /* Spawn worker threads to capture stdout/stderr */
        mStdoutChomper = new StreamChomper(mProcess.getInputStream());
        mStderrChomper = new StreamChomper(mProcess.getErrorStream());
        mStdoutChomper.start();
        mStderrChomper.start();
    }

    /** Wait for the child command to terminate, and read its return value */
    public void complete() throws InterruptedException {
        /* Wait for the command to complete, and then save the
         * contents of stdout/stderr after it's done */
        try {
            mRetval = mProcess.waitFor();
        } finally {
            mProcess.destroy();
        }
    }

    /** @return A string containing the stdout of the command executed */
    public String getStderr() {
        return mStderrChomper.getContents();
    }

    /** @return The integer return value of the command executed */
    public int getRetval() {
        return mRetval;
    }

    /** @return A string containing stdout of the command executed */
    public String getStdout() {
        return mStdoutChomper.getContents();
    }

    /** @return The original command we wanted to execute */
    public String getCommand() {
        return mCommand;
    }
}

