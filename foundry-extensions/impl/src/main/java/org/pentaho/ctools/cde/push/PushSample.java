package org.pentaho.ctools.cde.push;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.websocket.*;

import org.pentaho.platform.api.websocket.IWebsocketEndpoint;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

/**
 * Websocket implementation for tests on Foundry
 * This Endpoint is registered in plugin.spring.xml file
 * by means of the {@link org.pentaho.platform.web.websocket.WebsocketEndpointConfig} bean.
 *
 * Depends on BACKLOG-25403 work on the platform
 *
 * It sends random number messages to its clients, with random intervals
 */
public class PushSample extends Endpoint  implements IWebsocketEndpoint, MessageHandler.Whole<String>{

  private static final Log logger = LogFactory.getLog( PushSample.class );
  private static final String ACCEPTED_SUB_PROTOCOL = "CDE-sample";

  private static Queue<Session> queue = new ConcurrentLinkedQueue<Session>();
  private static Thread sampleThread;

  static {
    sampleThread = new Thread() {
      public void run () {
        while ( true ) {
          Double d = Math.random();
          int waitForMillis = ( new Random().nextInt( 100 ) + 1 ) * 100;
          if ( queue != null && queue.size() > 0 ) {
            sendToAllOpenSessions( "Current magic number is: " + d.toString() + ". Going to guess another one soon, beware...");
          }
          try {
            sleep( waitForMillis );
          } catch ( InterruptedException e ) {
          }
        }
      };
    };
    sampleThread.start();
  }

  private static void sendToAllOpenSessions( String msg) {
    try {
      ArrayList<Session> closedSessions = new ArrayList<>();
      for ( Session session : queue ) {
        if ( !session.isOpen() ) {
          closedSessions.add( session );
        } else {
          session.getBasicRemote().sendText( msg );
        }
      }
      queue.removeAll( closedSessions );
    } catch ( Throwable ex ) {
      logger.fatal( ex );
    }
  }


  @Override
  public void onOpen( Session session, EndpointConfig endpointConfig ) {
    logger.info( "Opening connection in session id " + session.getId() );

    //Start sending sample information to the client
    queue.add( session );

    //To allow receive messages from the client
    session.addMessageHandler( this );
  }

  @Override
  public void onOpen( Consumer<String> outboundMessageConsumer ) {
    logger.info( "onOpen - implementing IWebsocketEndpoint" );
  }


  @Override
  public void onClose( Session session, CloseReason closeReason ) {
    logger.info( "Closing connection in session id " + session.getId() + "... " + closeReason.getReasonPhrase() );
    queue.remove( session );
    super.onClose( session, closeReason );
  }

  @Override
  public void onClose() {
    logger.info( "onClose - implementing IWebsocketEndpoint" );
  }

  @Override
  public void onError( Session session, Throwable throwable ) {
    if ( logger.isDebugEnabled() ) {
      logger.debug( "Error occurred in session id " + session.getId(), throwable );
    }
    queue.remove( session );
    super.onError( session, throwable );
  }

  @Override
  public void onMessage( String message, Consumer<String> outboundMessageConsumer ) {
    logger.info( "onMessage - implementing IWebsocketEndpoint" );
  }

  @Override
  public void onMessage( String msg ) {
    logger.info( "Implementing MessageHandler.Whole<String> to be able to receive messages from clients" );
    logger.info( "Received message from client: " + msg);
    sendToAllOpenSessions( "Someone just sent me this: " + msg + ". Just sharing..." );
  }

  @Override
  public List<String> getSubProtocols() {
    return Collections.singletonList( ACCEPTED_SUB_PROTOCOL );
  }

}
