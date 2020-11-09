package com.qxkj.generator.mvc;


/**
 * Created by AQIANG
 */
@SuppressWarnings("checkstyle:all")
public final class ResponseUtil {
  private ResponseUtil() {
  }

  /**
   * @return ResponseBean
   */
  public static ResponseBean responseSuccess() {
    return new ResponseBean(200, "success", null);
  }

  /**
   *
   * @param t
   * @param <T>
   * @return ResponseBean
   */
  public static <T> ResponseBean responseSuccess(T t) {
    return new ResponseBean(200, "success", t);
  }

  /**
   *
   * @param msg
   * @param t
   * @param <T>
   * @return ResponseBean
   */
  public static <T> ResponseBean responseSuccess(String msg, T t) {
    return new ResponseBean(200, msg, t);
  }

  /**
   * @return ResponseBean
   */
  public static ResponseBean responseServerException() {
    return new ResponseBean(500, "server exception", null);
  }

  /**
   * @param msg msg
   * @return ResponseBean
   */
  public static ResponseBean responseServerException(String msg) {
    return new ResponseBean(500, msg, null);
  }

  /**
   * @return ResponseBean
   */
  public static ResponseBean responseArgumentException() {
    return new ResponseBean(400, "param error", null);
  }

  /**
   * @param msg msg
   * @return ResponseBean
   */
  public static ResponseBean responseArgumentException(String msg) {
    return new ResponseBean(500, msg, null);
  }

  /**
   *
   * @param code code
   * @param msg msg
   * @return ResponseBean
   */
  public static ResponseBean responseException(int code, String msg) {
    return new ResponseBean(code, msg, null);
  }

  /**
   * @param msg
   * @return
   */
  public static ResponseBean buildErrorResponse(String msg) {
    return new ResponseBean(500, msg);
  }
}
