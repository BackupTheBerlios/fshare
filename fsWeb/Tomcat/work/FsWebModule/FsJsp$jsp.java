package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.apache.jasper.runtime.*;


public class FsJsp$jsp extends HttpJspBase {

    // begin [file="/FsJsp.jsp";from=(6,0);to=(6,77)]
    // end

    static {
    }
    public FsJsp$jsp( ) {
    }

    private static boolean _jspx_inited = false;

    public final void _jspx_init() throws org.apache.jasper.runtime.JspException {
    }

    public void _jspService(HttpServletRequest request, HttpServletResponse  response)
        throws java.io.IOException, ServletException {

        JspFactory _jspxFactory = null;
        PageContext pageContext = null;
        HttpSession session = null;
        ServletContext application = null;
        ServletConfig config = null;
        JspWriter out = null;
        Object page = this;
        String  _value = null;
        try {

            if (_jspx_inited == false) {
                synchronized (this) {
                    if (_jspx_inited == false) {
                        _jspx_init();
                        _jspx_inited = true;
                    }
                }
            }
            _jspxFactory = JspFactory.getDefaultFactory();
            response.setContentType("text/html;ISO-8859-1");
            pageContext = _jspxFactory.getPageContext(this, request, response,
            			"", true, 8192, true);

            application = pageContext.getServletContext();
            config = pageContext.getServletConfig();
            session = pageContext.getSession();
            out = pageContext.getOut();

            // HTML // begin [file="/FsJsp.jsp";from=(0,0);to=(6,0)]
                out.write("<html>\r\n<head>\r\n<title>\r\nFsJsp\r\n</title>\r\n</head>\r\n");

            // end
            // begin [file="/FsJsp.jsp";from=(6,0);to=(6,77)]
                fsfrontend.FsJspBean fsJspBeanId = null;
                boolean _jspx_specialfsJspBeanId  = false;
                 synchronized (session) {
                    fsJspBeanId= (fsfrontend.FsJspBean)
                    pageContext.getAttribute("fsJspBeanId",PageContext.SESSION_SCOPE);
                    if ( fsJspBeanId == null ) {
                        _jspx_specialfsJspBeanId = true;
                        try {
                            fsJspBeanId = (fsfrontend.FsJspBean) java.beans.Beans.instantiate(this.getClass().getClassLoader(), "fsfrontend.FsJspBean");
                        } catch (ClassNotFoundException exc) {
                             throw new InstantiationException(exc.getMessage());
                        } catch (Exception exc) {
                             throw new ServletException (" Cannot create bean of class "+"fsfrontend.FsJspBean", exc);
                        }
                        pageContext.setAttribute("fsJspBeanId", fsJspBeanId, PageContext.SESSION_SCOPE);
                    }
                 } 
                if(_jspx_specialfsJspBeanId == true) {
            // end
            // begin [file="/FsJsp.jsp";from=(6,0);to=(6,77)]
                }
            // end
            // HTML // begin [file="/FsJsp.jsp";from=(6,77);to=(7,0)]
                out.write("\r\n");

            // end
            // begin [file="/FsJsp.jsp";from=(7,0);to=(7,51)]
                JspRuntimeLibrary.introspect(pageContext.findAttribute("fsJspBeanId"), request);
            // end
            // HTML // begin [file="/FsJsp.jsp";from=(7,51);to=(10,0)]
                out.write("\r\n<body bgcolor=\"#ffffff\">\r\n\r\n");

            // end
            // begin [file="/FsJsp.jsp";from=(10,2);to=(13,0)]
                
                	String cli = (String) request.getAttribute("html");
                  out.println(cli);
            // end
            // HTML // begin [file="/FsJsp.jsp";from=(13,2);to=(18,0)]
                out.write("\r\n\r\n\r\n</body>\r\n</html>\r\n");

            // end

        } catch (Throwable t) {
            if (out != null && out.getBufferSize() != 0)
                out.clearBuffer();
            if (pageContext != null) pageContext.handlePageException(t);
        } finally {
            if (_jspxFactory != null) _jspxFactory.releasePageContext(pageContext);
        }
    }
}
