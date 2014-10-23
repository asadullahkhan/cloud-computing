
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;

/**
 * Servlet implementation class MPServer
 */
public class MPServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String BUCKET_NAME = "Team11Bucket";
	private RestS3Service service = new RestS3Service(new AWSCredentials(
			"CREDENTIALLLSS", "MORE SECRET CREDENTIALS"));
	private S3Bucket bucket = service.getBucket(BUCKET_NAME);

	/**
	 * Default constructor.
	 * 
	 * @throws S3ServiceException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public MPServer() throws S3ServiceException, NoSuchAlgorithmException,
			IOException {

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<HTML><HEAD><TITLE>SHOUT OUT </TITLE></HEAD><BODY>"
				+ "<font color=\"orange\"><H1> UIUC SHOUT OUTS </H1></font> <hr size=3>");
		try {
			S3Object[] list = service.listObjects(BUCKET_NAME);
			/*if (list.length == 0)
				System.out.println("\n\nempty\n\n\n");*/
			for (S3Object o : list) {
				S3Object obj = service.getObject(BUCKET_NAME, o.getKey());
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(obj.getDataInputStream()));
				String data;
				int ct =0;
				while ((data = reader.readLine()) != null) {
					//System.out.println("data: " + data);
					out.println("<H1>" + data + "</H1>");
					ct++;
				}
				if(ct!=0)
					out.println("<font color=\"green\"><H4> by " + o.getKey()
						+ "</H4></font></BODY></HTML>");
				
				//System.out.println(o.getKey());
			}
		} catch (S3ServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		try {
			S3Object obj = new S3Object(bucket, request.getParameter("name"),
					request.getParameter("shout"));
			service.putObject(bucket, obj);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (S3ServiceException e) {
			e.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		out.println(request.getParameter("name")
				+ request.getParameter("shout"));
		out.println("Success!");
		out.close();

	}

}
