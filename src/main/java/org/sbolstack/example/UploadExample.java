package org.sbolstack.example;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import org.sbolstack.frontend.StackException;
import org.sbolstack.frontend.StackFrontend;
import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.SBOLDocument;

public class UploadExample
{
    public static void main(String[] args) throws StackException, URISyntaxException
    {
        Random random = new Random();

        String testUri = "javaTest" + Math.abs(random.nextLong());
            
        upload(testUri);
        
        retrieve(testUri);
    }
    
    public static void upload(String name) throws StackException, URISyntaxException
    {
        StackFrontend frontend = new StackFrontend("http://localhost:9090");
        
        SBOLDocument document = new SBOLDocument();
        document.setDefaultURIprefix("http://sbolstack.org/");
        
        ComponentDefinition componentDefinition = document.createComponentDefinition(name, ComponentDefinition.DNA);
        componentDefinition.addRole(new URI("http://sbolstack.org/testcomponent"));
        
        frontend.upload(document);
        
    }

    
    public static void retrieve(String name) throws StackException, URISyntaxException
    {
        StackFrontend frontend = new StackFrontend("http://localhost:9090");
        
        ComponentDefinition componentDefinition = frontend.getComponent(new URI("http://sbolstack.org/" + name));
        
        System.out.println(componentDefinition.getRoles().toArray()[0]);
        
    }
}
