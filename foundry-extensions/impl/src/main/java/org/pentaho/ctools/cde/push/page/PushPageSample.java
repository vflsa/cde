package org.pentaho.ctools.cde.push.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.core.Context;

import static javax.ws.rs.core.MediaType.TEXT_HTML;

@Path( "pentaho-cdf-dd/api/push/page" )
public class PushPageSample {

  @GET
  @Path( "/getSamplePage" )
  @Produces( TEXT_HTML )
  public String getWebSocketTestPage( @QueryParam( "port" ) @DefaultValue( "8080" ) String port,
                                     @Context HttpServletRequest request,
                                     @Context HttpServletResponse response ) throws Exception {

    response.setContentType( TEXT_HTML );

    //TODO: get it from somewhere?
    String host = "localhost";
    String sampleEndpoint = "pentaho/pentaho-cdf-dd/websocket/sample";

    StringBuilder page = new StringBuilder();
    page.append( "<!DOCTYPE html>\n"
      + "<html>\n"
      + "<head>\n"
      + "<meta charset=\"UTF-8\">\n"
      + "<title>Tomcat WebSocket</title>\n"
      + "</head>\n"
      + "<body>\n"
      + "\t<form>\n"
      + "\t\t<input id=\"message\" type=\"text\">\n"
      + "\t\t<input onclick=\"wsSendMessage();\" value=\"Echo\" type=\"button\">\n"
      + "\t\t<input onclick=\"wsCloseConnection();\" value=\"Disconnect\" type=\"button\">\n"
      + "\t</form>\n"
      + "\t<br>\n"
      + "\t<textarea id=\"echoText\" rows=\"5\" cols=\"30\"></textarea>\n"
      + "\t<script type=\"text/javascript\">\n"
      + "\t\tvar webSocket = new WebSocket(\"ws://" );
    page.append( host );
    page.append( ":" );
    page.append( port );
    page.append( "/" );
    page.append( sampleEndpoint );
    page.append( "\");\n"
      + "\t\tvar echoText = document.getElementById(\"echoText\");\n"
      + "\t\techoText.value = \"\";\n"
      + "\t\tvar message = document.getElementById(\"message\");\n"
      + "\t\twebSocket.onopen = function(message){ wsOpen(message);};\n"
      + "\t\twebSocket.onmessage = function(message){ wsGetMessage(message);};\n"
      + "\t\twebSocket.onclose = function(message){ wsClose(message);};\n"
      + "\t\twebSocket.onerror = function(message){ wsError(message);};\n"
      + "\t\tfunction wsOpen(message){\n"
      + "\t\t\techoText.value += \"Connected ... \\n\";\n"
      + "\t\t}\n"
      + "\t\tfunction wsSendMessage(){\n"
      + "\t\t\twebSocket.send(message.value);\n"
      + "\t\t\techoText.value += \"Message sent to the server : \" + message.value + \"\\n\";\n"
      + "\t\t\tmessage.value = \"\";\n"
      + "\t\t}\n"
      + "\t\tfunction wsCloseConnection(){\n"
      + "\t\t\twebSocket.close();\n"
      + "\t\t}\n"
      + "\t\tfunction wsGetMessage(message){\n"
      + "\t\t\techoText.value += \"Message received from the server : \" + message.data + \"\\n\";\n"
      + "\t\t}\n"
      + "\t\tfunction wsClose(message){\n"
      + "\t\t\techoText.value += \"Disconnect ... \\n\";\n"
      + "\t\t}\n"
      + "\n"
      + "\t\tfunction wsError(message){\n"
      + "\t\t\techoText.value += \"Error ... \" + message.data + \"\\n\";\n"
      + "\t\t}\n"
      + "\t</script>\n"
      + "</body>\n"
      + "</html>" );

    return page.toString();
  }
}
