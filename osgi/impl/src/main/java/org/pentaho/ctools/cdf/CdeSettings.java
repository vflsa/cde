package org.pentaho.ctools.cdf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pt.webdetails.cdf.dd.util.Utils;
import pt.webdetails.cpf.repository.api.IBasicFile;
import pt.webdetails.cpf.repository.api.IReadAccess;

import java.util.ArrayList;
import java.util.List;

public class CdeSettings {

  protected static Log logger = LogFactory.getLog(CdeSettings.class);

  public static enum FolderType { STATIC, REPO }

  private static CdfDDSettings settings = new CdfDDSettings();

  public static String[] getFilePickerHiddenFolderPaths( FolderType folderType ) {

    List<String> paths = new ArrayList<String>();

    // method IBasicFile[] getFilePickerHiddenFolders( folderType ) is already validating if folders exist
    IBasicFile[] files = getFilePickerHiddenFolders( folderType );

    if( files != null ) {

      for( IBasicFile file : files ) {
        paths.add( file.getFullPath() );
      }
    }

    return paths.toArray( new String[ files.length ] );
  }

  public static IBasicFile[] getFilePickerHiddenFolders( FolderType folderType ) {
    String[] paths = getSettings().getFilePickerHiddenFoldersByType( folderType );

    List<IBasicFile> files = new ArrayList<IBasicFile>();

    if( paths != null ){

      for( String path : paths ){

        IReadAccess access = Utils.getAppropriateReadAccess( path );

        if( access != null && access.fileExists( path ) && access.fetchFile( path ).isDirectory() ){
          files.add( access.fetchFile( path ) );
        } else {
          logger.error( "Discarding path '" + path + "': file does not exist or isn't a directory." );
        }
      }
    }

    return files.toArray( new IBasicFile[ files.size() ] );
  }

  public static CdfDDSettings getSettings(){
    return settings;
  }
}
