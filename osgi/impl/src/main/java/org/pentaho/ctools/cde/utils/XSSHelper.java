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
package org.pentaho.ctools.cde.utils;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import pt.webdetails.cdf.dd.CdeConstants;
import pt.webdetails.cdf.dd.CdeEngine;
import pt.webdetails.cdf.dd.util.CorsUtil;
import pt.webdetails.cpf.resources.IResourceLoader;

import org.apache.commons.lang.StringUtils;

public class XSSHelper {
  private static XSSHelper instance = new XSSHelper();

  private static DefaultPrettyPrinter prettyPrinter;

  public static XSSHelper getInstance() {
    return instance;
  }

  public String escape( final String userInput ) {
    final boolean isInputEmpty = StringUtils.isEmpty( userInput );
    final boolean isXssEscapeDisabled = "false".equals( getXssEscapingPluginSetting() );

    if ( isInputEmpty || isXssEscapeDisabled ) {
      return userInput;
    }

    return EscapeUtils.escapeJsonOrRaw( userInput, getPrettyPrinter() );
  }

  private DefaultPrettyPrinter getPrettyPrinter() {
    if ( prettyPrinter != null ) {
      return prettyPrinter;
    }

    final DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
    final DefaultIndenter indent = new DefaultIndenter( CdeConstants.Writer.INDENT1, CdeConstants.Writer.NEWLINE );

    prettyPrinter = printer
            .withObjectIndenter( indent )
            .withArrayIndenter( indent );

    return prettyPrinter;
  }

  static void setInstance( final XSSHelper newInstance ) {
    instance = newInstance;
  }

  private String getXssEscapingPluginSetting() {
    IResourceLoader cdeResourceLoader = CdeEngine.getInstance().getEnvironment().getResourceLoader();

    if ( cdeResourceLoader != null ) {
      return cdeResourceLoader.getPluginSetting(CorsUtil.class, CdeConstants.PARAMETER_XSS_ESCAPING);
    } else {
      return null;
    }
  }
}
