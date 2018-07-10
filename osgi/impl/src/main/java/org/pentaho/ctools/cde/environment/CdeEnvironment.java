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
package org.pentaho.ctools.cde.environment;

import java.util.Locale;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.ctools.cde.extapi.CdeApiPathProvider;
import org.pentaho.ctools.cde.plugin.resource.PluginResourceLocationManager;
import pt.webdetails.cdf.dd.IPluginResourceLocationManager;
import pt.webdetails.cdf.dd.InitializationException;
import pt.webdetails.cdf.dd.datasources.IDataSourceManager;
import pt.webdetails.cdf.dd.extapi.ICdeApiPathProvider;
import pt.webdetails.cdf.dd.extapi.IFileHandler;
import pt.webdetails.cdf.dd.model.core.writer.IThingWriterFactory;
import pt.webdetails.cdf.dd.model.inst.Dashboard;
import pt.webdetails.cdf.dd.model.inst.writer.cdfrunjs.dashboard.CdfRunJsDashboardWriteContext;
import pt.webdetails.cdf.dd.model.inst.writer.cdfrunjs.dashboard.CdfRunJsDashboardWriteOptions;
import pt.webdetails.cdf.dd.model.inst.writer.cdfrunjs.dashboard.legacy.PentahoCdfRunJsDashboardWriteContext;
import pt.webdetails.cdf.dd.util.Utils;
import pt.webdetails.cpf.PluginEnvironment;
import pt.webdetails.cpf.bean.IBeanFactory;
import pt.webdetails.cpf.context.api.IUrlProvider;
import pt.webdetails.cpf.api.IContentAccessFactoryExtended;
import pt.webdetails.cpf.repository.api.IBasicFile;
import pt.webdetails.cpf.repository.api.IRWAccess;
import pt.webdetails.cpf.repository.api.IReadAccess;
import pt.webdetails.cpf.resources.IResourceLoader;
import pt.webdetails.cpf.session.IUserSession;

public class CdeEnvironment implements ICdeEnvironmentExtended {

  private static final Log logger = LogFactory.getLog( CdeEnvironment.class );
  private static final String PLUGIN_REPOSITORY_DIR = "/public/cde";
  private static final String CDE_XML = "cde.xml";
  private static final String SYSTEM_DIR = "system";
  private static final String PLUGIN = "plugin";
  private static final String DEFAULT_PLUGIN_ID = "cde";

  private static final String TYPE_BLUEPRINT = "blueprint";
  private static final String SCHEMA_HTTP = "http";

  /* CDE Editor POC - Spike BACKLOG 24374 */
  private static final String DEFAULT_APPLICATION_ID = "/@pentaho/dependencies/1.0/";

  public static String SYSTEM_PLUGIN_EMPTY_WCDF_FILE_PATH = "/resources/empty.wcdf";

  private IPluginResourceLocationManager pluginResourceLocationManager;
  private IContentAccessFactoryExtended contentAccessFactory;
  private IDataSourceManager dataSourceManager;
  private IFileHandler fileHandler;

  public CdeEnvironment() {
    pluginResourceLocationManager = new PluginResourceLocationManager();
  }

  @Override
  public void init( IBeanFactory factory ) throws InitializationException {
    logger.info( "init() - Not implemented for the OSGi environment" );
  }

  @Override
  public Locale getLocale() {
    logger.info( "getLocale() - Not implemented for the OSGi environment, using default EN" );

    /* CDE Editor POC - Spike BACKLOG 24374 */
    return Locale.ENGLISH;
  }

  @Override
  public IResourceLoader getResourceLoader() {
    logger.info( "getResourceLoader() - Not implemented for the OSGi environment" );
    return null;
  }

  @Override
  public String getCdfIncludes( String dashboard, String type, boolean debug, boolean absolute, String absRoot,
                                String scheme ) throws Exception {

    //TODO: retrieve this information from somewhere else?
    //The list of included scripts is to replace the need of the cdf-require-js-cfg.js
    if ( type != null && type.equals( TYPE_BLUEPRINT ) && scheme != null && scheme.equals( SCHEMA_HTTP )) {
      /* CDE Editor POC - Spike BACKLOG 24374 */
      return "\t<!-- cdf-blueprint-script-includes -->\n"

              // BEGIN - brought from \Pentaho-server-ee\pentaho-server\pentaho-solutions\system\pentaho-cdf\
              // resources.properties
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/shims.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/pen-shim.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/modernizr/modernizr-2.8.3.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/jQuery/jquery.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/underscore/underscore.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/backbone/backbone.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/mustache/mustache.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/moment/moment.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/CCC/def.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/base64.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/CCC/jquery.tipsy.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/CCC/protovis.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/CCC/protovis-msie.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/CCC/tipsy.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/CCC/cdo.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/impromptu/jquery-impromptu.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/jQuery/jquery.ui.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/corner/jquery.corner.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/bgiframe/jquery.bgiframe.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/jdMenu/jquery.jdMenu.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/positionBy/jquery.positionBy.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/blockUI/jquery.blockUI.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/eventstack/jquery.eventstack.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/i18n/jquery.i18n.properties.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/jquery-ui-datepicker-i18n.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/MetaLayer/MetaLayer.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/fancybox/jquery.fancybox.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/dataTables/js/jquery.dataTables.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/uriQueryParser/jquery-queryParser.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/base/Base.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/wd.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/Dashboards.Startup.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/cdf-base.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/cccHelper.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/cggHelper.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/inputHelper.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/Dashboards.Main.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/Dashboards.Notifications.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/Dashboards.RefreshEngine.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/Dashboards.Query.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/Dashboards.Utils.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/Dashboards.Legacy.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/Dashboards.AddIns.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/Dashboards.Bookmarks.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/AddIns.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/addIns/coltypes.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/queries/coreQueries.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/queries/xmlaQueries.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/OlapUtils.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/json.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/MetaLayer/MetaLayer.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/captify/captify.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/CCC/pvc-d1.0.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/core.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/maps.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/simpleautocomplete.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/chosen/chosen.jquery.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/select2/select2.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/hynds/jquery.multiselect.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/backboneTreemodel/backbone.treemodel.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/mCustomScrollbar/jquery.mCustomScrollbar.concat.min.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/sanitizer/lib/html4.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/sanitizer/lib/uri.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/sanitizer/sanitizer.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/lib/baseevents.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/TreeFilter.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/defaults.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/Logger.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/models/Tree.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/models/SelectionTree.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/templates.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/views/scrollbar/AbstractScrollBarHandler.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/views/scrollbar/OptiScrollBarEngine.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/views/scrollbar/MCustomScrollBarEngine.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/views/scrollbar/ScrollBarFactory.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/views/Abstract.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/views/Root.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/views/Group.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/views/Item.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/controllers/Manager.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/controllers/RootCtrl.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/strategies/AbstractSelect.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/strategies/MultiSelect.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/strategies/SingleSelect.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/extensions/renderers.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/extensions/sorters.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/data-handlers/InputDataHandler.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/data-handlers/OutputDataHandler.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/TreeFilter/addIns/addIns.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/filter/js/filter.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/ccc.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/input.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/table.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/Pentaho.XAction.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/Pentaho.JPivot.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/Pentaho.Analyzer.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/VisualizationAPIComponent.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/CCC/compatVersion.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/navigation.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/components/Pentaho.Reporting.js\"></script>\n"
              // END resources.properties

              // BEGIN - brought from \Pentaho-server-ee\pentaho-server\pentaho-solutions\system\pentaho-cdf\
              // resources.bootstrap.properties
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/Bootstrap/js/bootstrap.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/respond/respond.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/html5shiv/html5shiv.js\"></script>\n"
              // END resources.bootstrap.properties

              // BEGIN - brought from \Pentaho-server-ee\pentaho-server\pentaho-solutions\system\pentaho-cdf\
              // resources.cdf.dashboards.properties
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/daterangepicker/daterangepicker.jQuery.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/sparkline/jquery.sparkline.js\"></script>\n"

              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/simile/ajax/simile-ajax-api.js\"></script>\n"

              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/simile/timeplot/timeplot-api.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/OpenMap/open_map.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/OpenMap/OpenLayers/OpenLayers.js\"></script>\n"
              + "\t<script language=\"javascript\" type=\"text/javascript\" "
              + "src=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/OpenStreetMap.js\"></script>\n"
              // END resources.cdf.dashboards.properties

              + "\t<!-- cdf-blueprint-style-includes -->\n"
              + "\t<link href=\"/@pentaho/dependencies/1.0/cdf/css/cdf-blueprint-style-includes.css\" rel=\"stylesheet\" "
              + "type=\"text/css\" />\n"
              + "\t<!-- cdf-blueprint-ie8style-includes -->\n"
              + "\t<!--[if lte IE 8]>\n"
              + "\t  <link href=\"/@pentaho/dependencies/1.0/cdf/js-legacy/lib/blueprint/ie.css\" "
              + "rel=\"stylesheet\" type=\"text/css\" />\n"
              + "\t<![endif]-->\n"
              + "\t<link href=\"/@pentaho/dependencies/1.0/cde/css/styles.css\" rel=\"stylesheet\" type=\"text/css\" />\n"
              + "\n";
    }
    return null;
  }

  @Override
  public ICdeApiPathProvider getExtApi() {
    return new CdeApiPathProvider( this.getUrlProvider() );
  }

  @Override
  public IFileHandler getFileHandler() {
    return this.fileHandler;
  }

  public void setFileHandler(IFileHandler fileHandler){
    this.fileHandler = fileHandler;
  }

  @Override
  public IUrlProvider getUrlProvider() {
    logger.fatal( "getUrlProvider() - Not implemented for the OSGi environment" );
    return null;
  }

  @Override
  public IUserSession getUserSession() {
    //TODO: implement it the right way
    return new IUserSession(){
      public String getUserName() {
        logger.fatal( "getUserSession() - getUserName() - " +
                "Not implemented for the OSGi environment" );
        return "admin";
      }
      public boolean isAdministrator() {
        logger.fatal( "getUserSession() - isAdministrator() - " +
                "Not implemented for the OSGi environment" );
        return true;
      }
      public String[] getAuthorities() {
        logger.fatal( "getUserSession() - getAuthorities() - " +
                "Not implemented for the OSGi environment" );
        return new String[0];
      }
      public Object getParameter( String key ) {
        logger.fatal( "getUserSession() - getParameter( String key ) - " +
                "Not implemented for the OSGi environment" );
        return null;
      }
      public String getStringParameter( String key ) {
        logger.fatal( "getUserSession() - getStringParameter( String key ) - " +
                "Not implemented for the OSGi environment" );
        return null;
      }
      public void setParameter( String key, Object value ) {
        logger.fatal( "getUserSession() - setParameter( String key, Object value ) - " +
                "Not implemented for the OSGi environment" );
      }
    };
  }

  @Override
  public void refresh() {
    logger.fatal( "refresh() - Not implemented for the OSGi environment" );
  }

  @Override
  public String getApplicationBaseUrl() {
    return "";
  }

  @Override
  public String getApplicationReposUrl() {
    logger.info( String.format( "getApplicationReposUrl() - using constant value '%s'.", DEFAULT_APPLICATION_ID ) );
    return DEFAULT_APPLICATION_ID;
  }

  @Override
  public IDataSourceManager getDataSourceManager() {
    return this.dataSourceManager;
  }

  public void setDataSourceManager( IDataSourceManager dataSourceManager ) {
    this.dataSourceManager = dataSourceManager;
  }

  @Override
  public IPluginResourceLocationManager getPluginResourceLocationManager() {
    return pluginResourceLocationManager;
  }

  public void setPluginResourceLocationManager( IPluginResourceLocationManager pluginResourceLocationManager ) {
    this.pluginResourceLocationManager = pluginResourceLocationManager;
  }

  @Override
  public String getPluginRepositoryDir() {
    return PLUGIN_REPOSITORY_DIR;
  }

  @Override
  public String getPluginId() {
    return DEFAULT_PLUGIN_ID; // TODO: any reason to keep supporting???
  }

  @Override
  public PluginEnvironment getPluginEnv() {
    return null;
  }

  @Override
  public String getSystemDir() {
    return SYSTEM_DIR;
  }

  @Override
  public String getApplicationBaseContentUrl() {
    return Utils.joinPath( getApplicationBaseUrl(), PLUGIN, getPluginId() ) + "/";
  }

  @Override
  public String getRepositoryBaseContentUrl() {
    return Utils.joinPath( getApplicationBaseUrl(), PLUGIN, getPluginId() ) + "/res/"; // TODO: review for osgi, deprecate ???
  }

  @Override
  public CdfRunJsDashboardWriteContext getCdfRunJsDashboardWriteContext( IThingWriterFactory factory, String indent,
                                                                         boolean bypassCacheRead, Dashboard dash,
                                                                         CdfRunJsDashboardWriteOptions options ) {
    if ( dash.getWcdf().isRequire() ) {
      return new pt.webdetails.cdf.dd.model.inst.writer.cdfrunjs.dashboard.amd.PentahoCdfRunJsDashboardWriteContext(
          factory, indent, bypassCacheRead, dash, options );
    } else {
      return new PentahoCdfRunJsDashboardWriteContext( factory, indent, bypassCacheRead, dash, options );
    }
  }

  @Override
  public CdfRunJsDashboardWriteContext getCdfRunJsDashboardWriteContext( CdfRunJsDashboardWriteContext factory,
                                                                         String indent ) {
    return new PentahoCdfRunJsDashboardWriteContext( factory, indent );
  }

  @Override
  public IBasicFile getCdeXml() {
    if ( getContentAccessFactory().getUserContentAccess( "/" )
        .fileExists( PLUGIN_REPOSITORY_DIR + "/" + CDE_XML ) ) {
      return getContentAccessFactory().getUserContentAccess( "/" )
        .fetchFile( PLUGIN_REPOSITORY_DIR + "/" + CDE_XML );
    } else if ( getContentAccessFactory().getPluginSystemReader( null ).fileExists( CDE_XML ) ) {
      return getContentAccessFactory().getPluginSystemReader( null ).fetchFile( CDE_XML );
    }
    return null;
  }

  @Override
  public boolean canCreateContent() {
    //TODO: implement in a proper way...
    return true;
  }

  @Override
  public IContentAccessFactoryExtended getContentAccessFactory() {
    return this.contentAccessFactory;
  }

  public void setContentAccessFactory( IContentAccessFactoryExtended contentAccessFactory ) {
    this.contentAccessFactory = contentAccessFactory;
  }

  @Override
  public IReadAccess getPluginSystemReader(){ return getContentAccessFactory().getPluginSystemReader( null ); }

  @Override
  public IReadAccess getPluginSystemReader( String path ){ return getContentAccessFactory().getPluginSystemReader( path ); }

  @Override
  public IReadAccess getPluginRepositoryReader(){ return getContentAccessFactory().getPluginRepositoryReader( PLUGIN_REPOSITORY_DIR ); }

  @Override
  public IRWAccess getPluginSystemWriter(){ return getContentAccessFactory().getPluginSystemWriter( null ); }
}
