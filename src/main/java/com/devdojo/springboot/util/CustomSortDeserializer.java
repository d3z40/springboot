//package com.devdojo.springboot.util;
//
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JavaType;
//import com.fasterxml.jackson.databind.JsonDeserializer;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.node.ArrayNode;
//import org.springframework.data.domain.Sort;
//
//import java.io.IOException;
//
//public class CustomSortDeserializer extends JsonDeserializer<Sort> {

//NAO NECESSARIO MAIS ESSA CLASSE PORQUE O org.springframework.data.domain.Sort; nao implementa mais o iterable e sim o streamable

//    private static final String CONTENT = "content";
//    private static final String NUMBER = "number";
//    private static final String SIZE = "size";
//    private static final String TOTAL_ELEMENTS = "totalElements";
//    private JavaType valueType;
//
//    @Override
//    public Sort deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
//        ArrayNode node = jsonParser.getCodec().readTree(jsonParser);
//
//        Sort.Order[] orders = new Sort.Order[node.size()];
//
//        int i = 0;
//        for (JsonNode json : node) {
//            orders[i] = new Sort.Order(Sort.Direction.valueOf(json.get("direction").asText()),
//                    json.get("property").asText());
//            i++;
//        }
//
//        return Sort.by(orders);
//
//    }
//}