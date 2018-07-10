package org.pentaho.ctools.cde.extapi;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.ctools.cde.environment.CdeEnvironment;
import org.pentaho.ctools.cde.environment.ICdeEnvironmentExtended;
import pt.webdetails.cdf.dd.CdeConstants;
import pt.webdetails.cdf.dd.CdeEngine;
import pt.webdetails.cdf.dd.extapi.IFileHandler;
import pt.webdetails.cdf.dd.structure.DashboardStructure;

import pt.webdetails.cdf.dd.util.Utils;
import pt.webdetails.cpf.api.IFileContent;
import pt.webdetails.cpf.repository.api.IRWAccess;
import pt.webdetails.cpf.utils.CharsetHelper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;

public class FileHandler implements IFileHandler {

  protected static Log logger = LogFactory.getLog( FileHandler.class );

  private ICdeEnvironmentExtended osgiEnvironment;

  private ICdeEnvironmentExtended getEnv() {
    if (this.osgiEnvironment == null) {
      this.osgiEnvironment = (CdeEnvironment) CdeEngine.getEnv();;
    }
    return this.osgiEnvironment;
  }

  @Override
  public boolean saveDashboardAs( String path, String title, String description, String cdfdeJsText, boolean isPreview )
          throws Exception {

    // 1. Read empty wcdf file or get original wcdf file if previewing dashboard
    InputStream wcdfFile;
    if ( isPreview ) {
      String wcdfPath = path.replace( "_tmp", "" );
      wcdfFile = Utils.getSystemOrUserRWAccess( wcdfPath ).getFileInputStream( wcdfPath );
    } else {
      wcdfFile = getEnv().getPluginSystemReader().getFileInputStream(
              CdeEnvironment.SYSTEM_PLUGIN_EMPTY_WCDF_FILE_PATH );

      // [CDE-130] CDE Dash saves file with name @DASHBOARD_TITLE@
      if ( CdeConstants.DASHBOARD_TITLE_TAG.equals( title ) ) {
        title = FilenameUtils.getBaseName( path );
      }
      if ( CdeConstants.DASHBOARD_DESCRIPTION_TAG.equals( description ) ) {
        description = FilenameUtils.getBaseName( path );
      }
    }

    String wcdfContentAsString = IOUtils.toString( wcdfFile, CharsetHelper.getEncoding() );

    // 2. Fill-in wcdf file title and description
    wcdfContentAsString = wcdfContentAsString.replaceFirst( CdeConstants.DASHBOARD_TITLE_TAG,
            ( title != null ? Matcher.quoteReplacement( title ) : "" ) );
    wcdfContentAsString = wcdfContentAsString.replaceFirst( CdeConstants.DASHBOARD_DESCRIPTION_TAG,
            ( description != null ? Matcher.quoteReplacement( description ) : "" ) );

    // 3. Publish new wcdf file
    final ByteArrayInputStream bais = new ByteArrayInputStream( wcdfContentAsString.getBytes( CharsetHelper.getEncoding() ) );

    if ( isPreview ) {
      return Utils.getSystemOrUserRWAccess( path ).saveFile( path, bais );

    } else {
      final String finalPath = path;
      final String finalTitle = title;
      final String finalDescription = description;

      IFileContent file = new IFileContent() {
        public String getPath() {
          return finalPath;
        }
        public InputStream getContents() { return bais; }
        public String getTitle() { return finalTitle; }
        public String getDescription() { return finalDescription; }
        public boolean isHidden() {
          return true;
        }
        public boolean isDirectory() {
          return false;
        }
        public String getExtension() {
          return FilenameUtils.getExtension( finalPath );
        }
        public String getName() {
          return FilenameUtils.getName( finalPath );
        }
        public String getFullPath() { return FilenameUtils.getFullPath( finalPath );
        }
      };

      /*file.setPath( path );
      file.setContents( bais );
      file.setTitle( title );
      file.setDescription( description );*/

      return getEnv().getContentAccessFactory().getUserContentAccess( null ).saveFile( file );
    }
  }

  @Override
  /**
   * Implementation of the Basic CDE files creation; temporarily switches session to create folders as admin
   *
   * @param access repositoryAccessor
   * @param file name of the basic CDE file ( widget.cdfde, widget.wcdf, widget.cda, widget.xml )
   * @param content content of the basic CDE file
   * @return operation success
   */
  public boolean createBasicFileIfNotExists(final IRWAccess access, final String file, final InputStream content ) {
    if ( access == null || StringUtils.isEmpty( file ) || content == null ) {
      return false;
    }

    // skip creation if file already exists
    if ( !access.fileExists( file ) ) {

      try {
        // current user may not have necessary create permissions; this is an admin task

        logger.fatal( "createBasicFileIfNotExists() - When security is implemented, need to force System rights to create file." );
        /*SecurityHelper.getInstance().runAsSystem( new Callable<Boolean>() {
          @Override
          public Boolean call() throws Exception {
            return access.saveFile( file, content );
          }
        } );*/
        access.saveFile( file, content );

      } catch ( Exception e ) {
        logger.error( "Couldn't find or create CDE " + file + "  file", e );
        return false;
      }
    }

    return true;
  }

  @Override
  /**
   * Implementation of the Basic CDE folders creation; temporarily switches session to create folders as admin
   *
   * @param access repositoryAccessor
   * @param relativeFolderPath name of the basic CDE folder ( styles, templates, components, wigdets )
   * @param isHidden if directory should be hidden
   * @return operation success
   */
  public boolean createBasicDirIfNotExists( final IRWAccess access, final String relativeFolderPath, boolean isHidden ) {
    if ( access == null || StringUtils.isEmpty( relativeFolderPath ) ) {
      return false;
    }

    // skip creation if folder already exists
    if ( !access.fileExists( relativeFolderPath ) ) {

      try {
        // current user may not have necessary create permissions; this is an admin task

        logger.fatal( "createBasicDirIfNotExists() - When security is implemented, need to force System rights to create folder." );
        /*SecurityHelper.getInstance().runAsSystem( new Callable<Boolean>() {
          @Override
          public Boolean call() throws Exception {
            return access.createFolder( relativeFolderPath, isHidden );
          }
        } );*/
        return access.createFolder( relativeFolderPath, isHidden );

      } catch ( Exception e ) {
        logger.error( "Couldn't find or create CDE " + relativeFolderPath + "  dir", e );
        return false;
      }
    }

    return true;
  }
}
