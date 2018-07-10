package org.pentaho.ctools.cde.datasources.provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import pt.webdetails.cdf.dd.datasources.IDataSourceProvider;
import pt.webdetails.cdf.dd.datasources.InvalidDataSourceProviderException;

public class DataSourceProvider implements IDataSourceProvider {
  public static final String DATA_SOURCE_DEFINITION_METHOD_NAME = "listDataAccessTypes";

  String pluginId;

  private static Log logger = LogFactory.getLog( DataSourceProvider.class );

  /**
   * @param pluginId Plugin that contains Data Source definitions
   * @throws InvalidDataSourceProviderException when passed provider is null
   */
  public DataSourceProvider( String pluginId ) throws InvalidDataSourceProviderException {
    assert pluginId != null;
    this.pluginId = pluginId;
  }

  public JSONObject getDataSourceDefinitions(boolean refresh ) {
    try {
      //TODO: properly solve this dependency
      /*String dsDefinitions = InterPluginBroker
      .getDataSourceDefinitions( pluginId, null, DATA_SOURCE_DEFINITION_METHOD_NAME, refresh );*/
      logger.info( "getDataSourceDefinitions() - Not implemented for the OSGI environment, using hammer data instead." );
      String dsDefinitions = "";

      if( this.pluginId == "cda" ) {
        dsDefinitions = "{\n" +
                "\"denormalizedMdx_mondrianJdbc\": {\n" +
                "\t\"metadata\": {\n" +
                "\t\t\"name\": \"denormalizedMdx over mondrianJdbc\",\n" +
                "\t\t\"conntype\": \"mondrian.jdbc\",\n" +
                "\t\t\"datype\": \"denormalizedMdx\",\n" +
                "\t\t\"group\": \"MDX\",\n" +
                "\t\t\"groupdesc\": \"MDX Queries\"\n" +
                "\t},\n" +
                "\t\"definition\": {\n" +
                "\t\t\"connection\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"catalog\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"driver\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"url\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"user\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"pass\": {\"type\": \"STRING\", \"placement\": \"CHILD\"}\n" +
                "\t\t},\n" +
                "\t\t\"dataaccess\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"access\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"parameters\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"output\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"columns\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"query\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"connection\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cache\": {\"type\": \"BOOLEAN\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"cacheDuration\": {\"type\": \"NUMERIC\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cacheKeys\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "},\n" +
                "\"denormalizedMdx_mondrianJndi\": {\n" +
                "\t\"metadata\": {\n" +
                "\t\t\"name\": \"denormalizedMdx over mondrianJndi\",\n" +
                "\t\t\"conntype\": \"mondrian.jndi\",\n" +
                "\t\t\"datype\": \"denormalizedMdx\",\n" +
                "\t\t\"group\": \"MDX\",\n" +
                "\t\t\"groupdesc\": \"MDX Queries\"\n" +
                "\t},\n" +
                "\t\"definition\": {\n" +
                "\t\t\"connection\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"catalog\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"jndi\": {\"type\": \"STRING\", \"placement\": \"CHILD\"}\n" +
                "\t\t},\n" +
                "\t\t\"dataaccess\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"access\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"parameters\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"output\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"columns\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"query\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"connection\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cache\": {\"type\": \"BOOLEAN\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"cacheDuration\": {\"type\": \"NUMERIC\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cacheKeys\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "},\n" +
                "\"denormalizedOlap4j_olap4j\": {\n" +
                "\t\"metadata\": {\n" +
                "\t\t\"name\": \"denormalizedOlap4j over olap4j\",\n" +
                "\t\t\"conntype\": \"olap4j.defaultolap4j\",\n" +
                "\t\t\"datype\": \"denormalizedOlap4j\",\n" +
                "\t\t\"group\": \"OLAP4J\",\n" +
                "\t\t\"groupdesc\": \"OLAP4J Queries\"\n" +
                "\t},\n" +
                "\t\"definition\": {\n" +
                "\t\t\"connection\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"driver\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"url\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"role\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"property\": {\"type\": \"STRING\", \"placement\": \"CHILD\"}\n" +
                "\t\t},\n" +
                "\t\t\"dataaccess\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"access\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"parameters\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"output\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"columns\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"query\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"connection\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cache\": {\"type\": \"BOOLEAN\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"cacheDuration\": {\"type\": \"NUMERIC\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cacheKeys\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "},\n" +
                "\"join\": {\n" +
                "\t\"metadata\": {\n" +
                "\t\t\"name\": \"join\",\n" +
                "\t\t\"datype\": \"join\",\n" +
                "\t\t\"group\": \"NONE\",\n" +
                "\t\t\"groupdesc\": \"Compound Queries\"\n" +
                "\t},\n" +
                "\t\"definition\": {\n" +
                "\t\t\"dataaccess\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"parameters\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"columns\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"left\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"right\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"output\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"joinType\": {\"type\": \"STRING\", \"placement\": \"CHILD\"}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "},\n" +
                "\"kettle_kettleTransFromFile\": {\n" +
                "\t\"metadata\": {\n" +
                "\t\t\"name\": \"kettle over kettleTransFromFile\",\n" +
                "\t\t\"conntype\": \"kettle.TransFromFile\",\n" +
                "\t\t\"datype\": \"kettle\",\n" +
                "\t\t\"group\": \"KETTLE\",\n" +
                "\t\t\"groupdesc\": \"KETTLE Queries\"\n" +
                "\t},\n" +
                "\t\"definition\": {\n" +
                "\t\t\"connection\": {\n" +
                "\t\t\t\"ktrFile\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"variables\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"}\n" +
                "\t\t},\n" +
                "\t\t\"dataaccess\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"access\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"parameters\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"output\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"columns\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"query\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"connection\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cache\": {\"type\": \"BOOLEAN\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"cacheDuration\": {\"type\": \"NUMERIC\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cacheKeys\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "},\n" +
                "\"mdx_mondrianJdbc\": {\n" +
                "\t\"metadata\": {\n" +
                "\t\t\"name\": \"mdx over mondrianJdbc\",\n" +
                "\t\t\"conntype\": \"mondrian.jdbc\",\n" +
                "\t\t\"datype\": \"mdx\",\n" +
                "\t\t\"group\": \"MDX\",\n" +
                "\t\t\"groupdesc\": \"MDX Queries\"\n" +
                "\t},\n" +
                "\t\"definition\": {\n" +
                "\t\t\"connection\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"catalog\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"driver\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"url\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"user\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"pass\": {\"type\": \"STRING\", \"placement\": \"CHILD\"}\n" +
                "\t\t},\n" +
                "\t\t\"dataaccess\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"access\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"parameters\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"output\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"columns\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"query\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"connection\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cache\": {\"type\": \"BOOLEAN\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"cacheDuration\": {\"type\": \"NUMERIC\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cacheKeys\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"bandedMode\": {\"type\": \"STRING\", \"placement\": \"CHILD\"}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "},\n" +
                "\"mdx_mondrianJndi\": {\n" +
                "\t\"metadata\": {\n" +
                "\t\t\"name\": \"mdx over mondrianJndi\",\n" +
                "\t\t\"conntype\": \"mondrian.jndi\",\n" +
                "\t\t\"datype\": \"mdx\",\n" +
                "\t\t\"group\": \"MDX\",\n" +
                "\t\t\"groupdesc\": \"MDX Queries\"\n" +
                "\t},\n" +
                "\t\"definition\": {\n" +
                "\t\t\"connection\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"catalog\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"jndi\": {\"type\": \"STRING\", \"placement\": \"CHILD\"}\n" +
                "\t\t},\n" +
                "\t\t\"dataaccess\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"access\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"parameters\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"output\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"columns\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"query\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"connection\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cache\": {\"type\": \"BOOLEAN\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"cacheDuration\": {\"type\": \"NUMERIC\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cacheKeys\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"bandedMode\": {\"type\": \"STRING\", \"placement\": \"CHILD\"}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "},\n" +
                "\"mql_metadata\": {\n" +
                "\t\"metadata\": {\n" +
                "\t\t\"name\": \"mql over metadata\",\n" +
                "\t\t\"conntype\": \"metadata.metadata\",\n" +
                "\t\t\"datype\": \"mql\",\n" +
                "\t\t\"group\": \"MQL\",\n" +
                "\t\t\"groupdesc\": \"MQL Queries\"\n" +
                "\t},\n" +
                "\t\"definition\": {\n" +
                "\t\t\"connection\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"xmiFile\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"domainId\": {\"type\": \"STRING\", \"placement\": \"CHILD\"}\n" +
                "\t\t},\n" +
                "\t\t\"dataaccess\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"access\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"parameters\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"output\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"columns\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"query\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"connection\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cache\": {\"type\": \"BOOLEAN\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"cacheDuration\": {\"type\": \"NUMERIC\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cacheKeys\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "},\n" +
                "\"olap4j_olap4j\": {\n" +
                "\t\"metadata\": {\n" +
                "\t\t\"name\": \"olap4j over olap4j\",\n" +
                "\t\t\"conntype\": \"olap4j.defaultolap4j\",\n" +
                "\t\t\"datype\": \"olap4j\",\n" +
                "\t\t\"group\": \"OLAP4J\",\n" +
                "\t\t\"groupdesc\": \"OLAP4J Queries\"\n" +
                "\t},\n" +
                "\t\"definition\": {\n" +
                "\t\t\"connection\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"driver\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"url\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"role\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"property\": {\"type\": \"STRING\", \"placement\": \"CHILD\"}\n" +
                "\t\t},\n" +
                "\t\t\"dataaccess\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"access\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"parameters\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"output\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"columns\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"query\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"connection\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cache\": {\"type\": \"BOOLEAN\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"cacheDuration\": {\"type\": \"NUMERIC\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cacheKeys\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "},\n" +
                "\"scriptable_scripting\": {\n" +
                "\t\"metadata\": {\n" +
                "\t\t\"name\": \"scriptable over scripting\",\n" +
                "\t\t\"conntype\": \"scripting.scripting\",\n" +
                "\t\t\"datype\": \"scriptable\",\n" +
                "\t\t\"group\": \"SCRIPTING\",\n" +
                "\t\t\"groupdesc\": \"SCRIPTING Queries\"\n" +
                "\t},\n" +
                "\t\"definition\": {\n" +
                "\t\t\"connection\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"language\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"initscript\": {\"type\": \"STRING\", \"placement\": \"CHILD\"}\n" +
                "\t\t},\n" +
                "\t\t\"dataaccess\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"access\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"parameters\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"output\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"columns\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"query\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"connection\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cache\": {\"type\": \"BOOLEAN\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"cacheDuration\": {\"type\": \"NUMERIC\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cacheKeys\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "},\n" +
                "\"jsonScriptable_scripting\": {\n" +
                "\t\"metadata\": {\n" +
                "\t\t\"name\": \"jsonScriptable over scripting\",\n" +
                "\t\t\"conntype\": \"scripting.scripting\",\n" +
                "\t\t\"datype\": \"jsonScriptable\",\n" +
                "\t\t\"group\": \"SCRIPTING\",\n" +
                "\t\t\"groupdesc\": \"SCRIPTING Queries\"\n" +
                "\t},\n" +
                "\t\"definition\": {\n" +
                "\t\t\"connection\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"language\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"initscript\": {\"type\": \"STRING\", \"placement\": \"CHILD\"}\n" +
                "\t\t},\n" +
                "\t\t\"dataaccess\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"access\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"parameters\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"output\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"columns\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"query\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"connection\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cache\": {\"type\": \"BOOLEAN\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"cacheDuration\": {\"type\": \"NUMERIC\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cacheKeys\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "},\n" +
                "\"sql_sqlJdbc\": {\n" +
                "\t\"metadata\": {\n" +
                "\t\t\"name\": \"sql over sqlJdbc\",\n" +
                "\t\t\"conntype\": \"sql.jdbc\",\n" +
                "\t\t\"datype\": \"sql\",\n" +
                "\t\t\"group\": \"SQL\",\n" +
                "\t\t\"groupdesc\": \"SQL Queries\"\n" +
                "\t},\n" +
                "\t\"definition\": {\n" +
                "\t\t\"connection\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"driver\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"url\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"user\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"pass\": {\"type\": \"STRING\", \"placement\": \"CHILD\"}\n" +
                "\t\t},\n" +
                "\t\t\"dataaccess\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"access\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"parameters\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"output\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"columns\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"query\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"connection\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cache\": {\"type\": \"BOOLEAN\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"cacheDuration\": {\"type\": \"NUMERIC\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cacheKeys\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "},\n" +
                "\"sql_sqlJndi\": {\n" +
                "\t\"metadata\": {\n" +
                "\t\t\"name\": \"sql over sqlJndi\",\n" +
                "\t\t\"conntype\": \"sql.jndi\",\n" +
                "\t\t\"datype\": \"sql\",\n" +
                "\t\t\"group\": \"SQL\",\n" +
                "\t\t\"groupdesc\": \"SQL Queries\"\n" +
                "\t},\n" +
                "\t\"definition\": {\n" +
                "\t\t\"connection\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"jndi\": {\"type\": \"STRING\", \"placement\": \"CHILD\"}\n" +
                "\t\t},\n" +
                "\t\t\"dataaccess\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"access\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"parameters\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"output\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"columns\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"query\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"connection\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cache\": {\"type\": \"BOOLEAN\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"cacheDuration\": {\"type\": \"NUMERIC\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cacheKeys\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "},\n" +
                "\"union\": {\n" +
                "\t\"metadata\": {\n" +
                "\t\t\"name\": \"union\",\n" +
                "\t\t\"datype\": \"union\",\n" +
                "\t\t\"group\": \"NONE\",\n" +
                "\t\t\"groupdesc\": \"Compound Queries\"\n" +
                "\t},\n" +
                "\t\"definition\": {\n" +
                "\t\t\"dataaccess\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"parameters\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"columns\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"top\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"bottom\": {\"type\": \"STRING\", \"placement\": \"CHILD\"}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "},\n" +
                "\"xPath_xPath\": {\n" +
                "\t\"metadata\": {\n" +
                "\t\t\"name\": \"xPath over xPath\",\n" +
                "\t\t\"conntype\": \"xpath.xPath\",\n" +
                "\t\t\"datype\": \"xPath\",\n" +
                "\t\t\"group\": \"XPATH\",\n" +
                "\t\t\"groupdesc\": \"XPATH Queries\"\n" +
                "\t},\n" +
                "\t\"definition\": {\n" +
                "\t\t\"connection\": {\n" +
                "\t\t\t\"dataFile\": {\"type\": \"STRING\", \"placement\": \"CHILD\"}\n" +
                "\t\t},\n" +
                "\t\t\"dataaccess\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"access\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"parameters\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"output\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"columns\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"query\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"connection\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cache\": {\"type\": \"BOOLEAN\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"cacheDuration\": {\"type\": \"NUMERIC\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cacheKeys\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "},\n" +
                "\"dataservices_dataservices\": {\n" +
                "\t\"metadata\": {\n" +
                "\t\t\"name\": \"sql over dataservices\",\n" +
                "\t\t\"conntype\": \"dataservices.dataservices\",\n" +
                "\t\t\"datype\": \"dataservices\",\n" +
                "\t\t\"group\": \"DATASERVICES\",\n" +
                "\t\t\"groupdesc\": \"DATASERVICES Queries\"\n" +
                "\t},\n" +
                "\t\"definition\": {\n" +
                "\t\t\"connection\": {\n" +
                "\t\t\t\"variables\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"}\n" +
                "\t\t},\n" +
                "\t\t\"dataaccess\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"access\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"parameters\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"output\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"columns\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"connection\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cache\": {\"type\": \"BOOLEAN\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"cacheDuration\": {\"type\": \"NUMERIC\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"cacheKeys\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"dataServiceName\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"dataServiceQuery\": {\"type\": \"STRING\", \"placement\": \"CHILD\"}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "},\n" +
                "\"streaming_dataservices\": {\n" +
                "\t\"metadata\": {\n" +
                "\t\t\"name\": \"streaming over dataservices\",\n" +
                "\t\t\"conntype\": \"dataservices.dataservices\",\n" +
                "\t\t\"datype\": \"streaming\",\n" +
                "\t\t\"group\": \"DATASERVICES\",\n" +
                "\t\t\"groupdesc\": \"DATASERVICES Queries\"\n" +
                "\t},\n" +
                "\t\"definition\": {\n" +
                "\t\t\"connection\": {\n" +
                "\t\t\t\"variables\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"}\n" +
                "\t\t},\n" +
                "\t\t\"dataaccess\": {\n" +
                "\t\t\t\"id\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"access\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"parameters\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"output\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"columns\": {\"type\": \"ARRAY\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"dataServiceQuery\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"connection\": {\"type\": \"STRING\", \"placement\": \"ATTRIB\"},\n" +
                "\t\t\t\"streamingDataServiceName\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"windowMode\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"windowSize\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"windowEvery\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"windowLimit\": {\"type\": \"STRING\", \"placement\": \"CHILD\"},\n" +
                "\t\t\t\"componentRefreshPeriod\": {\"type\": \"STRING\", \"placement\": \"CHILD\"}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n" +
                "}";
      }
      return new JSONObject( dsDefinitions );
    } catch ( Exception ex ) {
      logger.error(ex.getMessage(), ex);
      return null;
    }
  }

  public String getId() {
    return pluginId;
  }

  @Override
  public String toString() {
    return String.format( "DataSourceProvider [pluginId=%s]", pluginId );
  }
}
