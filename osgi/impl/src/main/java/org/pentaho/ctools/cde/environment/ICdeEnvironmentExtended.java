package org.pentaho.ctools.cde.environment;

import pt.webdetails.cdf.dd.ICdeEnvironment;
import pt.webdetails.cpf.api.IContentAccessFactoryExtended;
import pt.webdetails.cpf.repository.api.IRWAccess;
import pt.webdetails.cpf.repository.api.IReadAccess;

public interface ICdeEnvironmentExtended extends ICdeEnvironment {

  IReadAccess getPluginSystemReader();

  IReadAccess getPluginSystemReader( String path );

  IRWAccess getPluginSystemWriter();

  IReadAccess getPluginRepositoryReader();

  public IContentAccessFactoryExtended getContentAccessFactory();
}
