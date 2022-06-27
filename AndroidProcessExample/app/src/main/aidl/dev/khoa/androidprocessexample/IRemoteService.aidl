// IRemoteService.aidl
package dev.khoa.androidprocessexample;

import dev.khoa.androidprocessexample.MyParcel;

interface IRemoteService {
    int getPid();
    void sendBasicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString);
    void sendParcel(in MyParcel parcel);
}