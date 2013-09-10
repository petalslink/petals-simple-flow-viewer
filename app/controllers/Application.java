/**
 * Copyright (c) 2013 Linagora
 *
 * This program/library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This program/library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program/library; If not, see <http://www.gnu.org/licenses/>
 * for the GNU Lesser General Public License version 2.1.
 */
package controllers;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.ow2.petals.log.parser.api.FlowBuilder;
import org.ow2.petals.log.parser.api.PetalsLogUtils;
import org.ow2.petals.log.parser.api.model.Flow;
import org.ow2.petals.log.parser.api.model.FlowStep;

import models.Preferences;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utils.MiscUtils;
import views.html.*;

/**
 * @author Vincent Zurczak - Linagora
 */
public class Application extends Controller {

	static Form<Preferences> prefsForm = Form.form( Preferences.class );


	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}


	public static Result about() {
		return ok( about.render());
	}


	public static Result help() {
		return ok( help.render());
	}


	public static Result preferences() {

		Map<String,String> locales = new LinkedHashMap<String,String> ();
		locales.put( "en_US", "English" );
		locales.put( "fr_FR", "Français" );

		prefsForm = prefsForm.fill( MiscUtils.loadPreferences());
		return ok( preferences.render( locales, prefsForm ));
	}


	public static Result updatePreferences() {

		final Form<Preferences> filledForm = prefsForm.bindFromRequest();
		Preferences newPrefs = filledForm.get();
		MiscUtils.savePreferences( newPrefs );

		return redirect( routes.Application.preferences());
	}


	public static Result flows() throws Exception {

		String dir = MiscUtils.loadPreferences().logRootDirectory;
		FlowBuilder flowBuilder = new FlowBuilder();
		flowBuilder.setLogDirectories( dir );

		return ok( flows.render( flowBuilder.findFlowIds()));
	}


	public static Result flow( String flowId ) {

		String dir = MiscUtils.loadPreferences().logRootDirectory;
		FlowBuilder flowBuilder = new FlowBuilder();
		flowBuilder.setLogDirectories( dir );

		Flow flowObject = flowBuilder.parseFlow( flowId );
		return ok( flow.render( flowObject ));
	}


	public static Result step( String flowId, String stepId ) {

		String dir = MiscUtils.loadPreferences().logRootDirectory;
		FlowBuilder flowBuilder = new FlowBuilder();
		flowBuilder.setLogDirectories( dir );

		Flow flowObject = flowBuilder.parseFlow( flowId );
		FlowStep stepObject = PetalsLogUtils.findFlowStep( flowObject, stepId );

		return ok( step.render( stepObject, flowId ));
	}


	public static Result download( String filePath ) {

		File f = new File( filePath );
		if( ! f.exists()) {
			return notFound( dl_error.render( filePath ));

		} else {
			LinkedHashMap<String,String> props = new LinkedHashMap<String,String> ();
			props.put( Http.HeaderNames.CACHE_CONTROL, "public" );
			props.put( "Content-Description", "File Transfer" );
			props.put( "Content-Disposition", "attachment; filename=" + f.getName());
			props.put( Http.HeaderNames.CONTENT_TYPE, "text/plain" );
			props.put( Http.HeaderNames.CONTENT_TRANSFER_ENCODING, "binary" );
			props.put( Http.HeaderNames.CONTENT_LENGTH, String.valueOf( f.length()));

			for( Map.Entry<String,String> entry : props.entrySet()) {
				response().setHeader( entry.getKey(), entry.getValue());
			}

			return ok( f );
		}
	}
}
