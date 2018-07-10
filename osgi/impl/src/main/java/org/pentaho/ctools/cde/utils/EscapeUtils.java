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

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;


import java.io.IOException;

public class EscapeUtils {
  public EscapeUtils() {
  }

  public static String escapeJson(String text) throws IOException {
    return escapeJson(text, (DefaultPrettyPrinter)null);
  }

  public static String escapeJson(String text, DefaultPrettyPrinter prettyPrinter) throws IOException {
    if (text == null) {
      return null;
    } else {
      JsonNode parsedJson = (new ObjectMapper()).readTree(text);
      return getObjectWriter(prettyPrinter).writeValueAsString(parsedJson);
    }
  }

  public static String escapeRaw(String text) {
    return escapeRaw(text, (DefaultPrettyPrinter)null);
  }

  public static String escapeRaw(String text, DefaultPrettyPrinter prettyPrinter) {
    if (text == null) {
      return null;
    } else {
      String result = null;

      try {
        String escapedValue = getObjectWriter(prettyPrinter).writeValueAsString(text);
        result = escapedValue.substring(1, escapedValue.length() - 1);
      } catch (Exception var4) {
        var4.printStackTrace();
      }

      return result;
    }
  }

  public static String escapeJsonOrRaw(String text) {
    return escapeJsonOrRaw(text, (DefaultPrettyPrinter)null);
  }

  public static String escapeJsonOrRaw(String text, DefaultPrettyPrinter prettyPrinter) {
    if (text == null) {
      return null;
    } else {
      try {
        return escapeJson(text, prettyPrinter);
      } catch (Exception var3) {
        return escapeRaw(text, prettyPrinter);
      }
    }
  }

  private static ObjectWriter getObjectWriter(DefaultPrettyPrinter prettyPrinter) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.getFactory().setCharacterEscapes(new EscapeUtils.HTMLCharacterEscapes());
    return mapper.writer(prettyPrinter);
  }

  static class HTMLCharacterEscapes extends CharacterEscapes {
    private final int[] asciiEscapes;

    public HTMLCharacterEscapes() {
      int[] esc = CharacterEscapes.standardAsciiEscapesForJSON();
      esc[60] = -1;
      esc[62] = -1;
      esc[38] = -1;
      esc[39] = -1;
      esc[34] = -1;
      this.asciiEscapes = esc;
    }

    public int[] getEscapeCodesForAscii() {
      return this.asciiEscapes;
    }

    public SerializableString getEscapeSequence(int ch) {
      return null;
    }
  }
}
