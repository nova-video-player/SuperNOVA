package org.courville.supernova.ai;

public class SpeechSeparator {
    private long separator;

    public SpeechSeparator(String modelPath) {
        separator = create(modelPath);
    }

    public void setControl(float control) {
        setControl(separator, control);
    }

    public void run(int sampleCount, float[] input1, float[] input2, float[] output1, float[] output2) {
        run(separator, sampleCount, input1, input2, output1, output2);
    }

    public void destroy() {
        destroy(separator);
        separator = 0;
    }

    private native long create(String modelPath);
    private native void setControl(long separator, float control);
    private native void run(long separator, int sampleCount, float[] input1, float[] input2, float[] output1, float[] output2);
    private native void destroy(long separator);

    static {
        System.loadLibrary("supernova-native");
    }
}
