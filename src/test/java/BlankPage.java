import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/blank/*")
public class BlankPage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.setContentLength(stringBuilder.length());
        response.getWriter().write(stringBuilder.toString());
    }
}