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
package org.pentaho.ctools.cde.api;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.pentaho.ctools.cde.environment.CdeEnvironment;
import org.pentaho.ctools.cde.environment.ICdeEnvironmentExtended;
import org.pentaho.ctools.cde.utils.XSSHelper;
import org.pentaho.ctools.cdf.CdeSettings;
import pt.webdetails.cdf.dd.CdeEngine;
import pt.webdetails.cdf.dd.util.GenericBasicFileFilter;
import pt.webdetails.cdf.dd.util.GenericFileAndDirectoryFilter;
import pt.webdetails.cdf.dd.util.Utils;
import pt.webdetails.cpf.repository.api.IBasicFile;
import pt.webdetails.cpf.repository.api.IReadAccess;
import pt.webdetails.cpf.repository.util.RepositoryHelper;
import pt.webdetails.cpf.utils.MimeTypes;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@Path("resources")
public class ResourcesApi {
  private static final Log logger = LogFactory.getLog(ResourcesApi.class);
  private String resourceMaxAge;
  private ICdeEnvironmentExtended osgiEnvironment;

  private ICdeEnvironmentExtended getEnv() {
    if (this.osgiEnvironment == null) {
      this.osgiEnvironment = (CdeEnvironment) CdeEngine.getEnv();;
    }
    return this.osgiEnvironment;
  }

  public void setResourceMaxAge(String maxAge) {
    this.resourceMaxAge = maxAge;
  }

  public String getResourceMaxAge() {
    return this.resourceMaxAge;
  }

  @GET
  @Path("/{resource: [^?]+ }")
  public Response resource(@PathParam("resource") String resource) throws IOException {
    IBasicFile file = getFile(resource);
    if (file == null) {
      logger.error("resource not found:" + resource);

      return Response.serverError().build();
    }

    final String filename = file.getName();

    Response.ResponseBuilder response = Response.ok(file.getContents());

    response
            .header("Content-Type", getResourceMimeType(filename))
            .header("content-disposition", "inline; filename=\"" + filename + "\"");

    String maxAge = getResourceMaxAge();
    if (maxAge != null) {
      response.header("Cache-Control", "max-age=" + maxAge);
    }

    return response.build();
  }

  IBasicFile getFile(String path) {
    return Utils.getFileViaAppropriateReadAccess(path);
  }

  private String getResourceMimeType(String filename) {
    return MimeTypes.getMimeType(filename);
  }

  @POST
  @Path( "/explore" )
  @Produces( TEXT_PLAIN )
  public String exploreFolder( @FormParam( "dir" ) @DefaultValue( "/" ) String folder,
                               @FormParam( "outputType" ) String outputType,
                               @QueryParam( "dashboardPath" ) @DefaultValue( "" ) String dashboardPath,
                               @QueryParam( "fileExtensions" ) String fileExtensions,
                               @QueryParam( "access" ) String access,
                               @QueryParam( "showHiddenFiles" ) @DefaultValue( "false" ) boolean showHiddenFiles ) {

    folder = decodeAndEscape( folder );
    outputType = decodeAndEscape( outputType );
    dashboardPath = decodeAndEscape( dashboardPath );
    fileExtensions = decodeAndEscape( fileExtensions );

    if ( !StringUtils.isEmpty( outputType ) && outputType.equals( "json" ) ) {
      try {
        IBasicFile[] files = getFileList( folder, dashboardPath, fileExtensions, showHiddenFiles );
        return RepositoryHelper.toJSON( folder, files );
      } catch ( JSONException e ) {
        logger.error( "exploreFolder" + folder, e );
        return "Error getting files in folder " + folder;
      }
    } else {
      IBasicFile[] files = getFileList( folder, dashboardPath, fileExtensions, showHiddenFiles );
      return RepositoryHelper.toJQueryFileTree( folder, files );
    }
  }

  private IBasicFile[] getFileList( String dir, String dashboardPath, final String fileExtensions,
                                    boolean showHiddenFiles ) {

    ArrayList<String> extensionsList = new ArrayList<>();
    String[] extensions = StringUtils.split( fileExtensions, "." );
    if ( extensions != null ) {
      for ( String extension : extensions ) {
        // For some reason, in 4.5 file-based rep started to report a leading dot in extensions
        // Adding both just to be sure we don't break stuff
        extensionsList.add( "." + extension );
        extensionsList.add( extension );
      }
    }

    GenericBasicFileFilter fileFilter = new GenericBasicFileFilter(
            null, extensionsList.toArray( new String[ extensionsList.size() ] ), true );

    //check if it is a system dashboard
    List<IBasicFile> fileList;
    boolean isSystem = false;
    if ( !dashboardPath.isEmpty() ) {
      String path = dashboardPath.toLowerCase().replaceFirst( "/", "" );
      if ( path.startsWith( getEnv().getSystemDir() + "/" ) ) {
        isSystem = true;
      }
    }

    IReadAccess access = getEnv().getContentAccessFactory().getUserContentAccess( dashboardPath );
    GenericFileAndDirectoryFilter fileAndDirFilter = new GenericFileAndDirectoryFilter( fileFilter );

    if ( isSystem ) {
      // folder filtering ( see settings.xml ) will only occur for non-admin users
      if ( !isAdministrator() ) {
        fileAndDirFilter.setDirectories( CdeSettings.getFilePickerHiddenFolderPaths( CdeSettings.FolderType.STATIC ) );
        fileAndDirFilter.setFilterType( GenericFileAndDirectoryFilter.FilterType.FILTER_OUT ); // act as a black-list
      }
      fileList = access.listFiles( dir, fileAndDirFilter, 1, true, false );
      fileList.remove( 0 ); //remove the first because the root is being added
    } else {
      // folder filtering ( see settings.xml ) will only occur for non-admin users
      if ( !isAdministrator() ) {
        fileAndDirFilter.setDirectories( CdeSettings.getFilePickerHiddenFolderPaths( CdeSettings.FolderType.REPO ) );
        fileAndDirFilter.setFilterType( GenericFileAndDirectoryFilter.FilterType.FILTER_OUT ); // act as a black-list
      }
      fileList = access.listFiles( dir, fileAndDirFilter, 1, true, showHiddenFiles );
    }

    if ( fileList != null && fileList.size() > 0 ) {
      return fileList.toArray( new IBasicFile[ fileList.size() ] );
    }

    return new IBasicFile[] { };
  }

  private String decodeAndEscape( String path ) {
    final XSSHelper helper = XSSHelper.getInstance();

    return helper.escape( Utils.getURLDecoded( path ) );
  }

  protected boolean isAdministrator() {
    return getEnv().getUserSession().isAdministrator();
  }

}
