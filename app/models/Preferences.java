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

package models;

import java.io.File;

import play.data.validation.Constraints.Required;

/**
 * A bean that describes the user preferences and settings.
 * @author Vincent Zurczak - Linagora
 */
public class Preferences {

	// @Required
	public String logRootDirectory;
	public String localeId;


	/**
	 * Validates the log root directory.
	 * @return null if everything is fine, an error message otherwise
	 */
	public String validate() {

		if( this.logRootDirectory == null )
			return "The log root directory was not specified.";

		File f = new File( this.logRootDirectory );
		String result = null;
        if( ! f.exists())
            result = this.logRootDirectory + " does not exist.";
        else if( ! f.isDirectory())
        	result = this.logRootDirectory + " is not a directory.";

        return result;
    }
}
