package edu.temple.capstone.BinBotServer;

import java.awt.image.BufferedImage;

/**
 * <h1>OpenCVWrapper</h1>
 * The OpenCVWrapper class serves as an easy to use front end for initiating OpenCV on an image and for retrieving
 * data about the image, if waste has been located, where it has been located, its height, its width, and the matrix
 * representation of that image itself.
 *
 *
 *
 * @author Sean DiGirolamo
 * @version 1.0
 * @since   2019-10-13
 */
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
