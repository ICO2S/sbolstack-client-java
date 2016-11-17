
package org.sbolstack.example;

import org.sbolstack.frontend.*;
import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLValidationException;

public class CountMatchesExample
{
    public static void main(String[] args) throws StackException, SBOLValidationException
    {
        StackFrontend frontend = new StackFrontend("http://synbiohub.org:9090");
        
        SBOLDocument template = new SBOLDocument();
        template.setDefaultURIprefix("http://www.bacillondex.org/");
        
        template.createComponentDefinition("BO_10050", ComponentDefinition.PROTEIN);

        System.out.println(frontend.countMatchingComponents(template));
    }

}
