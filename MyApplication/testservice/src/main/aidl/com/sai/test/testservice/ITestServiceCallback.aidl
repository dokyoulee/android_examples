// ITestServiceCallback.aidl
package com.sai.test.testservice;

// Declare any non-default types here with import statements

oneway interface ITestServiceCallback {
    void callback(int count);
}
