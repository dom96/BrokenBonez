package com.dragonfruitstudios.brokenbonez;

import java.util.ArrayList;
import java.util.List;

/**getTouchEvents() and getKeyEvents() methods return TouchEvent and KeyEvent lists which
 * will create many new instances so instead of recreating the instances over and over again
 * this class allows the reuse of previously created ones.**/
public class Pool<T> {
    public interface PoolObjectFactory<T>{
        public T createObject(); // Creates the Pool/PoolObjectFactory object.
    }
    private final List<T> freeObjects;
    private final PoolObjectFactory<T> factory;
    private final int maxSize;

    public Pool(PoolObjectFactory<T>factory, int maxSize){
        this.factory=factory;
        this.maxSize=maxSize;
        this.freeObjects=new ArrayList<T>(maxSize);
    }

    public T newObject(){
        T object = null;

        if(freeObjects.isEmpty()){
            object=factory.createObject();
        }
        else{
            object=freeObjects.remove(freeObjects.size()-1);
        }
        return object;
    }

    public void free(T object){
        if(freeObjects.size()<maxSize){
            freeObjects.add(object);
        }
    }
}