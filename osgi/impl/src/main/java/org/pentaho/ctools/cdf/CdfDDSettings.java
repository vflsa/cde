package org.pentaho.ctools.cdf;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;
import org.pentaho.ctools.cde.environment.CdeEnvironment;
import org.pentaho.ctools.cde.environment.ICdeEnvironmentExtended;
import pt.webdetails.cdf.dd.CdeEngine;
import pt.webdetails.cpf.PluginSettings;
import pt.webdetails.cpf.packager.origin.PathOrigin;
import pt.webdetails.cpf.packager.origin.PluginRepositoryOrigin;
import pt.webdetails.cpf.packager.origin.StaticSystemOrigin;
import pt.webdetails.cpf.repository.api.IRWAccess;


import java.util.ArrayList;
import java.util.List;

public class CdfDDSettings extends PluginSettings {

  protected static Log logger = LogFactory.getLog(CdfDDSettings.class);
  private ICdeEnvironmentExtended osgiEnvironment;

  public CdfDDSettings( IRWAccess writeAccess ){
    super( writeAccess ); // useful when unit testing / mocking
    osgiEnvironment = (CdeEnvironment) CdeEngine.getEnv();
  }

  public CdfDDSettings() {
    super(( (CdeEnvironment) CdeEngine.getEnv() ).getPluginSystemWriter());
    osgiEnvironment = (CdeEnvironment) CdeEngine.getEnv();
  }

  List<Element> getComponentLocationElements(){
    return getSettingsXmlSection("custom-components/path");
  }

  public List<PathOrigin> getComponentLocations() {
    List<Element> pathElements = getSettingsXmlSection("custom-components/path");
    ArrayList<PathOrigin> locations = new ArrayList<PathOrigin>();
    for (Element pathElement : pathElements) {
      String path = StringUtils.strip(pathElement.getTextTrim());
      String origin = pathElement.attributeValue("origin");
      if(!StringUtils.isEmpty(origin)) {
        if (StringUtils.equals(origin, "static")) {
          locations.add(new StaticSystemOrigin(path));
        }
        else if (StringUtils.equals(origin, "repo")) {
          locations.add(new PluginRepositoryOrigin(osgiEnvironment.getPluginRepositoryDir(), path));
        }
      }
      else {
        logger.error("Must specify origin (static|repo), location '" + path + " 'ignored.");
      }
    }
    return locations;
  }

  public String[] getFilePickerHiddenFoldersByType( CdeSettings.FolderType folderType ) {

    List<String> hiddenFolders = new ArrayList<String>();
    List<Element> xmlPathElements = getSettingsXmlSection( "file-picker/hidden-folders/path" );

    if( xmlPathElements != null ) {

      for ( Element xmlPathElement : xmlPathElements ) {

        String path = StringUtils.strip( xmlPathElement.getTextTrim() );
        String origin = xmlPathElement.attributeValue( "origin" );

        if ( StringUtils.isEmpty( path ) || StringUtils.isEmpty( origin ) ) {
          logger.error( "Must specify origin (static|repo) and location '" + path + "." );
          continue;
        }

        if ( folderType == CdeSettings.FolderType.valueOf( origin.toUpperCase() ) ) {
          hiddenFolders.add( path );
        }
      }
    }

    return hiddenFolders.toArray( new String[ hiddenFolders.size() ] );
  }
}
