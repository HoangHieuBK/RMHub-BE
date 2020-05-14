package rmhub.common.util;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

public class PathUtil {

  /**
   * <p>
   * Returns the part of this request's URL from the protocol name up to the query string.
   * </p>
   * For example:
   * <table summary="Examples of Returned Values" border="1" cellspacing="0" cellpadding="5">
   * <tr align=left>
   * <th>First line of HTTP request</th>
   * <th>Returned Value</th>
   * </tr>
   * <tr align=left>
   * <td>POST /books/kim-binh-mai HTTP/1.1</td>
   * <td>/books/kim-binh-mai</td>
   * </tr>
   * <tr align=left>
   * <td>GET http://rmhub.autsoft.hu/weather?km=123 HTTP/1.0</td>
   * <td>/weather</td>
   * </tr>
   * </table>
   *
   * @see HttpServletRequest#getRequestURI
   */
  public static String getPath(WebRequest request) {
    return ((ServletWebRequest) request).getRequest().getRequestURI();
  }
}
