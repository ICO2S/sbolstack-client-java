
package org.sbolstack.example;

import org.sbolstack.frontend.*;
import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.SBOLDocument;

public class CountMatchesExample
{
    public static void main(String[] args) throws StackException
    {
        StackFrontend frontend = new StackFrontend("http://localhost:9090");
        
        SBOLDocument template = new SBOLDocument();
        template.setDefaultURIprefix("http://www.bacillondex.org/");
        
        template.createComponentDefinition("BO_10050", ComponentDefinition.PROTEIN);

        System.out.println(frontend.countMatchingComponents(template));
    }

}
