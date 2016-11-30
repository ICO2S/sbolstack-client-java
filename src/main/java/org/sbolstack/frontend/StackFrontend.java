
package org.sbolstack.frontend;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.sbolstandard.core2.*;

public class StackFrontend
{
    PoolingHttpClientConnectionManager connectionManager;
    HttpClient client;
    String backendUrl;

    public StackFrontend(String backendUrl)
    {
        this.backendUrl = backendUrl;

        connectionManager = new PoolingHttpClientConnectionManager();
        client = HttpClients.custom().setConnectionManager(connectionManager).build();
    }

    public String getBackendUrl()
    {
        return this.backendUrl;
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
        return fetchCount(backendUrl + storeUriFragment(storeName) + "/component/count");
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
        return fetchCount(backendUrl + storeUriFragment(storeName) + "/module/count");
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
        return fetchCount(backendUrl + storeUriFragment(storeName) + "/collection/count");
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
        return fetchCount(backendUrl + storeUriFragment(storeName) + "/sequence/count");
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
    public ComponentDefinition fetchComponent(String storeName, URI componentUri) throws StackException
    {
        String url = backendUrl + storeUriFragment(storeName) + "/component/" + encodeUri(componentUri) + "/sbol";

        TopLevel topLevel = fetchTopLevel(url, componentUri);

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
    public ComponentDefinition fetchComponent(URI componentUri) throws StackException
    {
        return fetchComponent(null, componentUri);
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
    public ModuleDefinition fetchModule(String storeName, URI moduleUri) throws StackException
    {
        String url = backendUrl + storeUriFragment(storeName) + "/module/" + encodeUri(moduleUri) + "/sbol";

        TopLevel topLevel = fetchTopLevel(url, moduleUri);

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
    public ModuleDefinition fetchModule(URI moduleUri) throws StackException
    {
        return fetchModule(null, moduleUri);
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
    public Collection fetchCollection(String storeName, URI collectionUri) throws StackException
    {
        String url = backendUrl + storeUriFragment(storeName) + "/collection/" + encodeUri(collectionUri) + "/sbol";

        TopLevel topLevel = fetchTopLevel(url, collectionUri);

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
    public Collection fetchCollection(URI collectionUri) throws StackException
    {
        return fetchCollection(null, collectionUri);
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
    public Sequence fetchSequence(String storeName, URI sequenceUri) throws StackException
    {
        String url = backendUrl + storeUriFragment(storeName) + "/sequence/" + encodeUri(sequenceUri) + "/sbol";

        TopLevel topLevel = fetchTopLevel(url, sequenceUri);

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
    public Sequence fetchSequence(URI sequenceUri) throws StackException
    {
        return fetchSequence(null, sequenceUri);
    }

    /**
     * Search a given store for ComponentDefinition instances matching a ComponentDefinition template.
     *
     * @param storeName The name of the store to query
     * @param template An SBOL document containing the ComponentDefinition template to match
     * @param offset The offset of the results to begin at, or null to begin at 0
     * @param limit The maximum number of results to return, or null to return all results
     *
     * @return An SBOL2 document with a summary of all matching components.
     *
     * @throws StackException if there was an error communicating with the stack
     * @throws StackException if the specified store name does not exist
     * @throws PermissionException if read permission is not granted by the backend
     */
    public SBOLDocument searchComponents(String storeName, SBOLDocument template, Integer offset, Integer limit) throws StackException
    {
        String url = backendUrl + storeUriFragment(storeName) + "/component/search/template";

        HttpPost request = new HttpPost(url);

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("sbol", serializeDocument(template)));

        if(offset != null)
            params.add(new BasicNameValuePair("offset", Integer.toString(offset)));

        if(limit != null)
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
        finally
        {
            request.releaseConnection();
        }
    }


    /**
     * Search a given store for ComponentDefinition instances matching a name and/or a set of roles
     *
     * @param storeName The name of the store to query
     * @param name The dcterms:title to search for, or null
     * @param roles A set of role URIs to search for, or null
     * @param offset The offset of the results to begin at, or null to begin at 0
     * @param limit The maximum number of results to return, or null to return all results
     *
     * @return An SBOL2 document with a summary of all matching components.
     *
     * @throws StackException if there was an error communicating with the stack
     * @throws StackException if the specified store name does not exist
     * @throws PermissionException if read permission is not granted by the backend
     */
    public SBOLDocument searchComponents(String storeName, String name, Set<URI> roles, Set<URI> types, Set<URI> collections, Integer offset, Integer limit)
            throws StackException
    {
        String url = backendUrl + storeUriFragment(storeName) + "/component/search/sbol";

        SearchQuery query = new SearchQuery();

        query.offset = offset;
        query.limit = limit;

        for(URI uri : roles)
        {
            SearchCriteria roleCriteria = new SearchCriteria();

            roleCriteria.key = "role";
            roleCriteria.value = uri.toString();
        }

        for(URI uri : types)
        {
            SearchCriteria typeCriteria = new SearchCriteria();

            typeCriteria.key = "type";
            typeCriteria.value = uri.toString();
        }

        for(URI uri : collections)
        {
            SearchCriteria collectionCriteria = new SearchCriteria();

            collectionCriteria.key = "collection";
            collectionCriteria.value = uri.toString();
        }

        if(name != null)
        {
            SearchCriteria nameCriteria = new SearchCriteria();

            nameCriteria.key = "name";
            nameCriteria.value = name;
        }

        Gson gson = new Gson();

        HttpPost request = new HttpPost(url);

        try
        {
            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(gson.toJson(query)));

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
        finally
        {
            request.releaseConnection();
        }
    }

    /**
     * Search the default store for ComponentDefinition instances matching a name and/or a set of roles
     *
     * @param name The dcterms:title to search for, or null
     * @param roles A set of role URIs to search for, or null
     * @param offset The offset of the results to begin at, or null to begin at 0
     * @param limit The maximum number of results to return, or null to return all results
     *
     * @return An SBOL2 document with a summary of all matching components.
     *
     * @throws StackException if there was an error communicating with the stack
     * @throws PermissionException if read permission is not granted by the backend
     */
    public SBOLDocument searchComponents(String name, Set<URI> roles, Set<URI> types, Set<URI> collections, Integer offset, Integer limit)
            throws StackException
    {
        return searchComponents(null, name, roles, types, collections, offset, limit);
    }


    /**
     * Search the default store for ComponentDefinition instances matching a ComponentDefinition template.
     * 
     * @param template An SBOL document containing the ComponentDefinition template to match
     * @param offset The offset of the results to begin at, or null to begin at 0
     * @param limit The maximum number of results to return, or null to return all results
     * 
     * @return An SBOL2 document with a summary of all matching components.
     *
     * @throws StackException if there was an error communicating with the stack
     * @throws PermissionException if read permission is not granted by the backend
     */
    public SBOLDocument searchComponents(SBOLDocument template, Integer offset, Integer limit) throws StackException
    {
        return searchComponents(null, template, offset, limit);
    }


    /**
     * Search the default store for ComponentDefinition instances matching a name and/or a set of roles
     *
     * @param name The dcterms:title to search for, or null
     * @param roles A set of role URIs to search for, or null
     * @param offset The offset of the results to begin at, or null to begin at 0
     * @param limit The maximum number of results to return, or null to return all results
     *
     * @return An ArrayList of ComponentMetaData objects with a summary of all matching components.
     *
     * @throws StackException if there was an error communicating with the stack
     * @throws PermissionException if read permission is not granted by the backend
     */
    public ArrayList<IdentifiedMetadata> searchComponentMetadata(String name, Set<URI> roles, Set<URI> types, Set<URI> collections, Integer offset, Integer limit)
            throws StackException
    {
        return searchComponentMetadata(null, name, roles, types, collections, offset, limit);
    }

    
    /**
     * Search a given store for ComponentDefinition instances matching a name and/or a set of roles
     *
     * @param storeName The name of the store to query
     * @param name The dcterms:title to search for, or null
     * @param roles A set of role URIs to search for, or null
     * @param offset The offset of the results to begin at, or null to begin at 0
     * @param limit The maximum number of results to return, or null to return all results
     *
     * @return An ArrayList of ComponentMetaData objects with a summary of all matching components.
     *
     * @throws StackException if there was an error communicating with the stack
     * @throws StackException if the specified store name does not exist
     * @throws PermissionException if read permission is not granted by the backend
     */
    public ArrayList<IdentifiedMetadata> searchComponentMetadata(String storeName, String name, Set<URI> roles, Set<URI> types, Set<URI> collections, Integer offset, Integer limit)
            throws StackException
    {
        String url = backendUrl + storeUriFragment(storeName) + "/component/search/metadata";

        SearchQuery query = new SearchQuery();

        query.offset = offset;
        query.limit = limit;

        for(URI uri : roles)
        {
            SearchCriteria roleCriteria = new SearchCriteria();

            roleCriteria.key = "role";
            roleCriteria.value = uri.toString();
            
            query.criteria.add(roleCriteria);
        }

        for(URI uri : types)
        {
            SearchCriteria typeCriteria = new SearchCriteria();

            typeCriteria.key = "type";
            typeCriteria.value = uri.toString();
            
            query.criteria.add(typeCriteria);
        }

        for(URI uri : collections)
        {
            SearchCriteria collectionCriteria = new SearchCriteria();

            collectionCriteria.key = "collection";
            collectionCriteria.value = uri.toString();
            
            query.criteria.add(collectionCriteria);
        }

        if(name != null)
        {
            SearchCriteria nameCriteria = new SearchCriteria();

            nameCriteria.key = "name";
            nameCriteria.value = name;

            query.criteria.add(nameCriteria);
        }

        Gson gson = new Gson();

        HttpPost request = new HttpPost(url);

        try
        {
            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(gson.toJson(query)));

            HttpResponse response = client.execute(request);

            checkResponseCode(response);

            InputStream inputStream = response.getEntity().getContent();

            ArrayList<IdentifiedMetadata> metadataList = gson.fromJson(
            		new InputStreamReader(inputStream),
            			new TypeToken<ArrayList<IdentifiedMetadata>>(){}.getType());
            
            return metadataList;
        }
        catch (Exception e)
        {
            throw new StackException(e);
        }
        finally
        {
            request.releaseConnection();
        }
    }


    /**
     * 
     */
    public ArrayList<IdentifiedMetadata> searchCollectionMetadata(String storeName, String name, Integer offset, Integer limit)
            throws StackException
    {
        String url = backendUrl + storeUriFragment(storeName) + "/collection/search/metadata";

        SearchQuery query = new SearchQuery();

        query.offset = offset;
        query.limit = limit;

        if(name != null)
        {
            SearchCriteria nameCriteria = new SearchCriteria();

            nameCriteria.key = "name";
            nameCriteria.value = name;

            query.criteria.add(nameCriteria);
        }

        Gson gson = new Gson();

        HttpPost request = new HttpPost(url);

        try
        {
            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(gson.toJson(query)));

            HttpResponse response = client.execute(request);

            checkResponseCode(response);

            InputStream inputStream = response.getEntity().getContent();

            ArrayList<IdentifiedMetadata> metadataList = gson.fromJson(
            		new InputStreamReader(inputStream),
            			new TypeToken<ArrayList<IdentifiedMetadata>>(){}.getType());
            
            return metadataList;
        }
        catch (Exception e)
        {
            throw new StackException(e);
        }
        finally
        {
            request.releaseConnection();
        }
    }
    
    public ArrayList<IdentifiedMetadata> searchCollectionMetadata(String name, Integer offset, Integer limit)
            throws StackException
    {
    	return searchCollectionMetadata(null, name, offset, limit);
    }

    public ArrayList<IdentifiedMetadata> fetchRootCollectionMetadata()
            throws StackException
    {
    	return fetchRootCollectionMetadata(null);
    }

    public ArrayList<IdentifiedMetadata> fetchRootCollectionMetadata(String storeName)
            throws StackException
    {
        String url = backendUrl + storeUriFragment(storeName) + "/collection/roots";

        Gson gson = new Gson();

        HttpGet request = new HttpGet(url);

        try
        {
            HttpResponse response = client.execute(request);

            checkResponseCode(response);

            InputStream inputStream = response.getEntity().getContent();

            ArrayList<IdentifiedMetadata> metadataList = gson.fromJson(
            		new InputStreamReader(inputStream),
            			new TypeToken<ArrayList<IdentifiedMetadata>>(){}.getType());
            
            return metadataList;
        }
        catch (Exception e)
        {
            throw new StackException(e);
        }
        finally
        {
            request.releaseConnection();
        }
    }

    public ArrayList<IdentifiedMetadata> fetchSubCollectionMetadata(URI parentCollectionUri)
            throws StackException
    {
    	return fetchSubCollectionMetadata(null, parentCollectionUri);
    }

    public ArrayList<IdentifiedMetadata> fetchSubCollectionMetadata(String storeName, URI parentCollectionUri)
            throws StackException
    {
        String url = backendUrl + storeUriFragment(storeName) + "/collection/" + encodeUri(parentCollectionUri) + "/subCollections";

        Gson gson = new Gson();

        HttpGet request = new HttpGet(url);

        try
        {
            HttpResponse response = client.execute(request);

            checkResponseCode(response);

            InputStream inputStream = response.getEntity().getContent();

            ArrayList<IdentifiedMetadata> metadataList = gson.fromJson(
            		new InputStreamReader(inputStream),
            			new TypeToken<ArrayList<IdentifiedMetadata>>(){}.getType());
            
            return metadataList;
        }
        catch (Exception e)
        {
            throw new StackException(e);
        }
        finally
        {
            request.releaseConnection();
        }
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
        String url = backendUrl + storeUriFragment(storeName) + "/component/search/template";

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
        finally
        {
            request.releaseConnection();
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
        finally
        {
            request.releaseConnection();
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
        finally
        {
            request.releaseConnection();
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

    private TopLevel fetchTopLevel(String url, URI topLevelUri) throws StackException
    {
        InputStream inputStream;
        
        try
        {
            inputStream = fetchContentAsInputStream(url);
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

    private int fetchCount(String url) throws StackException
    {
        try
        {
            return Integer.parseInt(fetchContentAsString(url));
        }
        catch(Exception e)
        {
            throw new StackException(e);    
        }
    }
    
    private String fetchContentAsString(String url) throws StackException, IOException
    {
        return inputStreamToString(fetchContentAsInputStream(url));
    }

    private String inputStreamToString(InputStream inputStream) throws IOException
    {
        StringWriter writer = new StringWriter();

        IOUtils.copy(inputStream, writer);
        
        return writer.toString();
    }
    
    private InputStream fetchContentAsInputStream(String url) throws StackException, IOException
    {
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);

        checkResponseCode(response);
        
        InputStream res = response.getEntity().getContent();

        request.releaseConnection();

        return res;
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

