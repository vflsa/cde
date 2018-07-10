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

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.json.JSONException;
import org.pentaho.ctools.cde.utils.XSSHelper;

import pt.webdetails.cdf.dd.DashboardDesignerException;
import pt.webdetails.cdf.dd.Messages;
import pt.webdetails.cdf.dd.structure.DashboardStructure;
import pt.webdetails.cdf.dd.structure.DashboardStructureException;
import pt.webdetails.cdf.dd.util.JsonUtils;
import pt.webdetails.cdf.dd.util.Utils;
import pt.webdetails.cdf.dd.CdeConstants.MethodParams;
import pt.webdetails.cpf.repository.api.IReadAccess;
import pt.webdetails.cpf.utils.CharsetHelper;
import org.pentaho.ctools.cdf.CdfStyles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;

@Path( "synchronizer" )
public class SynchronizerApi {
  private static final Log logger = LogFactory.getLog( SynchronizerApi.class );

  private static final String OPERATION_LOAD = "load";
  private static final String OPERATION_DELETE = "delete";
  private static final String OPERATION_DELETE_PREVIEW = "deletepreview";
  private static final String OPERATION_SAVE = "save";
  private static final String OPERATION_SAVE_AS = "saveas";
  private static final String OPERATION_NEW_FILE = "newfile";
  private static final String OPERATION_SAVE_SETTINGS = "savesettings";

  private static final String GET_RESOURCE = "/resources/get?resource=";
  /**
   * for historical reasons..
   */
  public static final String UNSAVED_FILE_PATH = "null/null/null";


  @POST
  @Path( "/synchronizeDashboard" )
  @Produces(APPLICATION_JSON)
  public String synchronize( @FormParam(MethodParams.FILE) @DefaultValue("") String file,
                           @FormParam(MethodParams.PATH) @DefaultValue("") String path,
                           @FormParam(MethodParams.TITLE) @DefaultValue("") String title,
                           @FormParam(MethodParams.AUTHOR) @DefaultValue("") String author,
                           @FormParam(MethodParams.DESCRIPTION) @DefaultValue("") String description,
                           @FormParam(MethodParams.STYLE) @DefaultValue("") String style,
                           @FormParam(MethodParams.WIDGET_NAME) @DefaultValue("") String widgetName,
                           @FormParam(MethodParams.WIDGET) boolean widget,
                           @FormParam(MethodParams.RENDERER_TYPE) @DefaultValue("") String rendererType,
                           @FormParam(MethodParams.WIDGET_PARAMETERS) List<String> widgetParams,
                           @FormParam(MethodParams.DASHBOARD_STRUCTURE) String cdfStructure,
                           @FormParam(MethodParams.OPERATION) String operation,
                           @FormParam(MethodParams.REQUIRE) boolean require,
                           @Context HttpServletRequest servletRequest,
                           @Context HttpServletResponse servletResponse ) throws Exception {

    final XSSHelper xssHelper = XSSHelper.getInstance();

    file = xssHelper.escape( file );
    title = xssHelper.escape( title );
    author = xssHelper.escape( author );
    description = xssHelper.escape( description );
    style = xssHelper.escape( style );
    widgetName = xssHelper.escape( widgetName );
    rendererType = xssHelper.escape( rendererType );
    cdfStructure = xssHelper.escape( cdfStructure );
    operation = xssHelper.escape( operation );

    if ( null != widgetParams ) {
      for ( int i = 0; i < widgetParams.size(); i++ ) {
        widgetParams.add(i, xssHelper.escape( widgetParams.get( i ) ) );
      }
    }

    servletResponse.setContentType( APPLICATION_JSON );
    servletResponse.setCharacterEncoding( CharsetHelper.getEncoding() );

    boolean isPreview = false;

    if ( !file.isEmpty() && !file.equals( UNSAVED_FILE_PATH ) ) {
      file = Utils.getURLDecoded( file, CharsetHelper.getEncoding() );

      isPreview = ( file.contains( "_tmp.cdfde" ) || file.contains( "_tmp.wcdf" ) );

      IReadAccess rwAccess = Utils.getSystemOrUserRWAccess( file );
      if ( rwAccess == null ) {
        String msg = "Access denied for the synchronize method syncronizeDashboard." + operation + " : " + file;
        logger.warn( msg );
        return JsonUtils.getJsonResult( false, msg );
      }
    }

    try {
      HashMap<String, Object> params = new HashMap<>();
      params.put(MethodParams.FILE, file);
      params.put(MethodParams.WIDGET, String.valueOf(widget));
      params.put(MethodParams.REQUIRE, String.valueOf(require));

      if (!author.isEmpty()) {
        params.put(MethodParams.AUTHOR, author);
      }

      if (!style.isEmpty()) {
        params.put(MethodParams.STYLE, style);
      }

      if (!widgetName.isEmpty()) {
        params.put(MethodParams.WIDGET_NAME, widgetName);
      }

      if (!rendererType.isEmpty()) {
        params.put(MethodParams.RENDERER_TYPE, rendererType);
      }

      if (!title.isEmpty()) {
        params.put(MethodParams.TITLE, title);
      }

      if (!description.isEmpty()) {
        params.put(MethodParams.DESCRIPTION, description);
      }

      String[] widgetParameters = widgetParams.toArray(new String[0]);
      if (widgetParameters.length > 0) {
        params.put(MethodParams.WIDGET_PARAMETERS, widgetParameters);
      }

      final String wcdfdeFile = file.replace(".wcdf", ".cdfde");
      final DashboardStructure dashboardStructure = new DashboardStructure();

      Object result = null;
      if (OPERATION_LOAD.equalsIgnoreCase(operation)) {
        return dashboardStructure.load(wcdfdeFile);
      }

      if (OPERATION_DELETE.equalsIgnoreCase(operation)) {
        dashboardStructure.delete(params);

      } else if (OPERATION_DELETE_PREVIEW.equalsIgnoreCase(operation)) {
        dashboardStructure.deletePreviewFiles(wcdfdeFile);

      } else if (OPERATION_SAVE.equalsIgnoreCase(operation)) {
        result = dashboardStructure.save(file, cdfStructure);

      } else if (OPERATION_SAVE_AS.equalsIgnoreCase(operation)) {
        if (StringUtils.isEmpty(title)) {
          title = FilenameUtils.getBaseName(file);
        }

        result = dashboardStructure.saveAs(file, title, description, cdfStructure, isPreview);

      } else if (OPERATION_NEW_FILE.equalsIgnoreCase(operation)) {
        dashboardStructure.newfile(params);

      } else if (OPERATION_SAVE_SETTINGS.equalsIgnoreCase(operation)) {
        // check if user is attempting to save settings over a new (non yet saved) dashboard/widget/template
        if (StringUtils.isEmpty(file) || file.equals(UNSAVED_FILE_PATH)) {
          logger.warn(getMessage("CdfTemplates.ERROR_003_SAVE_DASHBOARD_FIRST"));
          return JsonUtils.getJsonResult(false, getMessage("CdfTemplates.ERROR_003_SAVE_DASHBOARD_FIRST"));
        }

        result = dashboardStructure.saveSettingsToWcdf(params);

      } else {
        logger.error("Unknown operation: " + operation);
      }

      return JsonUtils.getJsonResult(true, result);

    } catch (Exception e) {
      if (e.getCause() != null) {
        handleDashboardStructureException(e, servletResponse.getOutputStream());
      }
      throw e;
    }
  }

  //useful to mock message bundle when unit testing SyncronizerApi
  protected String getMessage(String key) {
    return Messages.getString(key);
  }

  private void handleDashboardStructureException(Exception e, OutputStream out) throws Exception {
    Throwable cause = e.getCause();
    if (cause instanceof DashboardStructureException) {
      JsonUtils.buildJsonResult(out, false, cause.getMessage());

    } else if (e instanceof InvocationTargetException) {
      throw (Exception) cause;
    }
  }

  @GET
  @Path( "/synchronizeStyles" )
  @Produces( APPLICATION_JSON )
  public void syncStyles( @Context HttpServletResponse servletResponse )
          throws IOException, DashboardDesignerException, JSONException {

    servletResponse.setContentType( APPLICATION_JSON );
    servletResponse.setCharacterEncoding( CharsetHelper.getEncoding() );

    listStyles( servletResponse );
  }

  protected void listStyles( HttpServletResponse servletResponse )
          throws IOException, DashboardDesignerException, JSONException {

    final CdfStyles cdfStyles = new CdfStyles();
    JsonUtils.buildJsonResult( servletResponse.getOutputStream(), true, cdfStyles.liststyles() );
  }

  @POST
  @Path( "/saveDashboard" )
  @Consumes( MULTIPART_FORM_DATA )
  @Produces( APPLICATION_JSON )
  public String saveDashboard( @Multipart( value = MethodParams.FILE, type = "text/plain", required = false )
                                         String file,
                               @Multipart( value = MethodParams.TITLE, type = "text/plain", required = false )
                                         String title,
                               @Multipart( value = MethodParams.DESCRIPTION, type = "text/plain", required = false )
                                         String description,
                               @Multipart( value = MethodParams.DASHBOARD_STRUCTURE, type = "text/plain" )
                                         String cdfStructure,
                               @Multipart( value = MethodParams.OPERATION, type = "text/plain" )
                                         String operation,
                               @Context HttpServletResponse response ) throws Exception {

    final XSSHelper xssHelper = XSSHelper.getInstance();

    file = xssHelper.escape( file );
    title = xssHelper.escape( title );
    description = xssHelper.escape( description );
    cdfStructure = xssHelper.escape( cdfStructure );
    operation = xssHelper.escape( operation );

    response.setContentType( APPLICATION_JSON );
    response.setCharacterEncoding( CharsetHelper.getEncoding() );

    boolean isPreview = false;

    if ( !file.isEmpty()
            && !( file.equals( UNSAVED_FILE_PATH ) || Utils.getURLDecoded( file ).equals( UNSAVED_FILE_PATH ) ) ) {

      file = Utils.getURLDecoded( file, CharsetHelper.getEncoding() );

      if ( StringUtils.isEmpty( title ) ) {
        title = FilenameUtils.getBaseName( file );
      }

      // check access to path folder
      String fileDir =
              file.contains( ".wcdf" ) || file.contains( ".cdfde" ) ? file.substring( 0, file.lastIndexOf( "/" ) ) : file;

      isPreview = ( file.contains( "_tmp.cdfde" ) || file.contains( "_tmp.wcdf" ) );

      IReadAccess rwAccess;
      if ( OPERATION_SAVE_AS.equalsIgnoreCase( operation ) && !isPreview ) {
        rwAccess = Utils.getSystemOrUserRWAccess( fileDir );
      } else {
        rwAccess = Utils.getSystemOrUserRWAccess( file );
      }

      if ( rwAccess == null ) {
        String msg = "Access denied for the syncronize method saveDashboard." + operation + " : " + file;
        logger.warn( msg );
        return JsonUtils.getJsonResult( false, msg );
      }
    }

    try {
      final DashboardStructure dashboardStructure = new DashboardStructure();
      Object result = null;


      if ( OPERATION_SAVE.equalsIgnoreCase( operation ) ) {
        result = dashboardStructure.save( file, cdfStructure );
      } else if ( OPERATION_SAVE_AS.equalsIgnoreCase( operation ) ) {
        result = dashboardStructure.saveAs( file, title, description, cdfStructure, isPreview );
      } else {
        logger.error( "Unknown operation: " + operation );
      }
      return JsonUtils.getJsonResult( true, result );
    } catch ( Exception e ) {
      if ( e.getCause() != null ) {
        handleDashboardStructureException( e, response.getOutputStream() );
      }

      throw e;
    }
  }
}
