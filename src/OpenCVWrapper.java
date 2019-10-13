package edu.temple.capstone.BinBotServer;

import java.awt.image.BufferedImage;

/******************************************************************************
 * Filename:		OpenCVWrapper.java
 * Creation Date:	Wed 09 Oct 2019 05:06:28 PM EDT
 * Last Modified:	Wed 09 Oct 2019 05:11:57 PM EDT
 * Purpose:			Wrapper for handling openCV logic. Created by providing an
 *                  and used to locate identified waste.
 ******************************************************************************/

public interface OpenCVWrapper
{
	/**
	 * <h1>getAppImg()</h1>
	 * This method returns a BufferedImage, which, if waste has been detected in it, will have a box surrounding that
	 * waste.
	 *
	 *
	 *
	 * @author  Sean DiGirolamo
	 * @version 1.0
	 * @since   2019-10-11
	 */
	BufferedImage getAppImg();

	/**
	 * <h1>getMatrix()</h1>
	 * This method returns an Object matrix which was generated by OpenCV. This matrix represents the image in and
	 * identification of a waste object and it's location
	 *
	 *
	 *
	 * @author  Sean DiGirolamo
	 * @version 1.0
	 * @since   2019-10-11
	 */
	Object[][] getMatrix();

	/**
	 * <h1>getX()</h1>
	 * This method returns the x value as a double of the origin of the section of the image identified to contain waste.
	 *
	 *
	 *
	 * @author  Sean DiGirolamo
	 * @version 1.0
	 * @since   2019-10-11
	 */
	double getX();

	/**
	 * <h1>getX()</h1>
	 * This method returns the y value as a double of the origin of the section of the image identified to contain waste.
	 *
	 *
	 *
	 * @author  Sean DiGirolamo
	 * @version 1.0
	 * @since   2019-10-11
	 */
	double getY();

	/**
	 * <h1>getX()</h1>
	 * This method returns the height value as a double of the section of the image identified to contain waste.
	 *
	 *
	 *
	 * @author  Sean DiGirolamo
	 * @version 1.0
	 * @since   2019-10-11
	 */
	double getHeight();

	/**
	 * <h1>getX()</h1>
	 * This method returns the width value as a double of the section of the image identified to contain waste.
	 *
	 *
	 *
	 * @author  Sean DiGirolamo
	 * @version 1.0
	 * @since   2019-10-11
	 */
	double getWidth();
}