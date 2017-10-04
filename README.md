# elastic-rest-spring-wrapper
Thin wrapper for interacting with elasticsearch using the REST client, it has
a few methods to create indexes and also some utilities to create 

# Artifacts
Artifacts are deployed in the luminis nexus repository. This repository 
cannot act as a mirror. You can only download our own artifacts overthere.

Add this repository to your pom:
```
<repository>
    <id>luminis</id>
    <name>Snapshots</name>
    <url>https://repository.luminis.amsterdam/repository/maven-public</url>
</repository>
```

## Release
```
<dependency>
    <groupId>eu.luminis</groupId>
    <artifactId>elastic-rest-spring-wrapper</artifactId>
    <version>0.6.0</version>
</dependency>
```

## snapshot
```
<dependency>
    <groupId>eu.luminis</groupId>
    <artifactId>elastic-rest-spring-wrapper</artifactId>
    <version>0.7.0-SNAPSHOT</version>
</dependency>
```

# Using the library
```
@Import(RestClientConfig.class)
```

# Query for document by ID
To query for a document by ID we just need to provide a TypeReference that is required to obtain a typed object.

```
package nl.gridshore.recommend;

import com.fasterxml.jackson.core.type.TypeReference;
import eu.luminis.elastic.document.response.GetByIdResponse;
import nl.gridshore.kafka.JobSession;

public class JobSessionTypeReference extends TypeReference<GetByIdResponse<JobSession>> {
}
```

With this type reference we can use the *DocumentService* to obtain an object of type *JobSession*

```
QueryByIdRequest queryRequest = new QueryByIdRequest();
queryRequest.setId(jobSession.getKey());
queryRequest.setType(TYPE);
queryRequest.setIndex(INDEX);
queryRequest.setTypeReference(TYPE_REFERENCE);

try {
    JobSession currentJobSession = this.documentService.querybyId(queryRequest);
    jobSession.getIds().forEach(currentJobSession::addId);
} catch (QueryByIdNotFoundException e) {
    indexRequest.setEntity(jobSession);
}
```

# Index a document
Again we use the TypeReference as mentioned by the query

```
IndexRequest indexRequest = new IndexRequest();
indexRequest.setIndex(INDEX);
indexRequest.setType(TYPE);
indexRequest.setId(jobSession.getKey());
indexRequest.setEntity(aJobSession);
```

# Querying for documents
In the next sample code we use another index containing employees, we want to search for employees using their name 
and email address. For creating a query you can use the *twig* json templates. Such a template looks like this:
```
{
    "query":{
{% if (length(searchText)==0) %}
        "match_all": {}
{% else %}
        "multi_match": {
            "query": "{{ searchText }}",
            "operator": "{{ default(operator, 'or') }}",
            "fields": ["name","email"]
        }
{% endif %}
    }
}
```
In the template we check if you entered a search text, if not, we execute the match_all query. If you do provide a text we 
give the option to provide another operator than the default *OR* and we enter the *searchText*. With the following method
we load the template, set the parameters and we execute the service call. We do need another TypeReference for a query in 
contrast with the query by id.
```
package nl.gridshore.employees;

import com.fasterxml.jackson.core.type.TypeReference;
import eu.luminis.elastic.document.response.QueryResponse;

public class EmployeeTypeReference extends TypeReference<QueryResponse<Employee>> {
}

public List<Employee> queryForEmployeesByNameAndEmail(String searchString) {
    Map<String, Object> params = new HashMap<>();
    params.put("searchText", searchString);
    params.put("operator", "and");

    QueryByTemplateRequest request = QueryByTemplateRequest.create()
            .setAddId(true)
            .setTypeReference(new EmployeeTypeReference())
            .setIndexName(INDEX)
            .setModelParams(params)
            .setTemplateName("find_employee_by_email.twig");

    return documentService.queryByTemplate(request);
}
```

# Using aggregations
This is mainly the same as for searching for documents. You do have to configure the ObjectMapper though. To help you do this we have created a helper class _AggregationConfig_. In this class you provide a mapping from names of aggregations to the to the specific types of the aggregations. Beware that
you should not reuse aggregation names in your queries for different types.

```$java
    @Bean
    public AggregationConfig aggregationConfig() {
        AggregationConfig config = new AggregationConfig();
        config.addConfig("termsByTags", TermsAggregation.class);
        config.addConfig("histoByYear", HistogramAggregation.class);
        config.addConfig("dateHistoByCreated", DateHistogramAggregation.class);

        return config;
    }
```


# deploying an artifacts
The command to upload an artifact is:
```
mvn clean deploy -DskipTests
```

Of course this can only be done with the appropriate rights, so change your settings.xml accordingly. 

If you remove the *-SNAPHOT* from the version, it will become a release. ANd if you add it again 
it will become a snapshot again.

# References
These are some interesting not so obvious blog posts that helped me during the creation of this library:

Help with using Jackson for object deserialization
http://www.robinhowlett.com/blog/2015/03/19/custom-jackson-polymorphic-deserialization-without-type-metadata/
