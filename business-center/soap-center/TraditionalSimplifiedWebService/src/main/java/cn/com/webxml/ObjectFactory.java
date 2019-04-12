
package cn.com.webxml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cn.com.webxml package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _String_QNAME = new QName("http://webxml.com.cn/", "string");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.com.webxml
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ToSimplifiedChineseResponse }
     * 
     */
    public ToSimplifiedChineseResponse createToSimplifiedChineseResponse() {
        return new ToSimplifiedChineseResponse();
    }

    /**
     * Create an instance of {@link ToSimplifiedChinese }
     * 
     */
    public ToSimplifiedChinese createToSimplifiedChinese() {
        return new ToSimplifiedChinese();
    }

    /**
     * Create an instance of {@link ToTraditionalChineseResponse }
     * 
     */
    public ToTraditionalChineseResponse createToTraditionalChineseResponse() {
        return new ToTraditionalChineseResponse();
    }

    /**
     * Create an instance of {@link ToTraditionalChinese }
     * 
     */
    public ToTraditionalChinese createToTraditionalChinese() {
        return new ToTraditionalChinese();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webxml.com.cn/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

}
