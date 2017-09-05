// ITestService.aidl
package com.sai.test.testservice;

// Declare any non-default types here with import statements
import com.sai.test.testservice.ITestServiceCallback;

interface ITestService {
    void registerCallback(ITestServiceCallback cb);
    void unregisterCallback(ITestServiceCallback cb);
    int getCount();
}
