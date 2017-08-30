// IMyRemoteService.aidl
package com.sai.test.myremoteservice;

import com.sai.test.myremoteservice.IMyCallback;

interface IMyRemoteService {
    void registerCallback(IMyCallback cb);
    void unregisterCallback(IMyCallback cb);
    int getCount();
}
