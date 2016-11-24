package org.sbolstack.example;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;

import org.sbolstack.frontend.IdentifiedMetadata;
import org.sbolstack.frontend.StackException;
import org.sbolstack.frontend.StackFrontend;
import org.sbolstandard.core2.SequenceOntology;

public class CollectionsExample {

	public static void main(String[] args) throws StackException, URISyntaxException
	{
        StackFrontend frontend = new StackFrontend("http://synbiohub.org:9090");
        
        ArrayList<IdentifiedMetadata> results = frontend.fetchRootCollectionMetadata();
        
        for(IdentifiedMetadata result : results)
        {
        	System.out.println(result.name + ": " + result.uri);

            ArrayList<IdentifiedMetadata> subCollections = frontend.fetchSubCollectionMetadata(new URI(result.uri));

            for(IdentifiedMetadata subCollectionMetadata : subCollections)
            {
                System.out.println("\t" + subCollectionMetadata.name + ": " + subCollectionMetadata.uri);
            }
        
        }
	}

}
