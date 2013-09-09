/****************************************************************************
 *
 * Copyright (c) 2013, Linagora
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *****************************************************************************/

package utils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.ow2.petals.log.parser.api.PetalsLogUtils;
import org.ow2.petals.log.parser.api.model.FlowStep;

import models.Preferences;

/**
 * @author Vincent Zurczak - Linagora
 */
public class MiscUtils {

	private static final String PREFS_LOG_DIRECTORY = "preferences.log.directory";
	private static final String PREFS_LOCALE = "preferences.locale";
	private static final String DEFAULT_PREFS_LOCALE = "en_US";

	private static Preferences cachedPreferences;


	/**
	 * Loads the preferences.
	 * @return the preferences (not null)
	 */
	public static Preferences loadPreferences() {

		if( cachedPreferences == null ) {
			java.util.prefs.Preferences sysPrefs = java.util.prefs.Preferences.userNodeForPackage( MiscUtils.class );

			cachedPreferences = new Preferences();
			cachedPreferences.logRootDirectory = sysPrefs.get( PREFS_LOG_DIRECTORY, "" );
			cachedPreferences.localeId = sysPrefs.get( PREFS_LOCALE, DEFAULT_PREFS_LOCALE );
		}

		return cachedPreferences;
	}


	/**
	 * Saves the preferences.
	 * @param prefs the preferences (not null)
	 */
	public static void savePreferences( Preferences prefs ) {

		java.util.prefs.Preferences sysPrefs = java.util.prefs.Preferences.userNodeForPackage( MiscUtils.class );

		sysPrefs.put( PREFS_LOG_DIRECTORY, prefs.logRootDirectory );
		sysPrefs.put( PREFS_LOCALE, prefs.localeId );
		cachedPreferences = prefs;
	}


	/**
	 * Transforms a Petals date (found in the logs) in a time stamp.
	 * @param date
	 * @return a time stamp or null if the date could not be parsed
	 */
	public static Long parsePetalsDate( String date ) {

		// Hack: fix the time zone
		if( date.contains( "GMT" )
				&& date.endsWith( "00" )
				&& ! date.endsWith( ":00" ))
			date = new StringBuilder( date ).insert( date.length() - 2, ":" ).toString();

		// Parse the updated date
		Long result = null;
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss,S z" );
		try {
			result = sdf.parse( date ).getTime();

		} catch( ParseException e ) {
			// nothing
		}

		return result;
	}


	/**
	 * Tests whether a filer path targets an existing file.
	 * @param filePath a file path (can be null)
	 * @return true if this path points to an existing file, false otherwise
	 */
	public static boolean exists( String filePath ) {
		return filePath != null && filePath.length() > 0 && new File( filePath ).exists();
	}


	/**
	 * Determines whether a flow contains errors.
	 * @param flow a flow (not null)
	 * @return true if this flow contains errors, false otherwise
	 */
	public static boolean containsFailures( FlowStep step ) {

		for( FlowStep sf : PetalsLogUtils.linearizeFlowTree( step ).keySet()) {
			if( sf.isFailure())
				return true;
		}

		return false;
	}
}
