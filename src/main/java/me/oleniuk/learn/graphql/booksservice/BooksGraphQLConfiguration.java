package me.oleniuk.learn.graphql.booksservice;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;
import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Configuration
public class BooksGraphQLConfiguration {

    @Autowired
    private GraphQLDataFetchers graphQLDataFetchers;

    @Bean
    public GraphQL booksGraphQL() {
        GraphQLSchema graphQLSchema = buildSchema(BooksGraphQLConfiguration.class.getResourceAsStream("/schema.graphqls"));
        return GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema(InputStream sdl) {
        //the parsed version of our schema file
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        //combines the TypeDefinitionRegistry with RuntimeWiring to actually make the GraphQLSchema
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        return newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher())
                        .dataFetcher("books", graphQLDataFetchers.getBooksDataFetcher()))
                .type(newTypeWiring("Mutation")
                        .dataFetcher("addAuthor", graphQLDataFetchers.getAuthorMutator()))
                .type(newTypeWiring("Book")
                        .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))
                .type(newTypeWiring("Author")
                        .dataFetcher("books", graphQLDataFetchers.getBooksByAuthorDataFetcher()))
                .build();
    }

}
