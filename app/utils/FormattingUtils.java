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

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.ow2.petals.log.api.PetalsLogConstants;
import org.ow2.petals.log.api.model.FlowStep;

/**
 * Utilities related to formatting.
 * @author Vincent Zurczak - Linagora
 */
public class FormattingUtils {

	/**
	 * Formats a date.
	 * @param date
	 * @return a string representing a formatted date
	 */
	public static String formatDate( Long date ) {
		return new SimpleDateFormat( "yyyy/MM/dd 'at' HH:mm:ss:SSS" ).format( new Date( date ));
	}


	/**
	 * Formats a time.
	 * @param date
	 * @return a string representing a formatted time
	 */
	public static String formatTime( Long date ) {
		return new SimpleDateFormat( "HH:mm:ss,SSS" ).format( new Date( date ));
	}


	/**
	 * Formats the indentation for a flow step.
	 * @param indent
	 * @return a string to define a CSS margin
	 */
	public static String formatIndentationStyle( int indent ) {
		return indent == 0 ? "0" : (indent-1) * 25 + "px";
	}


	/**
	 * Formats a nice string to display a flow step.
	 * @param flowStep
	 * @return a HTML portion
	 */
	public static String formatFlowStepForListing( FlowStep flowStep ) {

		StringBuilder sb = new StringBuilder();
		String date = flowStep.getBeginProperties().get( PetalsLogConstants.DATE );

		Long rawDate = date != null ? MiscUtils.parsePetalsDate( date ) : null;
		sb.append( "<span class=\"l-date\">" );
		sb.append( rawDate != null ? formatTime( rawDate ) : date );
		sb.append( "</span> " );

		String node = flowStep.getBeginProperties().get( PetalsLogConstants.PETALS_NODE );
		if( node != null ) {
			sb.append( "<span class=\"l-node\">Node " );
			sb.append( node );
			sb.append( "</span> " );
		}

		String start = flowStep.getBeginProperties().get( PetalsLogConstants.COMPONENT_LOGGER );
		sb.append( "<span class=\"l-component\">" );
		sb.append( shortenComponentLoggerName( start ));
		sb.append( "</span> " );
		sb.append( " &#8594; ..." );

		return sb.toString();
	}


	/**
	 * Shortens a component logger name.
	 * @param loggerName
	 * @return
	 */
	public static String shortenComponentLoggerName( String loggerName ) {

		String result = "?";
		if( loggerName != null ) {
			int index = loggerName.lastIndexOf( '.' );
			if( index < loggerName.length() - 1 ) {
				result = loggerName.substring( index + 1 ).replace( '-', ' ' );
				if( result.toLowerCase().startsWith( "petals " ))
					result = result.substring( 7 );
			}
		}

		return result;
	}
}
