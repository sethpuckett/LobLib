/*
 * Copyright (C) 2010 The Android Open Source Project
 *
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
 */

package com.game.loblib.utility.android;

import com.game.loblib.exception.PoolExhaustedException;
import com.game.loblib.utility.Logger;

/**
 * A general-purpose pool of objects.  Objects in the pool are allocated up front and then 
 * passed out to requesting objects until the pool is exhausted (at which point an error is thrown).
 * Code that requests objects from the pool should return them to the pool when they are finished.
 * This class is abstract; derivations need to implement the fill() function to fill the pool, and
 * may wish to override release() to clear state on objects as they are returned to the pool.
 */
public abstract class ObjectPool {
	protected static final StringBuffer _tag = new StringBuffer("ObjectPool");
    private FixedSizeArray<Object> mAvailable;
    private int mSize;

    private static final int DEFAULT_SIZE = 32;
    
    public ObjectPool() {
        setSize(DEFAULT_SIZE);
    }

    public ObjectPool(int size) {
        setSize(size);
    }
    
    /** Allocates an object from the pool */
    protected Object allocate() {
        Object result = mAvailable.removeLast();
        if (result == null) {
        	PoolExhaustedException e = new PoolExhaustedException();
        	Logger.e(_tag, "Pool exhausted", e);
        	//throw e;
        }
        assert result != null : "Object pool of type " + this.getClass().getSimpleName()
                                + " exhausted!!";
        return result;
    }

    /** Returns an object to the pool. */
    public void release(Object entry) {
        mAvailable.add(entry);
    }

    /** Returns the number of pooled elements that have been allocated but not released. */
    public int getAllocatedCount() {
        return mAvailable.getCapacity() - mAvailable.getCount();
    }
    
    private void setSize(int size) {
        mSize = size;
        mAvailable = new FixedSizeArray<Object>(mSize);
    }

    protected abstract void fill();

    protected FixedSizeArray<Object> getAvailable() {
        return mAvailable;
    }

    protected int getSize() {
        return mSize;
    }

   
}
