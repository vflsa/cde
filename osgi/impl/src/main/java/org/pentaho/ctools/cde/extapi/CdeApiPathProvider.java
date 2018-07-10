package org.pentaho.ctools.cde.extapi;

import pt.webdetails.cdf.dd.extapi.ICdeApiPathProvider;
import pt.webdetails.cpf.Util;
import pt.webdetails.cpf.context.api.IUrlProvider;

public class CdeApiPathProvider implements ICdeApiPathProvider {

    private IUrlProvider urlProvider;

    public CdeApiPathProvider( IUrlProvider urlProvider ) {
        this.urlProvider = urlProvider;
    }

    @Override
    public String getRendererBasePath() {
      String pluginBaseUrl = urlProvider != null ? urlProvider.getPluginBaseUrl() : "";
      return Util.joinPath( pluginBaseUrl, "renderer" );
    }

    @Override
    public String getPluginStaticBaseUrl() {
      if ( urlProvider != null ) {
        return urlProvider.getPluginStaticBaseUrl();
      } else {
        return "cde";
      }
    }

    @Override
    public String getResourcesBasePath() {
      String pluginBaseUrl = urlProvider != null ? urlProvider.getPluginBaseUrl() : "";
      return Util.joinPath( pluginBaseUrl, "resources" );
    }

}
