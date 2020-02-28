package org.onlychain.crypto.base;

public interface Memoable
{

    Memoable copy();


    void reset(Memoable other);
}
