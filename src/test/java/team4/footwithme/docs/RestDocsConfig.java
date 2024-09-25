package team4.footwithme.docs;

import org.springframework.boot.test.context.TestConfiguration;

import static org.springframework.restdocs.snippet.Attributes.Attribute;

@TestConfiguration
public class RestDocsConfig {

    public static final Attribute field(
        final String key,
        final String value) {
        return new Attribute(key, value);
    }
}
