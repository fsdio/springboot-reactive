package com.example.springboot_reactive.jwt;

import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtProperties {
  private String secretKey = "o2a8n5n3kr07cozqgol92a5ah1ak6clts0ryebxtv2kuupksxdbhwlkme5zg4qd2ohwnn9ax3pvcs9ceeajgn64sg85wmwj767j3haimlazet1o8oe2pc04ukdu98rc9gs9hcg556tbliqoqnzteo2upvhu4k59sv03284v6uysmqlvb50ijm1hc2mtvuols99rhpudzyv5nx8bfqdpseth6ky68u14ho4lwsqedg1393kp5t39zz0grh868mnbd";
  private long validityInMs = 10 * 60 * 1000;

  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  public long getValidityInMs() {
    return validityInMs;
  }

  public void setValidityInMs(long validityInMs) {
    this.validityInMs = validityInMs;
  }
}
