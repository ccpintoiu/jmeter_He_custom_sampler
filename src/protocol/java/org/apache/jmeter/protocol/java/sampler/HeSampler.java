/* Written By Alexandru Bordei
* Adapted by Cosmin Pintoiu
*  Bigstep.com
*  Distrubuted under the same license as apache jmeter itself.
* http://www.apache.org/licenses/LICENSE-2.0
*/
package com.bigstep;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.File;
import java.io.Serializable;

import com.bigstep.Helium;

public class HeSampler extends AbstractJavaSamplerClient implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggingManager.getLoggerForClass();

    // set up default arguments for the JMeter GUI
    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = new Arguments();
        
        defaultParameters.addArgument("datastore", "");
        defaultParameters.addArgument("method", "GET");
        defaultParameters.addArgument("bucket", "");
        defaultParameters.addArgument("object", "");
        defaultParameters.addArgument("key_id", "");   
        return defaultParameters;
    }

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        // pull parameters
        String datastore = context.getParameter("datastore");
        String bucket = context.getParameter("bucket");
        String object = context.getParameter("object");
        String method = context.getParameter("method");
        String key_id = context.getParameter("key_id");

        log.debug("runTest:method=" + method +  " object=" + object);

        SampleResult result = new SampleResult();
        result.sampleStart(); // start stopwatch

        try {
            if (method.equals("GET")) {
               // File file = new File(local_file_path);
                Helium he = new Helium();
                he.open(datastore);
              //insert if not exists
        		if(!he.exists(key_id))
        			bucket = he.lookup(key_id);
                he.close();
            } else if (method.equals("PUT")) {
            	Helium he = new Helium();
                he.open(datastore);
            	he.insert(key_id,object);
            	he.close();
            }

            result.sampleEnd(); // stop stopwatch
            result.setSuccessful(true);
            result.setResponseCodeOK(); // 200 code

        } catch (Exception e) {
            result.sampleEnd(); // stop stopwatch
            result.setSuccessful(false);
            result.setResponseMessage("Exception: " + e);

            // get stack trace as a String to return as document data
            java.io.StringWriter stringWriter = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(stringWriter));
            result.setResponseData(stringWriter.toString());
            result.setDataType(org.apache.jmeter.samplers.SampleResult.TEXT);
            result.setResponseCode("500");
        }

        return result;
    }
}