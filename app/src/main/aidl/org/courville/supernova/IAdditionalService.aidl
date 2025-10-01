// IAdditionalService.aidl
package org.courville.supernova;

// Declare any non-default types here with import statements

interface IAdditionalService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    float[] transformAudio(in float[] audio);
}
