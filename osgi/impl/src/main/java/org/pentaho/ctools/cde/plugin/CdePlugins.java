/*!
 * Copyright 2018 Webdetails, a Hitachi Vantara company. All rights reserved.
 *
 * This software was developed by Webdetails and is provided under the terms
 * of the Mozilla Public License, Version 2.0, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://mozilla.org/MPL/2.0/. The Initial Developer is Webdetails.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 */
package org.pentaho.ctools.cde.plugin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pentaho.ctools.cde.environment.CdeEnvironment;
import org.pentaho.ctools.cde.environment.ICdeEnvironmentExtended;
import pt.webdetails.cdf.dd.CdeEngine;

import java.util.List;

public class CdePlugins {
  private static Log logger = LogFactory.getLog( CdePlugins.class );
  private ICdeEnvironmentExtended osgiEnvironment;

  private ICdeEnvironmentExtended getEnv() {
    if (this.osgiEnvironment == null) {
      this.osgiEnvironment = (CdeEnvironment) CdeEngine.getEnv();;
    }
    return this.osgiEnvironment;
  }

  public String getCdePlugins() {

    JSONArray pluginsArray = new JSONArray();

    //TODO: Get plugins info from somewhere, in case there are actually plugins compatible with cde...
    //Currently the object Plugin exists in cpf-pentaho

    /*PluginsAnalyzer pluginsAnalyzer =
            new PluginsAnalyzer( CdeEnvironment.getContentAccessFactory(), PentahoSystem.get( IPluginManager.class ) );
    pluginsAnalyzer.refresh();

    IPluginFilter pluginFilter = new IPluginFilter() {
      public boolean include( Plugin plugin ) {
        boolean include = false;
        if ( plugin.hasSettingsXML() ) {
          include =
                  ( plugin.getXmlValue( "/settings/cde-compatible", "settings.xml" ).equals( "true" ) ) ? true : false;
        }
        return include;
      }
    };

    List<Plugin> cdePlugins = pluginsAnalyzer.getPlugins( pluginFilter );



    for ( Plugin plugin : cdePlugins ) {
      try {
        JSONObject pluginObject = new JSONObject();
        pluginObject.put( "title", plugin.getId() );
        pluginObject.put( "name", plugin.getName() );
        pluginObject.put( "description", plugin.getXmlValue( "/settings/description", "settings.xml" ) );
        pluginObject.put( "url", plugin.getXmlValue( "/settings/url", "settings.xml" ) );
        pluginObject.put( "jsPath", plugin.getXmlValue( "/settings/jsPath", "settings.xml" ) );
        pluginObject.put( "pluginId", plugin.getId() );

        pluginsArray.put( pluginObject );
      } catch ( Exception e ) {
        logger.error( e );
      }
    }*/

    /*JSONObject pluginObject = new JSONObject();
    pluginObject.put( "title", "CDE" );
    pluginObject.put( "name", "CDE" );
    pluginObject.put( "description", "CDE description", "settings.xml" );
    pluginObject.put( "url", "/settings/url" );
    pluginObject.put( "jsPath", "/settings/jsPath" );
    pluginObject.put( "pluginId", "cde" );

    pluginsArray.put( pluginObject );*/

    logger.fatal( "getCdePlugins() - Not implemented for the OSGi environment" );

    return pluginsArray.toString();
  }
}
