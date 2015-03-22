package com.sandeep.prototypes.person.dependency;

/**
 * <p>
 * The base exception for address service exceptions. Each extension of this exception is for a
 * specific HTTP response code.
 * </p>
 * 
 * @author Sandeep Nayak
 *
 */
public abstract class AddressServiceException extends Exception {

  private static final long serialVersionUID = -5404109512229360313L;
  private final int responseCode;

  /**
   * Base exception for address service from which all other address exceptions are to be derived.
   * 
   * @param message the message
   * @param cause the cause
   */
  protected AddressServiceException(int responseCode, String message, Throwable cause) {
    super(message, cause);
    this.responseCode = responseCode;
  }

  /**
   * <p>
   * Returns the response code which caused this exception
   * </p>
   * 
   * @return int the HTTP response code
   */
  public int getResponseCode() {
    return responseCode;
  }

  /**
   * Exception thrown when an address is not found
   * 
   * @author Sandeep Nayak
   *
   */
  public static class AddressNotFoundException extends AddressServiceException {

    private static final long serialVersionUID = 7571844258001521452L;

    protected AddressNotFoundException(String message) {
      super(404, message, null);
    }

  }

  /**
   * Exception thrown when the address service is temporarily unavailable
   * 
   * @author Sandeep Nayak
   *
   */
  public static class AddressTemporarilyUnavailableException extends AddressServiceException {

    private static final long serialVersionUID = -8635478331933988763L;

    protected AddressTemporarilyUnavailableException(String message) {
      super(503, message, null);
    }
  }

  /**
   * Exception thrown when the address service is temporarily unavailable
   * 
   * @author Sandeep Nayak
   *
   */
  public static class RequestTimeout extends AddressServiceException {

    private static final long serialVersionUID = 3780010431385934549L;

    protected RequestTimeout(String message) {
      super(408, message, null);
    }
  }

  /**
   * Exception maps to a generic IOException
   * 
   * @author Sandeep Nayak
   *
   */
  public static class IOException extends AddressServiceException {

    private static final long serialVersionUID = -5201697888200917536L;

    protected IOException(Throwable e) {
      super(500, e.getMessage(), e);
    }
  }

}
