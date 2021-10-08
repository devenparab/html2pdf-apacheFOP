package com.fop.example;

import org.apache.fop.apps.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

@SpringBootApplication
public class FopExampleApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(FopExampleApplication.class, args);
        convertToPDF();
    }

   
    public static void convertToPDF() throws IOException {
        System.out.println("### called convertToPDF() ###");
        // the XSL FO file
        File xsltFile = new File("<Your Path>\\fop-example\\\\src\\\\main\\\\resources\\xhtml2fo.xsl");
        // the XML file which provides the input
        StreamSource xmlSource = new StreamSource(new File("<Your Path>\\fop-example\\src\\main\\resources\\Sampler.html"));
        // create an instance of fop factory
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        // a user agent is needed for transformation
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        // Setup output
        OutputStream out;
        out = new java.io.FileOutputStream("<Your Path>\\fop-example\\src\\main\\resources\\generated.pdf");

        try {
            // Construct fop with desired output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            // Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            // Start XSLT transformation and FOP processing
            // That's where the XML is first transformed to XSL-FO and then
            // PDF is created
            transformer.transform(xmlSource, res);
        } catch (FOPException | TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
}
