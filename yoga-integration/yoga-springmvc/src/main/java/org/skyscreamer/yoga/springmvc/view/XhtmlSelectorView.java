package org.skyscreamer.yoga.springmvc.view;

import org.dom4j.Element;
import org.dom4j.dom.DOMDocument;
import org.dom4j.dom.DOMElement;
import org.skyscreamer.yoga.mapper.ResultTraverser;
import org.skyscreamer.yoga.mapper.YogaRequestContext;
import org.skyscreamer.yoga.model.HierarchicalModel;
import org.skyscreamer.yoga.model.XhtmlHierarchyModel;
import org.skyscreamer.yoga.selector.Selector;
import org.skyscreamer.yoga.util.NameUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class XhtmlSelectorView extends AbstractYogaView
{

    @Override
    public void render( OutputStream outputStream, Selector selector, Object value, HttpServletResponse response ) throws IOException
    {
        YogaRequestContext context = new YogaRequestContext( getHrefSuffix(), response );

        DOMDocument domDocument = new DOMDocument();
        Element rootElement = new DOMElement( "html" );
        domDocument.setRootElement( rootElement );
        Element cssLink = rootElement.addElement( "head" ).addElement( "link" );
        cssLink.addAttribute( "href", "/css/xhtml.css" );
        cssLink.addAttribute( "rel", "stylesheet" );
        Element body = rootElement.addElement( "body" );

        if ( value instanceof Iterable )
        {
            for ( Object child : (Iterable<?>) value )
            {
                traverse( child, selector, resultTraverser, body, context );
            }
        }
        else
        {
            traverse( value, selector, resultTraverser, body, context );
        }
        write( outputStream, domDocument );
    }

    public void traverse( Object value, Selector selector, ResultTraverser traverser, Element body, YogaRequestContext context )
    {
        String name = NameUtil.getName( _classFinderStrategy.findClass( value ) );
        HierarchicalModel model = new XhtmlHierarchyModel( body.addElement( "div" ).addAttribute( "class", name ) );
        traverser.traverse( value, selector, model, context );
    }

    @Override
    public String getContentType()
    {
        return "text/html";
    }

    @Override
    public String getHrefSuffix()
    {
        return "xhtml";
    }
}
