package org.skyscreamer.yoga.selector;

import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.yoga.mapper.ResultTraverser;
import org.skyscreamer.yoga.test.AbstractTraverserTest;
import org.skyscreamer.yoga.test.data.DataGenerator;
import org.skyscreamer.yoga.test.model.Artist;
import org.skyscreamer.yoga.test.model.User;
import org.springframework.core.io.ClassPathResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: corby
 * Date: 5/8/12
 */
public class PropertyResolverTest extends AbstractTraverserTest
{
    @Test
    // Populate a MapSelectorResolver with an aliased selector, and ensure it resolves correctly.
    public void testMapSelectorResolver()
    {
        Map<String,String> definedSelectors = new HashMap<String, String>();
        definedSelectors.put( "$suggestedAlbums", ":(favoriteArtists:(albums:(title,songs:(id,title))))" );
        MapSelectorResolver mapSelectorResolver = new MapSelectorResolver();
        mapSelectorResolver.setDefinedSelectors( definedSelectors );
        setAliasSelectorResolver( mapSelectorResolver );

        doTestResolver();
    }

    @Test
    // Set up a DynamicPropertyResolver with the Properties file selectorAlias.properties, and ensure it
    // resolves the selector correctly.
    public void testDynamicPropertyResolver()
    {
        DynamicPropertyResolver dynamicPropertyResolver = new DynamicPropertyResolver();
        ClassPathResource resource = new ClassPathResource( "selectorAlias.properties" );
        dynamicPropertyResolver.setPropertyFile( resource );
        setAliasSelectorResolver( dynamicPropertyResolver );

        doTestResolver();
    }

    @Test (expected=ParseSelectorException.class)
    public void testBadDynamicPropertyResolver() throws ParseSelectorException
    {
        DynamicPropertyResolver dynamicPropertyResolver = new DynamicPropertyResolver();
        ClassPathResource resource = new ClassPathResource( "garbage" );
        dynamicPropertyResolver.setPropertyFile( resource );
        dynamicPropertyResolver.resolveSelector( "$suggestedAlbums" );
    }

    private void doTestResolver()
    {
        User corby = DataGenerator.corby();
        Artist prince = DataGenerator.prince();
        corby.getFavoriteArtists().add( prince );
        prince.getAlbums().add( DataGenerator.nineteen99() );
        prince.getAlbums().add( DataGenerator.diamondsAndPearls() );

        ResultTraverser traverser = traverserNoEnrichers();
        doTraverse( corby, "$suggestedAlbums", traverser, _simpleContext );
        Assert.assertEquals( 3, _objectTree.size() );
        List<Map<String,Object>> favoriteArtists = getList( _objectTree, "favoriteArtists" );
        Assert.assertEquals( 1, favoriteArtists.size() );
        Map<String,Object> princeMap = findItem( favoriteArtists, "name", "Prince" );

        List<Map<String,Object>> albums = getList( princeMap, "albums" );
        Assert.assertEquals( 2, albums.size() );
        Map<String,Object> nineteen99Map = findItem( albums, "title", "1999" );

        List<Map<String,Object>> songs = getList( nineteen99Map, "songs" );
        Assert.assertEquals( 3, songs.size() );
        Map<String,Object> marriedMap = findItem( songs, "id", 12 );

        Assert.assertEquals( 2, marriedMap.size() );
        Assert.assertEquals( "Let's Pretend We're Married", marriedMap.get( "title" ) );
    }

}
