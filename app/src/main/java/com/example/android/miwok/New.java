package com.example.android.miwok;

public class New {

        // State
        private final String mDefaultTranslation;
        private final String mMiwokTranslationl;
        private final int mAudioResourceId;

        private int mImageResourceId = NO_IMAGE_PROVIDED ;
        // State with initial value to decide if a immage has or not a immage
        public static final int NO_IMAGE_PROVIDED = -1 ;

        // Constructor
        public New(String defaultTranslation , String miwokTranslation, int audioResourceId){
            mDefaultTranslation = defaultTranslation;
            mMiwokTranslationl = miwokTranslation;
            mAudioResourceId = audioResourceId;
        }

        // Constructor nr.2 So we can populate the phrases diferently
        public New(String defaultTranslation , String miwokTranslation, int imageResourceId, int audioResourceId){
            mDefaultTranslation = defaultTranslation;
            mMiwokTranslationl = miwokTranslation;
            mImageResourceId = imageResourceId;
            mAudioResourceId = audioResourceId;
        }

        // Methods
        public String getDefaultTranslation(){
            return mDefaultTranslation;
        }

        public String getMiwokTranslationl(){
            return mMiwokTranslationl;
        }
        public int getmImageResourceId(){
            return  mImageResourceId;
        }

        // Boolean that return frue or false ( on hasImage on the costructor )
        public boolean hasImage(){
            return mImageResourceId != NO_IMAGE_PROVIDED;
        }
        public int getmAudioResourceId(){
            return mAudioResourceId;
        }
    }


