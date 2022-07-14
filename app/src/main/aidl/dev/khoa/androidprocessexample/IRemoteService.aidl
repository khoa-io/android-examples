// IRemoteService.aidl
package dev.khoa.androidprocessexample;

import dev.khoa.androidprocessexample.MyParcel;
import dev.khoa.androidprocessexample.ICallback;

interface IRemoteService {
    int getPid();
    void sendBasicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString);
    oneway void sendParcel(in MyParcel parcel, ICallback callback);
}