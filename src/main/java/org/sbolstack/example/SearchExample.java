
package org.sbolstack.example;

import org.sbolstack.frontend.StackFrontend;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLWriter;
import java.net.URI;
import java.util.HashSet;

public class SearchExample
{
    public static void main(String[] args) throws Exception
    {
        StackFrontend frontend = new StackFrontend("http://localhost:9090");

        HashSet<URI> roles = new HashSet<URI>();

        SBOLDocument results = frontend.searchComponents("BO_*", roles, new HashSet<URI>(), new HashSet<URI>(), 0, 2);

        SBOLWriter.write(results, System.out);
    }
}
