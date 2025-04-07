package customer.upskilling.utilities;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import com.sap.cds.Result;
import com.sap.cds.ResultBuilder;
import com.sap.cds.services.cds.CdsReadEventContext;

public class Utilities {
    public static List<Map<String, Object>> convertListToMap(List<?> resultList) {
        ObjectMapper mapper = new ObjectMapper();

        List<Map<String, Object>> result = resultList
                .stream()
                .map(el -> mapper.convertValue(el, new TypeReference<Map<String, Object>>() {
                }))
                .collect(Collectors.toList());

        return result;
    }

    public static <T> List<T> convertJsonArrayToList(JsonArray ar, Class<T> clazz) {
        // Ottieni il tipo completo della lista usando TypeToken
        Type listType = TypeToken.getParameterized(List.class, clazz).getType();
        return new Gson().fromJson(ar, listType);
    }

    public static Result buildResultWithInlineCount(CdsReadEventContext context, List<?> resultList,
            Long l) {

        List<Map<String, Object>> result = Utilities.convertListToMap(resultList);
        ResultBuilder r = ResultBuilder.selectedRows(result);
        context.setResult(result);
        r.rowType(context.getResult().rowType());
        r.inlineCount(l);
        return r.result();
    }

}
