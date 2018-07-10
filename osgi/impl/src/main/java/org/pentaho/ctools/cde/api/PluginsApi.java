package org.pentaho.ctools.cde.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static pt.webdetails.cpf.utils.MimeTypes.JAVASCRIPT;

import org.pentaho.ctools.cde.plugin.CdePlugins;

@Path("plugins")
public class PluginsApi {

  @GET
  @Path("/get")
  @Produces(JAVASCRIPT)
  public String getCDEplugins() {
    CdePlugins plugins = new CdePlugins();
    return plugins.getCdePlugins();
  }
}
