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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import org.ow2.petals.log.api.model.FlowStep;

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
	 * Linearize a flow step tree.
	 * <p>
	 * A flow is simply a tree, with a root.<br />
	 * This root is a flow step. A flow step has children.
	 * It generally has a parent, except the root.So, a flow is simply a
	 * tree of flow steps.
	 * </p>
	 * <p>
	 * This function builds a list of flow steps from such a tree.<br />
	 * The tree run starts with the root, then the first child, then the first grand-child...
	 * Once all the children of a flow step have been processed, its siblings are treated.
	 * </p>
	 *
	 * @param flowStep a flow step (not null)
	 * @return a non-null map, where the key is the flow step ID and the value is the indentation level
	 */
	public static Map<FlowStep,Integer> linearizeFlowTree( FlowStep flowStep ) {

		Map<FlowStep,Integer> flowStepIdToIndentation = new LinkedHashMap<FlowStep,Integer> ();
		FlowStep current = flowStep;
		int level = 0;
		nextOne: while( current != null ) {

			// Process the current step
			// Precondition: "current" was not already added in the map.
			flowStepIdToIndentation.put( current, level );

			// Find the next one in the children
			while( current != null ) {
				// Precondition: the parent has already been processed
				for( FlowStep child : current.getChildren()) {
					if( ! flowStepIdToIndentation.containsKey( child )) {
						current = child;
						level ++;
						continue nextOne;
					}
				}

				// The root flow step does not have a parent
				current = current.getParent();
				level --;

				// If we quit this loop with current = null, we will exit the other loop.
			}

			// Postcondition: "current" has not been added in the map.
		}

		return flowStepIdToIndentation;
	}
}
