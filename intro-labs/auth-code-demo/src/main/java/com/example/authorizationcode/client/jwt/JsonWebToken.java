package com.example.authorizationcode.client.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * A simple JWT decoder.
 * This just splits the base 64 encoded string into 2 or 3 parts
 * (depending on existing signature part) and does a base 64 decoding on the first two parts.
 * There is no check of the signature included (which is a must have in productive code)
 * */
public class JsonWebToken {

  private static final ObjectMapper MAPPER = new ObjectMapper()
          .configure(SerializationFeature.INDENT_OUTPUT, true);
  private static final Base64.Decoder DECODER = Base64.getDecoder();
  private static final Logger LOGGER = LoggerFactory.getLogger(JsonWebToken.class);

  private String base64Header;
  private String base64Payload;
  private String signature;

  public JsonWebToken(String token) {
    String[] splitToken = token.split("\\.");
    if (splitToken.length == 3) {
      base64Header = splitToken[0];
      base64Payload = splitToken[1];
      signature = splitToken[2];
    } else if (splitToken.length == 2) {
      base64Header = splitToken[0];
      base64Payload = splitToken[1];
    }
  }

  public boolean isJwt() {
    return StringUtils.isNotBlank(base64Header)
        && StringUtils.isNotBlank(base64Payload)
        && StringUtils.isNotBlank(signature);
  }

  public String getHeader() {
    return new String(DECODER.decode(base64Header), UTF_8);
  }

  public String getPayload() {
    String rawJson = new String(DECODER.decode(base64Payload), UTF_8);
    try {
      return MAPPER.writerWithDefaultPrettyPrinter()
              .writeValueAsString(MAPPER.readValue(rawJson, Object.class));
    } catch (IOException e) {
      LOGGER.error("Error: {}", e.getMessage());
      return rawJson;
    }
  }

  public String getSignature() {
    return signature != null ? signature : "--";
  }
}
