/**
 * Create Wavelet objects ...
 *
 * @author Christian Scheiblich (cscheiblich@gmail.com)
 * @date 14.03.2015 13:50:30 
 *
 * WaveletBuilder.java
 */
package com.example.alex.gait_authenticator.Waves;

import java.util.ArrayList;

import com.example.alex.gait_authenticator.Waves.exceptions.JWaveException;
import com.example.alex.gait_authenticator.Waves.exceptions.JWaveFailure;
import com.example.alex.gait_authenticator.Waves.daubechies.Daubechies2;
import com.example.alex.gait_authenticator.Waves.daubechies.Daubechies3;
import com.example.alex.gait_authenticator.Waves.daubechies.Daubechies4;
import com.example.alex.gait_authenticator.Waves.daubechies.Daubechies5;
import com.example.alex.gait_authenticator.Waves.daubechies.Daubechies6;
import com.example.alex.gait_authenticator.Waves.daubechies.Daubechies7;
import com.example.alex.gait_authenticator.Waves.daubechies.Daubechies8;

/**
 * Class for creating and identifying Wavelet object.
 * 
 * @author Christian Scheiblich (cscheiblich@gmail.com)
 * @date 14.03.2015 13:50:30
 */
public class WaveletBuilder {

  /**
   * Create a Wavelet object by string. Look into each Wavelet for matching
   * string identifier. By the way the method requires Java 7, due to the
   * switch statement with at String. *rofl*
   * 
   * @author Christian Scheiblich (cscheiblich@gmail.com)
   * @date 14.03.2015 14:19:09
   * @param waveletName
   *          identifier as stored in Wavelet object
   * @return a matching object of type Wavelet
   */
  static public Wavelet create( String waveletName ) {

    Wavelet wavelet = null;

    try {

      switch( waveletName ){

        case "Daubechies 2":
          wavelet = new Daubechies2( );
          break;

        case "Daubechies 3":
          wavelet = new Daubechies3( );
          break;

        case "Daubechies 4":
          wavelet = new Daubechies4( );
          break;

        case "Daubechies 5":
          wavelet = new Daubechies5( );
          break;

        case "Daubechies 6":
          wavelet = new Daubechies6( );
          break;

        case "Daubechies 7":
          wavelet = new Daubechies7( );
          break;

        case "Daubechies 8":
          wavelet = new Daubechies8( );
          break;

        case "Battle 23":
          // wavelet = new Battle23( );
          throw new JWaveFailure(
              "WaveletBuilder::create - Battle23 - "
                  + "This wavelet has an odd number of coefficients, "
                  + "due to that it is not comaptible to the implemented algorithms; somehow!" );
          // break;

        case "CDF 5/3":
          // wavelet = new CDF53( );
          throw new JWaveFailure(
              "WaveletBuilder::create - CDF 5/3 - "
                  + "This wavelet has an odd number of coefficients, "
                  + "due to that it is not comaptible to the implemented algorithms; somehow!" );
          // break;

        case "CDF 9/7":
          // wavelet = new CDF97( );
          throw new JWaveFailure(
              "WaveletBuilder::create - CDF 9/7 - "
                  + "This wavelet has an odd number of coefficients, "
                  + "due to that it is not comaptible to the implemented algorithms; somehow!" );
          // break;

        default:

          throw new JWaveFailure(
              "WaveletBuilder::create - unknown type of wavelet for given string!" );

      } // switch

    } catch( JWaveException e ) {

      e.showMessage( );
      e.printStackTrace( );

    } // try

    return wavelet;

  } // create

  /**
   * Returns the identifier string of a given Wavelet object.
   * 
   * @author Christian Scheiblich (cscheiblich@gmail.com)
   * @date 14.03.2015 14:22:22
   * @param wavelet
   *          an object of type Wavelet
   * @return identifier string of a given Wavelet object
   */
  static public String identify( Wavelet wavelet ) {

    return wavelet.getName( );

  } // identify

  /**
   * Create an array keeping all - available - wavelet objects.
   * 
   * @author Christian Scheiblich (cscheiblich@gmail.com)
   * @date 22.03.2015 15:39:15
   * @return an array keeping all Wavelet objects
   */
  static public Wavelet[ ] create2arr( ) {

    ArrayList< Wavelet > listWavelets = new ArrayList< Wavelet >( );

    listWavelets.add( WaveletBuilder.create( "Daubechies 2" ) );
    listWavelets.add( WaveletBuilder.create( "Daubechies 3" ) );
    listWavelets.add( WaveletBuilder.create( "Daubechies 4" ) );
    listWavelets.add( WaveletBuilder.create( "Daubechies 5" ) );
    listWavelets.add( WaveletBuilder.create( "Daubechies 6" ) );
    listWavelets.add( WaveletBuilder.create( "Daubechies 7" ) );
    listWavelets.add( WaveletBuilder.create( "Daubechies 8" ) );

    int noOfWavelets = listWavelets.size( );
    Wavelet[ ] arrWavelets = new Wavelet[ noOfWavelets ];
    for( int w = 0; w < noOfWavelets; w++ )
      arrWavelets[ w ] = listWavelets.get( w );

    return arrWavelets;

  } // create2arr
  
} // class
