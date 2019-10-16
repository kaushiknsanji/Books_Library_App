/*
 * Copyright 2017 Kaushik N. Sanji
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.kaushiknsanji.bookslibrary.customviews;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Custom {@link AppCompatImageView} class that extends {@link #setFrame(int, int, int, int)}
 * to position the image to bottom-center within its frame while maintaining the aspect ratio
 *
 * @author Kaushik N Sanji
 */
public class MatrixBottomCenteredImageView extends AppCompatImageView {

    public MatrixBottomCenteredImageView(Context context) {
        //Delegating to other constructor
        this(context, null);
    }

    public MatrixBottomCenteredImageView(Context context, AttributeSet attrs) {
        //Delegating to other constructor
        this(context, attrs, 0);
    }

    public MatrixBottomCenteredImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        //Propagating the call to super
        super(context, attrs, defStyleAttr);
    }

    /**
     * Method extended to position the image/drawable to bottom-center within its frame
     * while maintaining the aspect ratio
     *
     * @param frameLeft   is the Integer value of the position of the Left edge of the image frame
     * @param frameTop    is the Integer value of the position of the Top edge of the image frame
     * @param frameRight  is the Integer value of the position of the Right edge of the image frame
     * @param frameBottom is the Integer value of the position of the Bottom edge of the image frame
     * @return Boolean value. True if the new size and position are different than the previous ones
     */
    @Override
    protected boolean setFrame(int frameLeft, int frameTop, int frameRight, int frameBottom) {
        //Retrieving the current Drawable
        Drawable imageDrawable = getDrawable();
        if (imageDrawable != null) {
            //If Drawable is present

            //Calculating the dimensions of the Frame
            float frameWidth = frameRight - frameLeft;
            float frameHeight = frameBottom - frameTop;

            //Retrieving the dimensions of the Drawable
            float imageDrawableWidth = (float) imageDrawable.getIntrinsicWidth();
            float imageDrawableHeight = (float) imageDrawable.getIntrinsicHeight();

            //Retrieving the current Image Matrix
            Matrix imageMatrix = getImageMatrix();

            //Applying the Matrix with Centered Image Fit
            RectF drawableRect = new RectF(0, 0, imageDrawableWidth, imageDrawableHeight);
            RectF frameRect = new RectF(0, 0, frameWidth, frameHeight);
            imageMatrix.setRectToRect(drawableRect, frameRect, Matrix.ScaleToFit.CENTER);

            //Retrieving the Matrix Values post Image Centering
            float[] matrixValues = new float[9]; // Matrix is a 3x3 Matrix, hence 9 is the size
            imageMatrix.getValues(matrixValues);

            //Retrieving the translation in Y dimension
            float translateY = matrixValues[Matrix.MTRANS_Y];

            if (translateY > 0) {
                //If there is translation in Y dimension, add the same amount
                //to bring the image to bottom center(from center),
                //leaving the translation in X AS-IS (pass 0)
                imageMatrix.postTranslate(0, translateY);
            }

            //Set scale type to Matrix
            setScaleType(ScaleType.MATRIX);

            //Pass the modified Matrix as the Image Matrix to be used
            setImageMatrix(imageMatrix);
        }
        //Calling to super and returning its value
        return super.setFrame(frameLeft, frameTop, frameRight, frameBottom);
    }
}