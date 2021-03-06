package org.sbolstack.example;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;

import org.sbolstack.frontend.IdentifiedMetadata;
import org.sbolstack.frontend.StackException;
import org.sbolstack.frontend.StackFrontend;
import org.sbolstandard.core2.SequenceOntology;

public class SearchMetadataExample {

	public static void main(String[] args) throws StackException
	{
        StackFrontend frontend = new StackFrontend("http://synbiohub.org:9090");
      
        HashSet<URI> roles = new HashSet<URI>();
        roles.add(SequenceOntology.PROMOTER);
        
        ArrayList<IdentifiedMetadata> results = frontend.searchComponentMetadata(null, roles, new HashSet<URI>(), new HashSet<URI>(), 0, 1000);
        
        for(IdentifiedMetadata result : results)
        {
        	System.out.println(result.name + ": " + result.uri);
        }
	}

}
