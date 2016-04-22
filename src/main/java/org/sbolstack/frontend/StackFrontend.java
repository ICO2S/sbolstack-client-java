
package org.sbolstack.frontend;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.sbolstandard.core2.*;

public class StackFrontend
{
    HttpClient client;
    String backendUrl;
    
    public StackFrontend(String backendUrl)
    {
        this.backendUrl = backendUrl;
        
        client = HttpClients.createDefault();
    }
    
    
    /**
     * Return the total number of ComponentDefinition instances present in the default store.
     *
     * @return the number of ComponentDefinition instances as an integer
     * 
     * @throws StackException if there was an error communicating with the Stack
     * @throws PermissionException if read permission is not granted by the backend
     */
    public int countComponents() throws StackException
    {
        return countComponents(null);
    }

    /**
     * Return the total number of ComponentDefinition instances present in a given store.
     * 
     * @param storeName The name of the store to query
     *
     * @return the number of ComponentDefinition instances as an integer
     * 
     * @throws StackException if there was an error communicating with the stack
     * @throws StackException if the specified store name does not exist
     * @throws PermissionException if read permission is not granted by the backend
     */
    public int countComponents(String storeName) throws StackException
    {
        return getCount(backendUrl + storeUriFragment(storeName) + "/component/count");
    }

    /**
     * Return the total number of ModuleDefinition instances present in the default store.
     * 
     * @return the number of ModuleDefinition instances as an integer
     *
     * @throws StackException if there was an error communicating with the Stack
     * @throws PermissionException if read permission is not granted by the backend
     */
    public int countModules() throws StackException
    {
        return countModules(null);
    }
    
    /**
     * Return the total number of ModuleDefinition instances present in a given store.
     * 
     * @return the number of ModuleDefinition instances as an integer
     *
     * @param storeName The name of the store to query
     * 
     * @throws StackException if there was an error communicating with the stack
     * @throws StackException if the specified store name does not exist
     * @throws PermissionException if read permission is not granted by the backend
     */
    public int countModules(String storeName) throws StackException
    {
        return getCount(backendUrl + storeUriFragment(storeName) + "/module/count");
    }

    /**
     * Return the total number of Collection instances present in the default store.
     * 
     * @return the number of Collection instances as an integer
     *
     * @throws StackException if there was an error communicating with the Stack
     * @throws PermissionException if read permission is not granted by the backend
     */
    public int countCollections() throws StackException
    {
        return countCollections(null);
    }

    /**
     * Return the total number of Collection instances present in a given store.
     * 
     * @return the number of Collection instances as an integer
     *
     * @param storeName The name of the store to query
     * 
     * @throws StackException if there was an error communicating with the stack
     * @throws StackException if the specified store name does not exist
     * @throws PermissionException if read permission is not granted by the backend
     */
    public int countCollections(String storeName) throws StackException
    {
        return getCount(backendUrl + storeUriFragment(storeName) + "/collection/count");
    }

    /**
     * Return the total number of Sequence instances present in the default store.
     * 
     * @return the number of Sequence instances as an integer
     *
     * @throws StackException if there was an error communicating with the Stack
     * @throws PermissionException if read permission is not granted by the backend
     */
    public int countSequences() throws StackException
    {
        return countSequences(null);
    }

    /**
     * Return the total number of Sequence instances present in a given store.
     * 
     * @return the number of Sequence instances as an integer
     *
     * @param storeName The name of the store to query
     * 
     * @throws StackException if there was an error communicating with the stack
     * @throws StackException if the specified store name does not exist
     * @throws PermissionException if read permission is not granted by the backend
     */
    public int countSequences(String storeName) throws StackException
    {
        return getCount(backendUrl + storeUriFragment(storeName) + "/sequence/count");
    }

    /**
     * Retrieve a ComponentDefinition from a given store by URI.
     * 
     * @param storeName The name of the store to query
     * @param componentUri The URI of the component to retrieve
     * 
     * @return A libSBOLj ComponentDefinition instance corresponding to the component
     * 
     * @throws StackException if there was an error communicating with the stack
     * @throws StackException if the specified store name does not exist
     * @throws NotFoundException if the specified URI was not found
     * @throws PermissionException if read permission is not granted by the backend
     */
    public ComponentDefinition getComponent(String storeName, URI componentUri) throws StackException
    {
        String url = backendUrl + storeUriFragment(storeName) + "/component/" + encodeUri(componentUri) + "/sbol";
                
        TopLevel topLevel = getTopLevel(url, componentUri);
        
        if(! (topLevel instanceof ComponentDefinition))
            throw new StackException("Expected ComponentDefinition, found " + topLevel.getClass().getName());

        return (ComponentDefinition) topLevel;
    }

    /**
     * Retrieve a ComponentDefinition from the default store by URI.
     *
     * @param componentUri The URI of the component to retrieve
     * 
     * @return A libSBOLj ComponentDefinition instance corresponding to the component
     *
     * @throws StackException if there was an error communicating with the stack
     * @throws NotFoundException if the specified URI was not found
     * @throws PermissionException if read permission is not granted by the backend
     */
    public ComponentDefinition getComponent(URI componentUri) throws StackException
    {
        return getComponent(null, componentUri);
    }

    /**
     * Retrieve a ModuleDefinition from a given store by URI.
     * 
     * @param storeName The name of the store to query
     * @param moduleUri The URI of the module to retrieve
     * 
     * @return A libSBOLj ModuleDefinition instance corresponding to the module
     *
     * @param storeName The name of the store to query
     * 
     * @throws StackException if there was an error communicating with the stack
     * @throws StackException if the specified store name does not exist
     * @throws NotFoundException if the specified URI was not found
     * @throws PermissionException if read permission is not granted by the backend
     */
    public ModuleDefinition getModule(String storeName, URI moduleUri) throws StackException
    {
        String url = backendUrl + storeUriFragment(storeName) + "/module/" + encodeUri(moduleUri) + "/sbol";
                
        TopLevel topLevel = getTopLevel(url, moduleUri);
        
        if(! (topLevel instanceof ModuleDefinition))
            throw new StackException("Expected ModuleDefinition, found " + topLevel.getClass().getName());

        return (ModuleDefinition) topLevel;
    }

    /**
     * Retrieve a ModuleDefinition from the default store by URI.
     *
     * @param moduleUri The URI of the module to retrieve
     * 
     * @return A libSBOLj ModuleDefinition instance corresponding to the module
     *
     * @throws StackException if there was an error communicating with the stack
     * @throws NotFoundException if the specified URI was not found
     * @throws PermissionException if read permission is not granted by the backend
     */
    public ModuleDefinition getModule(URI moduleUri) throws StackException
    {
        return getModule(null, moduleUri);
    }


    /**
     * Retrieve a Collection from a given store by URI.
     * 
     * @param storeName The name of the store to query
     * @param collectionUri The URI of the collection to retrieve
     * 
     * @return A libSBOLj Collection instance corresponding to the collection
     *
     * @throws StackException if there was an error communicating with the stack
     * @throws StackException if the specified store name does not exist
     * @throws NotFoundException if the specified URI was not found
     * @throws PermissionException if read permission is not granted by the backend
     */
    public Collection getCollection(String storeName, URI collectionUri) throws StackException
    {
        String url = backendUrl + storeUriFragment(storeName) + "/collection/" + encodeUri(collectionUri) + "/sbol";
                
        TopLevel topLevel = getTopLevel(url, collectionUri);
        
        if(! (topLevel instanceof Collection))
            throw new StackException("Expected Collection, found " + topLevel.getClass().getName());

        return (Collection) topLevel;
    }

    /**
     * Retrieve a Collection from the default store by URI.
     *
     * @param collectionUri The URI of the collection to retrieve
     * 
     * @return A libSBOLj Collection instance corresponding to the collection
     *
     * @throws StackException if there was an error communicating with the stack
     * @throws NotFoundException if the specified URI was not found
     * @throws PermissionException if read permission is not granted by the backend
     */
    public Collection getCollection(URI collectionUri) throws StackException
    {
        return getCollection(null, collectionUri);
    }

    /**
     * Retrieve a Sequence from a given store by URI.
     *
     * @param storeName The name of the store to query
     * @param sequenceUri The URI of the sequence to retrieve
     *  
     * @return A libSBOLj Sequence instance corresponding to the sequence
     *
     * @throws StackException if there was an error communicating with the stack
     * @throws StackException if the specified store name does not exist
     * @throws NotFoundException if the specified URI was not found
     * @throws PermissionException if read permission is not granted by the backend
     */
    public Sequence getSequence(String storeName, URI sequenceUri) throws StackException
    {
        String url = backendUrl + storeUriFragment(storeName) + "/sequence/" + encodeUri(sequenceUri) + "/sbol";
                
        TopLevel topLevel = getTopLevel(url, sequenceUri);
        
        if(! (topLevel instanceof Collection))
            throw new StackException("Expected Sequence, found " + topLevel.getClass().getName());

        return (Sequence) topLevel;
    }

    /**
     * Retrieve a Sequence from the default store by URI.
     *
     * @param sequenceUri The URI of the sequence
     * 
     * @return A libSBOLj Sequence instance corresponding to the sequence
     *
     * @throws StackException if there was an error communicating with the stack
     * @throws NotFoundException if the specified URI was not found
     * @throws PermissionException if read permission is not granted by the backend
     */
    public Sequence getSequence(URI sequenceUri) throws StackException
    {
        return getSequence(null, sequenceUri);
    }

    /**
     * Search a given store for ComponentDefinition instances matching a ComponentDefinition template.
     *
     * @param storeName The name of the store to query
     * @param template An SBOL document containing the ComponentDefinition template to match
     * @param offset The offset of the results to begin at
     * @param limit The maximum number of results to return
     * 
     * @return An SBOL2 document with a summary of all matching components.
     *
     * @throws StackException if there was an error communicating with the stack
     * @throws StackException if the specified store name does not exist
     * @throws PermissionException if read permission is not granted by the backend
     */
    public SBOLDocument searchComponents(String storeName, SBOLDocument template, int offset, int limit) throws StackException
    {
        String url = backendUrl + storeUriFragment(storeName) + "/component/search/template";

        HttpPost request = new HttpPost(url);
        
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sbol", serializeDocument(template)));
        params.add(new BasicNameValuePair("offset", Integer.toString(offset)));
        params.add(new BasicNameValuePair("limit", Integer.toString(limit)));
        
        try
        {
            request.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse response = client.execute(request);

            checkResponseCode(response);
            
            InputStream inputStream = response.getEntity().getContent();
            
            SBOLDocument resultDocument = SBOLReader.read(inputStream);
            
            return resultDocument;
        }
        catch (Exception e)
        {
            throw new StackException(e);
        }
    }

    /**
     * Search the default store for ComponentDefinition instances matching a ComponentDefinition template.
     * 
     * @param template An SBOL document containing the ComponentDefinition template to match
     * @param offset The offset of the results to begin at
     * @param limit The maximum number of results to return
     * 
     * @return An SBOL2 document with a summary of all matching components.
     *
     * @throws StackException if there was an error communicating with the stack
     * @throws PermissionException if read permission is not granted by the backend
     */
    public SBOLDocument searchComponents(SBOLDocument template, int offset, int limit) throws StackException
    {
        return searchComponents(null, template, offset, limit);
    }


    /**
     * Return the number of ComponentDefinition instances matching a ComponentDefinition template in a given store.
     * 
     * @param storeName The name of the store to query
     * @param template An SBOL document containing the ComponentDefinition template to match
     * 
     * @return the number of matching instances as an integer
     *
     * @throws StackException if there was an error communicating with the stack
     * @throws StackException if the specified store name does not exist
     * @throws PermissionException if read permission is not granted by the backend
     */
    public int countMatchingComponents(String storeName, SBOLDocument template) throws StackException
    {
        String url = backendUrl + storeUriFragment(storeName) + "/component/count/template";

        HttpPost request = new HttpPost(url);
        
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sbol", serializeDocument(template)));
                
        try
        {
            request.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse response = client.execute(request);

            checkResponseCode(response);
            
            InputStream inputStream = response.getEntity().getContent();
            
            return Integer.parseInt(inputStreamToString(inputStream));
        }
        catch (Exception e)
        {
            throw new StackException(e);
        }
    }

    /**
     * Return the number of ComponentDefinition instances matching a ComponentDefinition template in the default store.
     * 
     * @param template An SBOL document containing the ComponentDefinition template to match
     * 
     * @return the number of matching instances as an integer
     *
     * @throws StackException if there was an error communicating with the stack
     * @throws PermissionException if read permission is not granted by the backend
     */
    public int countMatchingComponents(SBOLDocument template) throws StackException
    {
        return countMatchingComponents(null, template);
    }
    

    /**
     * Upload an SBOLDocument to the stack.
     * 
     * @param storeName The name of the store to upload to
     * @param document The document to upload
     *
     * @throws StackException if there was an error communicating with the stack
     * @throws StackException if the specified store name does not exist
     * @throws PermissionException if write permission is not granted by the backend
     */
    public void upload(String storeName, SBOLDocument document) throws StackException
    {
        String url = backendUrl + storeUriFragment(storeName);

        HttpPost request = new HttpPost(url);
                
        try
        {
            request.setEntity(new StringEntity(serializeDocument(document)));
            request.setHeader("Content-Type", "application/rdf+xml");
            
            HttpResponse response = client.execute(request);
            
            checkResponseCode(response);
        }
        catch (Exception e)
        {
            throw new StackException(e);
        }
    }

    
    /**
     * Upload an SBOLDocument to the stack.
     * 
     * @param document The document to upload
     *
     * @throws StackException if there was an error communicating with the stack
     * @throws PermissionException if write permission is not granted by the backend
     */
    public void upload(SBOLDocument document) throws StackException
    {
        upload(null, document);
    }
    

    /**
     * Create a new store.
     * 
     * @param storeName The name of the store to create
     *
     * @throws StackException if there was an error communicating with the stack
     * @throws PermissionException if create permission is not granted by the backend
     */
    public void createStore(String storeName) throws StackException
    {
        String url = backendUrl + "/store/create";

        HttpPost request = new HttpPost(url);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("storeName", storeName));
                
        try
        {
            request.setEntity(new UrlEncodedFormEntity(params));

            request.setHeader("Content-Type", "application/rdf+xml");
            
            HttpResponse response = client.execute(request);
            
            checkResponseCode(response);
        }
        catch (Exception e)
        {
            throw new StackException(e);
        }
    }
    
    
    
    
    private String serializeDocument(SBOLDocument document) throws StackException
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try
        {
            SBOLWriter.write(document,  outputStream);
            
            return outputStream.toString("UTF-8");
        }
        catch(Exception e)
        {
            throw new StackException("Error serializing SBOL document", e);
        }
        
    }

    private TopLevel getTopLevel(String url, URI topLevelUri) throws StackException
    {
        InputStream inputStream;
        
        try
        {
            inputStream = getInputStream(url);
        }
        catch (Exception e)
        {
            throw new StackException("Error connecting to stack endpoint", e);
        }
            
        SBOLDocument document;
        
        try
        {
            document = SBOLReader.read(inputStream);
        }
        catch (Exception e)
        {
            throw new StackException("Error reading SBOL", e);
        }
        
        TopLevel topLevel = document.getTopLevel(topLevelUri);
        
        if(topLevel == null)
        {
            throw new StackException("Matching top-level not found in response");
        }
        
        return topLevel;
    }

    private int getCount(String url) throws StackException
    {
        try
        {
            return Integer.parseInt(getString(url));
        }
        catch(Exception e)
        {
            throw new StackException(e);    
        }
    }
    
    private String getString(String url) throws StackException, IOException
    {
        return inputStreamToString(getInputStream(url));
    }

    private String inputStreamToString(InputStream inputStream) throws IOException
    {
        StringWriter writer = new StringWriter();

        IOUtils.copy(inputStream, writer);
        
        return writer.toString();
    }
    
    private InputStream getInputStream(String url) throws StackException, IOException
    {
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);

        checkResponseCode(response);
        
        return response.getEntity().getContent();
    }
    
    private String storeUriFragment(String storeName)
    {
        return storeName != null ? "/store/" + storeName : "";
    }

    private String encodeUri(URI uri)
    {
        try
        {
            return URLEncoder.encode(uri.toString(), "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("UTF-8 not supported?");
        }
    }
    
    private void checkResponseCode(HttpResponse response) throws StackException
    {
        int statusCode = response.getStatusLine().getStatusCode();
                
        if(statusCode >= 300)
        {
            switch(statusCode)
            {
            case 401:
                throw new PermissionException();
            
            case 404:
                throw new NotFoundException();
            
            default:
                throw new StackException();
            }
        }
    }
}

