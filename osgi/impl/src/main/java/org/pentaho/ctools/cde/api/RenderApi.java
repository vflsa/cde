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

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static pt.webdetails.cpf.utils.MimeTypes.JAVASCRIPT;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.ctools.cde.environment.ICdeEnvironmentExtended;
import org.pentaho.ctools.cde.environment.CdeEnvironment;
import pt.webdetails.cdf.dd.CdeEngine;
import pt.webdetails.cdf.dd.DashboardManager;
import pt.webdetails.cdf.dd.CdeConstants;
import pt.webdetails.cdf.dd.Messages;
import pt.webdetails.cdf.dd.MetaModelManager;
import pt.webdetails.cdf.dd.CdeConstants.MethodParams;
import pt.webdetails.cdf.dd.editor.DashboardEditor;
import pt.webdetails.cdf.dd.model.core.writer.ThingWriteException;
import pt.webdetails.cdf.dd.model.inst.writer.cdfrunjs.dashboard.CdfRunJsDashboardWriteOptions;
import pt.webdetails.cdf.dd.model.inst.writer.cdfrunjs.dashboard.CdfRunJsDashboardWriteResult;
import pt.webdetails.cdf.dd.structure.DashboardWcdfDescriptor;
import pt.webdetails.cdf.dd.util.Utils;
import pt.webdetails.cpf.Util;
import pt.webdetails.cpf.localization.MessageBundlesHelper;


import pt.webdetails.cpf.repository.api.IReadAccess;

import java.util.UUID;

@Path( "renderer" )
public class RenderApi {

  private static final Log logger = LogFactory.getLog(RenderApi.class);
  private ICdeEnvironmentExtended osgiEnvironment;
  private DashboardManager dashboardManager;

  @GET
  @Path( "/getDashboard" )
  @Produces(APPLICATION_JSON)
  public String getDashboard(@QueryParam(MethodParams.PATH) @DefaultValue("") String path,
                             @QueryParam(MethodParams.INFERSCHEME) @DefaultValue("false") boolean inferScheme,
                             @QueryParam(MethodParams.ROOT) @DefaultValue("") String root,
                             @QueryParam(MethodParams.ABSOLUTE) @DefaultValue("true") boolean absolute,
                             @QueryParam(MethodParams.BYPASSCACHE) @DefaultValue("false") boolean bypassCache,
                             @QueryParam(MethodParams.DEBUG) @DefaultValue("false") boolean debug,
                             @QueryParam(MethodParams.SCHEME) @DefaultValue("") String scheme,
                             @QueryParam(MethodParams.STYLE) @DefaultValue("") String style,
                             @QueryParam(MethodParams.ALIAS) @DefaultValue("") String alias,
                             @Context HttpServletRequest request) {

    final String schemeToUse;
    if (!inferScheme) {
      schemeToUse = (scheme == null || scheme.length() == 0) ? request.getScheme() : scheme;
    } else {
      schemeToUse = "";
    }

    if (path == null || path.length() == 0) {
      logger.warn("No path provided.");
      return "No path provided.";
    }

    try {
      CdfRunJsDashboardWriteResult dashboard = getDashboardModule(
              path, schemeToUse, root, absolute, bypassCache, debug, style, alias);

      // TODO: i18n for a required dashboard
      // TODO: get dashboard initial context and storage from CDF plugin

      return dashboard.getContent();
    } catch (Exception ex) {
      String msg = "Could not load dashboard: " + ex.getMessage();
      logger.error(msg, ex);
      return msg;
    }
  }

  @GET
  @Path( "/getDashboardParameters" )
  @Produces(APPLICATION_JSON)
  public String getDashboardParameters(
          @QueryParam(MethodParams.PATH) @DefaultValue("") String path,
          @QueryParam(MethodParams.BYPASSCACHE) @DefaultValue("false") boolean bypassCache,
          @QueryParam(MethodParams.ALLPARAMS) @DefaultValue("false") boolean all) {

    if (path == null || path.length() == 0) {
      logger.warn("No path provided.");
      return "No path provided.";
    }

    try {
      return getDashboardManager().getDashboardParameters(path, bypassCache, all);
    } catch (Exception ex) {
      String msg = "Could not load dashboard parameters: " + ex.getMessage();
      logger.error(msg, ex);
      return msg;
    }
  }


  private CdfRunJsDashboardWriteResult getDashboardModule(String path, String scheme, String root,
                                                          boolean absolute, boolean bypassCache, boolean debug,
                                                          String style, String alias)
          throws ThingWriteException {

    final String dashboardAlias = getDashboardAlias(path, alias);

    CdfRunJsDashboardWriteOptions options =
            new CdfRunJsDashboardWriteOptions(dashboardAlias, true, absolute, debug, root, scheme);

    return getDashboardManager().getDashboardCdfRunJs(path, options, bypassCache, style);

  }

  public DashboardManager getDashboardManager() {
    return this.dashboardManager;
  }

  public void setDashboardManager(DashboardManager dashboardManager) {
    this.dashboardManager = dashboardManager;
  }

  /**
   * Returns a dashboard alias built using its filename and an alias. If no alias parameter is provided a default value
   * will be used.
   *
   * @param path  The dashboard full path.
   * @param alias A unique alias.
   * @return The dashboard alias.
   */
  private String getDashboardAlias(final String path, final String alias) {
    String dashboardAlias;
    int index = path.lastIndexOf("/");
    if (index > -1) {
      dashboardAlias = path.substring(index + 1);
    } else {
      dashboardAlias = path;
    }
    index = dashboardAlias.lastIndexOf(".");
    if (index > -1) {
      dashboardAlias = dashboardAlias.substring(0, index);
    }
    if (alias == null || alias.length() == 0) {
      dashboardAlias += "_" + CdeConstants.DASHBOARD_ALIAS_TAG;
    } else {
      dashboardAlias += "_" + alias;
    }
    return dashboardAlias;
  }

  @GET
  @Path( "/render" )
  @Produces( TEXT_HTML )
  public String render( @QueryParam( MethodParams.SOLUTION ) @DefaultValue( "" ) String solution,
                        @QueryParam( MethodParams.PATH ) @DefaultValue( "" ) String path,
                        @QueryParam( MethodParams.FILE ) @DefaultValue( "" ) String file,
                        @QueryParam( MethodParams.INFERSCHEME ) @DefaultValue( "false" ) boolean inferScheme,
                        @QueryParam( MethodParams.ROOT ) @DefaultValue( "" ) String root,
                        @QueryParam( MethodParams.ABSOLUTE ) @DefaultValue( "true" ) boolean absolute,
                        @QueryParam( MethodParams.BYPASSCACHE ) @DefaultValue( "false" ) boolean bypassCache,
                        @QueryParam( MethodParams.DEBUG ) @DefaultValue( "false" ) boolean debug,
                        @QueryParam( MethodParams.SCHEME ) @DefaultValue( "" ) String scheme,
                        @QueryParam( MethodParams.VIEW ) @DefaultValue( "" ) String view,
                        @QueryParam( MethodParams.STYLE ) @DefaultValue( "" ) String style,
                        @Context HttpServletRequest request ) {

    solution = decodeAndEscape( solution );
    path = decodeAndEscape( path );
    file = decodeAndEscape( file );
    scheme = decodeAndEscape( scheme );
    view = decodeAndEscape( view );
    style = decodeAndEscape( style );

    String schemeToUse = "";
    if ( !inferScheme ) {
      schemeToUse = StringUtils.isEmpty( scheme ) ? request.getScheme() : scheme;
    }

    String filePath = getWcdfRelativePath( solution, path, file );
    if ( StringUtils.isEmpty( filePath ) ) {
      return "No path provided.";
    }

    IReadAccess readAccess = Utils.getSystemOrUserReadAccess( filePath );
    if ( readAccess == null ) {
      return Messages.getString( "XmlStructure.ERROR_011_READ_WRITE_ACCESS_EXCEPTION" );
    }

    long start = System.currentTimeMillis();
    long end;
    /*ILogger iLogger = getAuditLogger();
    IParameterProvider requestParams = getParameterProvider( request.getParameterMap() );

    UUID uuid = CpfAuditHelper.startAudit(
            getPluginName(), filePath, getObjectName(), this.getPentahoSession(), iLogger, requestParams );*/

    try {
      logger.info( "[Timing] CDE Starting Dashboard Rendering" );
      CdfRunJsDashboardWriteResult dashboard =
              loadDashboard( filePath, schemeToUse, root, absolute, bypassCache, debug, style );

      DashboardWcdfDescriptor dashboardWcdf = DashboardWcdfDescriptor.load( filePath );
      String context = ""; /* dashboardWcdf.isRequire()
              ? getCdfRequireContext( filePath, requestParams )
              : getCdfContext( filePath, "", view, requestParams );*/

      String result = dashboard.render( context, "{\n" +
              "context: {\"path\":\"vera\",\"solution\":\"home\",\"serverLocalDate\":1531920956754,\"queryData\":{},\"roles\":[\"Administrator\",\"Authenticated\"],\"sessionTimeout\":7200,\"sessionAttributes\":{},\"locale\":\"en_US\",\"params\":{},\"serverUTCDate\":1531917356754,\"user\":\"admin\"},\n" +
              "storage: {}\n" +
              "}"/*getCdfRequireConfig( filePath, requestParams ) */);

      //i18n token replacement
      if ( !StringUtils.isEmpty( result ) && !dashboardWcdf.isRequire() ) {
        String msgDir = FilenameUtils.getPath( FilenameUtils.separatorsToUnix( filePath ) );
        msgDir = msgDir.startsWith( Util.SEPARATOR ) ? msgDir : Util.SEPARATOR + msgDir;

        result = new MessageBundlesHelper(
                msgDir,
                Utils.getAppropriateReadAccess( msgDir ),
                getEnv().getPluginSystemWriter(),
                getEnv().getLocale(),
                getEnv().getExtApi().getPluginStaticBaseUrl() )
                .replaceParameters( result, null );
      }

      logger.info( "[Timing] CDE Finished Dashboard Rendering: " + Utils.ellapsedSeconds( start ) + "s" );

      end = System.currentTimeMillis();
      /*CpfAuditHelper.endAudit(
              getPluginName(), filePath, getObjectName(), this.getPentahoSession(), iLogger, start, uuid, end );*/

      return result;
    } catch ( Exception ex ) { //TODO: better error handling?
      String msg = "Could not load dashboard: " + ex.getMessage();
      logger.error( msg, ex );

      end = System.currentTimeMillis();
      /*CpfAuditHelper.endAudit( getPluginName(), filePath, getObjectName(),
              this.getPentahoSession(), iLogger, start, uuid, end );*/

      return msg;
    }
  }

  private CdfRunJsDashboardWriteResult loadDashboard( String filePath, String scheme, String root, boolean absolute,
                                                      boolean bypassCache, boolean debug, String style )
          throws ThingWriteException {

    CdfRunJsDashboardWriteOptions options = new CdfRunJsDashboardWriteOptions(
            "", false, absolute, debug, root, scheme );

    CdfRunJsDashboardWriteResult result = getDashboardManager().getDashboardCdfRunJs( filePath, options, bypassCache, style );

    if( result != null ) {
      /**
       * TODO: find proper way to deal with this
       * The content of the dashboard is being built in cde-core, with static constants of cde-core bundle
       * For the OSGi environment we need to replace some paths and contexts
       * This is being done here until proper integrated solution is defined
       */
      logger.fatal( "processCDeDashboardForOSGiEnvironment() - This is a hammer to solve!!" );
      result = processCDeDashboardForOSGiEnvironment(result);
    }

    return result;
  }

  private CdfRunJsDashboardWriteResult processCDeDashboardForOSGiEnvironment( CdfRunJsDashboardWriteResult result ){
    if( result != null ) {
      CdfRunJsDashboardWriteResult.Builder builder = new CdfRunJsDashboardWriteResult.Builder();

      String template = result.getTemplate();
      String header = result.getHeader();
      String layout = result.getLayout();
      String components = result.getComponents();
      String content = result.getContent();
      String footer = result.getFooter();

      template = template.replace( "webcontext.js", "/@pentaho/dependencies/1.0/cde/webcontext.js");
      header = header.replace( "webcontext.js", "/@pentaho/dependencies/1.0/cde/webcontext.js");

      builder.setTemplate(template);
      builder.setHeader(header);
      builder.setLayout(layout);
      builder.setComponents(components);
      builder.setContent(content);
      builder.setFooter(footer);

      result = builder.build();
    }

    return result;
  }

  @GET
  @Path( "/edit" )
  @Produces(TEXT_HTML)
  public String edit(
          @QueryParam(MethodParams.SOLUTION) @DefaultValue("") String solution,
          @QueryParam(MethodParams.PATH) @DefaultValue("") String path,
          @QueryParam(MethodParams.FILE) @DefaultValue("") String file,
          @QueryParam(MethodParams.DEBUG) @DefaultValue("false") boolean debug,
          @QueryParam("isDefault") @DefaultValue("false") boolean isDefault,
          @Context HttpServletRequest request,
          @Context HttpServletResponse response) throws Exception {

    solution = decodeAndEscape(solution);
    path = decodeAndEscape(path);
    file = decodeAndEscape(file);

    String wcdfPath = getWcdfRelativePath(solution, path, file);

    if ( !getEnv().canCreateContent() ) {
      return "This functionality is limited to users with permission 'Create Content'";
    } else if ( Utils.getSystemOrUserRWAccess( wcdfPath ) == null ) {
      return "Access Denied or file not found - " + wcdfPath; //TODO: keep html?
    }

    DashboardWcdfDescriptor descriptor = DashboardWcdfDescriptor.load(wcdfPath);

    return getEditor(wcdfPath, debug, request.getScheme(), isDefault, response, descriptor.isRequire());
  }

  private String getEditor(String path, boolean debug, String scheme, boolean isDefault,
                           HttpServletResponse response, boolean isRequire) throws Exception {
    response.setContentType(TEXT_HTML);
    String result = DashboardEditor.getEditor(path, debug, scheme, isDefault, isRequire);

    //i18n token replacement
    if (!StringUtils.isEmpty(result)) {

      /* cde editor's i18n is different; it continues on relying on pentaho-cdf-dd/lang/messages.properties */

      String msgDir = Util.SEPARATOR + "lang" + Util.SEPARATOR;
      result = new MessageBundlesHelper(msgDir, getEnv().getPluginSystemReader(null),
              getEnv().getPluginSystemWriter(), getEnv().getLocale(),
              getEnv().getExtApi().getPluginStaticBaseUrl()).replaceParameters(result, null);
    }

    return result;
  }

  private ICdeEnvironmentExtended getEnv() {
    if (this.osgiEnvironment == null) {
      this.osgiEnvironment = (CdeEnvironment) CdeEngine.getEnv();;
    }
    return this.osgiEnvironment;
  }

  private String decodeAndEscape(String path) {
    // TODO: Import XSSHelper correctly
    return path;
    // final XSSHelper helper = XSSHelper.getInstance();

    // return helper.escape( Utils.getURLDecoded( path ) );
  }

  private String getWcdfRelativePath(String solution, String path, String file) {
    //TODO: change to use path instead of file
    //    if ( !StringUtils.isEmpty( solution ) || !StringUtils.isEmpty( file ) ) {
    //      logger.warn( "Use of solution/path/file is deprecated. Use just the path argument" );
    return Util.joinPath(solution, path, file);
    //    }
    //    else return path;
    //    final String filePath = "/" + solution + "/" + path + "/" + file;
    //    return filePath.replaceAll( "//+", "/" );
  }

  @GET
  @Path( "/getComponentDefinitions" )
  @Produces( JAVASCRIPT )
  public String getComponentDefinitions(
          @QueryParam(MethodParams.SUPPORTS) @DefaultValue(CdeConstants.DashboardSupportedTypes.LEGACY) String supports,
          @Context HttpServletResponse response) {

    // Get and output the definitions
    if (!StringUtils.isEmpty(supports) && supports.equals(CdeConstants.DashboardSupportedTypes.AMD)) {
      return MetaModelManager.getInstance().getAmdJsDefinition();
    } else {
      return MetaModelManager.getInstance().getJsDefinition();
    }
  }

  @GET
  @Path( "/listRenderers" )
  @Produces( APPLICATION_JSON )
  public String listRenderers() {
    return "{\"result\": [\""
            + DashboardWcdfDescriptor.DashboardRendererType.BLUEPRINT.getType()
            + "\",\""
            + DashboardWcdfDescriptor.DashboardRendererType.MOBILE.getType()
            + "\",\""
            + DashboardWcdfDescriptor.DashboardRendererType.BOOTSTRAP.getType()
            + "\"]}";
  }

}
